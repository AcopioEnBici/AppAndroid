package org.acopioenbici.appacopioenbici

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import org.jetbrains.anko.longToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener



class MainActivity : AppCompatActivity() {

    var currentSession : TwitterSession?                 = null
    var sessionManager : SessionManager<TwitterSession>? = null
    var user           : FirebaseUser?                   = null
    var db             : FirebaseDatabase?               = null
    var activo         : Boolean                         = false

    private var mTextMessage: TextView? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_donations -> {
                validateUser()
                mTextMessage!!.setText(R.string.title_donations)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_centers -> {
                mTextMessage!!.setText(R.string.title_centers)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_signout -> {
                signout()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Twitter.initialize(this);
        sessionManager = TwitterCore.getInstance().getSessionManager()
        currentSession = sessionManager?.getActiveSession()

        user = FirebaseAuth.getInstance()?.getCurrentUser()
        db = FirebaseDatabase.getInstance()

        // Checa si el voluntario esta activo
        db?.getReference("/volunteers")?.
                child(user?.uid)?.child("active")?.
                addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        activo = snapshot.value as Boolean
                        validateUser()
                    }
                    override fun onCancelled(error: DatabaseError?) {
                        longToast(error.toString())
                    }
                })

        // Obtiene donaciones
        db?.getReference("donations")?.limitToFirst(10)?.
                addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        mTextMessage?.setText(snapshot.value.toString())
                    }

                    override fun onCancelled(error: DatabaseError?) {
                        longToast(error.toString())
                    }
                })

        mTextMessage = findViewById(R.id.message) as TextView
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onStart() {
        super.onStart()
        validateUser()
    }

    fun validateUser() {
        if (user == null || currentSession == null) signout()
    }

    fun signout() {
        FirebaseAuth.getInstance()?.signOut()

        currentSession?.id?.let {
            sessionManager?.clearSession(it)
        }

        val i = Intent(applicationContext, LoginActivity::class.java)
        startActivity(i);
        finish()
    }
}
