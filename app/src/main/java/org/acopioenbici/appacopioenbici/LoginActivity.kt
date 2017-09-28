package org.acopioenbici.appacopioenbici

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * A login screen that offers login via Twitter.
 */
class LoginActivity : AppCompatActivity() {
    var currentSession = null as TwitterSession?
    var loginButton    = null as TwitterLoginButton?

    override fun onCreate(savedInstanceState: Bundle?) {
        Twitter.initialize(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        currentSession = TwitterCore.getInstance().getSessionManager().getActiveSession() as TwitterSession?
        loginButton    = findViewById(R.id.login_button) as TwitterLoginButton?

        // Check if Current Session
        currentSession?.getUserName()?.let { longToast(it) }

        // Twitter Login Logic
        loginButton?.setCallback(object : Callback<TwitterSession>() {
            // Login Exitoso
            override fun success(result: Result<TwitterSession>) {
                longToast(result.toString())
                if (true) {
                    launchMain()
                } else {
                    longToast("Usuario No Activo")
                }
            }

            // Login Fallo
            override fun failure(exception: TwitterException) {
                longToast(exception.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the login button.
        loginButton?.onActivityResult(requestCode, resultCode, data)
    }

    fun launchMain() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i);
    }
}

