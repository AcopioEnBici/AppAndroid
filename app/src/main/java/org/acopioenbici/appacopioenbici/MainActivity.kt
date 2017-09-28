package org.acopioenbici.appacopioenbici

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession

class MainActivity : AppCompatActivity() {

    var currentSession : TwitterSession?                 = null
    var sessionManager : SessionManager<TwitterSession>? = null

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
                currentSession?.id?.let {
                    sessionManager?.clearSession(it)
                }
                exitToLogin()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Twitter.initialize(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = TwitterCore.getInstance().getSessionManager()
        currentSession = sessionManager?.getActiveSession()

        if(currentSession == null) {
            exitToLogin()
        }

        mTextMessage = findViewById(R.id.message) as TextView
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun exitToLogin() {
        val i = Intent(applicationContext, LoginActivity::class.java)
        startActivity(i);
    }
}
