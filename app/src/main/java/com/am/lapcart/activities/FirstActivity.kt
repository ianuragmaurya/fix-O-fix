package com.am.lapcart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.am.lapcart.activities.FirebaseLoginActivity
import com.am.lapcart.activities.FirebaseRegisterActivity
import com.am.lapcart.R

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val pref = getSharedPreferences("user", MODE_PRIVATE)
        val token = pref.getString("token", null)
        if (!token.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val loginBtn = findViewById<AppCompatButton>(R.id.loginBtn)
        val registerBtn = findViewById<AppCompatButton>(R.id.registerBtn)

        loginBtn.setOnClickListener {

            val intent = Intent(this, FirebaseLoginActivity::class.java)
            startActivity(intent)

        }
        registerBtn.setOnClickListener {

            val intent = Intent(this, FirebaseRegisterActivity::class.java)

            startActivity(intent)
        }

        }
    }