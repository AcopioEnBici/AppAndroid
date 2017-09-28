package org.acopioenbici.appacopioenbici

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession

class MainActivity : AppCompatActivity() {

    var currentSession : TwitterSession?                 = null
    var sessionManager : SessionManager<TwitterSession>? = null
    var mAuth          : FirebaseAuth?                   = null

    private var mTextMessage: TextView? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_donations -> {
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
        Twitter.initialize(this);
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = TwitterCore.getInstance().getSessionManager()
        currentSession = sessionManager?.getActiveSession()

        mTextMessage = findViewById(R.id.message) as TextView
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onStart() {
        super.onStart()

        if (mAuth?.getCurrentUser() == null
                || currentSession == null) {
            signout()
        }
    }

    fun signout() {
        FirebaseAuth.getInstance().signOut();

        currentSession?.id?.let {
            sessionManager?.clearSession(it)
        }

        val i = Intent(applicationContext, LoginActivity::class.java)
        startActivity(i);
        finish()
    }
}
