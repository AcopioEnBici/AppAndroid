package org.acopioenbici.appacopioenbici

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.TwitterAuthProvider
import com.google.firebase.auth.AuthCredential
import com.twitter.sdk.android.core.TwitterSession





/**
 * A login screen that offers login via Twitter.
 */
class LoginActivity : AppCompatActivity() {
    var sessionManager : SessionManager<TwitterSession>? = null
    var currentSession : TwitterSession?     = null
    var loginButton    : TwitterLoginButton? = null
    var mAuth          : FirebaseAuth?       = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Twitter.initialize(this);
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = TwitterCore.getInstance().getSessionManager()
        currentSession = sessionManager?.getActiveSession()
        loginButton    = findViewById(R.id.login_button) as TwitterLoginButton?

        // Check if Current Session
        currentSession?.getUserName()?.let { longToast(it) }

        // Twitter Login Logic
        loginButton?.setCallback(object : Callback<TwitterSession>() {
            // Login Exitoso
            override fun success(result: Result<TwitterSession>) {
                handleTwitterSession(result.data)
            }

            // Login Fallo
            override fun failure(exception: TwitterException) {
                longToast(exception.toString())
            }
        })
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth?.getCurrentUser()?.let { launchMain() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the login button.
        loginButton?.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleTwitterSession(session: TwitterSession) {
        val credential = TwitterAuthProvider.getCredential(
                session.authToken.token,
                session.authToken.secret)

        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        launchMain()
                    } else {
                        // If sign in fails, display a message to the user.
                        longToast("Authentication failed.")
                    }
                }
    }

    fun launchMain() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i);
    }
}

