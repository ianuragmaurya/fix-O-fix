package com.am.lapcart.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.am.lapcart.activities.FirebaseLoginActivity
import com.am.lapcart.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val pref = getSharedPreferences("user", MODE_PRIVATE)
            val token = pref.getString("token", null)

            if (!token.isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, FirebaseLoginActivity::class.java))
            }
            finish()


        }, 4000)
    }
}