package com.am.lapcart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var btnLogin: Button
    lateinit var loaderLayout: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pref = getSharedPreferences("user", MODE_PRIVATE)
        val token = pref.getString("token", null)
        if (!token.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }




        loaderLayout = findViewById(R.id.loaderLayout)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        val txtRegister = findViewById<TextView>(R.id.txtRegister)

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        btnLogin.setOnClickListener {


            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            //always put validation before calling api

            if (email.isEmpty()) {
                edtEmail.error = "Email is required"
                edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Please enter a valid email"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                edtPassword.error = "Password is required"
                edtPassword.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                edtPassword.error = "Password must be at least 6 characters"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)

        }
    }
    // this function will be called when the user clicks on the login button
    private fun loginUser(

        email: String,
        password: String

    ) {
        loaderLayout.visibility = View.VISIBLE

            //create a request object for the login request to be sent to the server
        val request = LoginRequest(

            login = email,
            password = password

        )
// when login request match then called api and get response


        ApiClient.getApi()

            .loginUser(request)

            .enqueue(object
                : Callback<LoginResponse> {

                override fun onResponse(

                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>

                ) {
// when response is success then show the result
                    btnLogin.isEnabled = true

                    if (!response.isSuccessful) {
                        loaderLayout.visibility = View.GONE

                        Toast.makeText(this@LoginActivity, "Server error: ${response.code()}" , Toast.LENGTH_SHORT).show()
                        return

                    }
                    val result = response.body()

                    if (result == null || !result.success ) {
                            loaderLayout.visibility = View.GONE

                            Toast.makeText(this@LoginActivity, result?.message?: "Login failed", Toast.LENGTH_SHORT)
                                .show()
                            return

                        }
                    //check if the token is not null
                    val token = result.data?.token

                    if (token.isNullOrEmpty()) {
                        loaderLayout.visibility = View.GONE

                        Toast.makeText(this@LoginActivity, "Token is missing", Toast.LENGTH_SHORT).show()
                        return
                    }
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()


                    // save the token in shared preferences for save the user session
                     getSharedPreferences("user", MODE_PRIVATE)
                         .edit()
                         .putString("token", token)
                         .apply()

                    loaderLayout.visibility = View.GONE

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()


                        }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnLogin.isEnabled = true

                    loaderLayout.visibility = View.GONE

                    println("Error"+ t.message)

                    Toast.makeText(this@LoginActivity, t.message ?: "Something went wrong", Toast.LENGTH_SHORT
                    ).show()

                }

            })

    }
}