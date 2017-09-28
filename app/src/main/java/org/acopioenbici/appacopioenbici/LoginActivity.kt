package org.acopioenbici.appacopioenbici

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton

/**
 * A login screen that offers login via Twitter.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Twitter.initialize(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById(R.id.login_button) as TwitterLoginButton?

        if(loginButton != null) {
            loginButton.setCallback(object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {
                    // Do something with result, which provides a TwitterSession for making API calls
                }

                override fun failure(exception: TwitterException) {
                    // Do something on failure
                }
            })
        }
        else {

        }
    }
}

