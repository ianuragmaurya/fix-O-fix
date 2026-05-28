package com.am.lapcart



import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseRegisterActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText

    lateinit var loginPage: MaterialTextView
    lateinit var btnRegister: AppCompatButton
    private lateinit var btnGoogle: LinearLayout
    private lateinit var googleSignInClient: GoogleSignInClient

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_register)

        getFCMToken()
        askNotificationPermission()


        etEmail = findViewById(R.id.fbEmail)
        etPassword = findViewById(R.id.fbPassword)
        loginPage = findViewById(R.id.loginPage)
        btnRegister = findViewById(R.id.btnRegisterFb)
        btnGoogle = findViewById(R.id.btnGoogle)


        auth = FirebaseAuth.getInstance()


        btnRegister.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {

                Toast.makeText(
                    this,
                    "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            registerUser(email, password)

        }
        loginPage.setOnClickListener {
            startActivity(Intent(this, FirebaseLoginActivity::class.java))
        }


        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        btnGoogle.setOnClickListener {
            signInWithGoogle()

        }
    }

    private fun signInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.result

                if (account.idToken != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Google Sign In Failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    private fun registerUser(email: String, password: String) {

        FirebaseCrashlytics.getInstance()
            .log("user started email register")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    val bundle = Bundle().apply {
                        putString(FirebaseAnalytics.Param.METHOD, "email")
                    }
                    MyApplication.analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)


                    Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, FirebaseLoginActivity::class.java))
                    finish()

                } else {

                    FirebaseCrashlytics.getInstance()
                        .recordException(task.exception ?: Exception("Register failed"))

                    Toast.makeText(this, task.exception?.message?: "Registration Failed", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    val bundle = Bundle().apply {
                        putString(FirebaseAnalytics.Param.METHOD, "google")

                    }
                    MyApplication.analytics.logEvent(
                        FirebaseAnalytics.Event.SIGN_UP, bundle
                    )

                    val user = auth.currentUser

                    Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT)
                        .show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    //this function for token saving in firebase
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    println("FCM TOKEN : $token")
                }
            }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }
}