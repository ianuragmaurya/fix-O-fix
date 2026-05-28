package com.am.lapcart

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtPhone: EditText
    lateinit var edtPassword: EditText
    lateinit var edtAddress: EditText
    lateinit var edtCity: EditText
    lateinit var edtState: EditText
    lateinit var edtPincode: EditText
    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        edtPassword = findViewById(R.id.edtPassword)
        edtAddress = findViewById(R.id.edtAddress)
        edtCity = findViewById(R.id.edtCity)
        edtState = findViewById(R.id.edtState)
        edtPincode = findViewById(R.id.edtPincode)

        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val phone = edtPhone.text.toString()
            val password = edtPassword.text.toString()
            val address = edtAddress.text.toString()
            val city = edtCity.text.toString()
            val state = edtState.text.toString()
            val pincode = edtPincode.text.toString()

            if (!validateInputs(name, email, phone, password, address, city, state, pincode))
                return@setOnClickListener

            registerUser(name, email, phone, password, address, city, state, pincode)
        }
    }
                private fun validateInputs(
                    name: String,
                    email: String,
                    phone: String,
                    password: String,
                    address: String,
                    city: String,
                    state: String,
                    pincode: String
                ): Boolean {


         if (name.isEmpty()) {
             edtName.error = "Enter Name"
             edtName.requestFocus()
             return false
         }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Enter valid email"
                edtEmail.requestFocus()
                return false
            }
                    if (!phone.matches(Regex("^[0-9]{10}$"))) {
                edtPhone.error = "Enter Valid Phone Number"
                edtPhone.requestFocus()
                return false
            }
            if (password.isEmpty()) {
                edtPassword.error = "Enter Password"
                edtPassword.requestFocus()
                return false
            }
              if (address.isEmpty()) {
                  edtAddress.error = "Enter Address"
                  edtAddress.requestFocus()
                  return false
              }
               if (city.isEmpty()) {
                  edtCity.error = "Enter City"
                  edtCity.requestFocus()
                   return false
            }
                if (state.isEmpty()) {
                    edtState.error = "Enter State"
                    edtState.requestFocus()
                    return false

              }
                if (pincode.length != 6) {
                  edtPincode.error = "Enter valid pincode"
                  edtPincode.requestFocus()
                    return false
              }
                    return true
    }

        private fun registerUser(

            name: String,
            email: String,
            phone: String,
            password: String,
            address: String,
            city: String,
            state: String,
            pincode: String


        ) {

            val request = RegisterRequest(
                name = name,
                email = email,
                phone_number = phone,
                password = password,
                address = address,
                city = city,
                state = state,
                pincode = pincode,
                referral_code = ""

                )
            ApiClient.getApi()
                .registerUser(request)
                .enqueue(object : Callback<RegisterResponse> {

                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result?.success == true) {


                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    result?.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Server Error: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                    override fun onFailure(
                        call: Call<RegisterResponse>, t: Throwable

                    ) {
                        println("Error" + t.message)

                        Toast.makeText(
                            this@RegisterActivity, t.message, Toast.LENGTH_SHORT
                        ).show()

                    }

                })
        }
    }
