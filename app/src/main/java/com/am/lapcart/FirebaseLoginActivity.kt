package com.am.lapcart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseLoginActivity : AppCompatActivity() {

    lateinit var etEmail : TextInputEditText
    lateinit var etPassword : TextInputEditText
    lateinit var loginbtnFb : AppCompatButton

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_login)

        etEmail = findViewById(R.id.loginEmailFb)
        etPassword = findViewById(R.id.loginPasswordFb)
        loginbtnFb = findViewById(R.id.loginbtnFb)

        auth = FirebaseAuth.getInstance()

        loginbtnFb.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty()||password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginUser(email, password)
            }
        }

    private fun loginUser(email: String, password: String) {

        FirebaseCrashlytics.getInstance()
            .log("user started email login ")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val bundle = Bundle().apply {
                        putString(FirebaseAnalytics.Param.METHOD, email)
                    }
                    MyApplication.analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {

                    FirebaseCrashlytics.getInstance()
                        .recordException(task.exception?: Exception("Login failed"))

                    Toast.makeText(this, task.exception?.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}