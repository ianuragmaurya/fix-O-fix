package com.am.lapcart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val loginBtn = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.loginBtn)
        val registerBtn = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.registerBtn)

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
