package com.example.foodresc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    //sambungan komponen//
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var toggleRole: MaterialButtonToggleGroup
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var progressLogin: ProgressBar
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvCreateAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //pintu masuk ke firebase and firestore//
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        toggleRole = findViewById(R.id.toggleRole)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressLogin = findViewById(R.id.progressLogin)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvCreateAccount = findViewById(R.id.tvCreateAccount)

        btnLogin.setOnClickListener {
            if (validateInput()) {
                loginUser()
            }
        }

        tvForgotPassword.setOnClickListener {
            resetPassword()
        }
    }

    //semakan data input dari user//
    private fun validateInput(): Boolean {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        tilEmail.error = null
        tilPassword.error = null

        var isValid = true

        if (email.isEmpty()) {
            tilEmail.error = "Please enter your email"
            isValid = false
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Invalid email format"
            isValid = false
        }

        if (password.isEmpty()) {
            tilPassword.error = "Please enter your password"
            isValid = false
        }

        else if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }

    //login firebase and semak role//
    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val selectedRole =
            if (toggleRole.checkedButtonId == R.id.btnRoleVolunteer) "volunteer" else "community"

        setLoading(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener

                db.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        val role = doc.getString("role")
                        setLoading(false)

                        if (role == selectedRole) {
                            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            auth.signOut()
                            val roleName =
                                if (selectedRole == "volunteer") "Volunteer Collector" else "Community User"
                            Toast.makeText(this, "This account is not registered as $roleName", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        auth.signOut()
                        setLoading(false)
                        Toast.makeText(this, "Failed to load account data. Please try again.", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener { e ->
                setLoading(false)
                val message = when (e) {
                    is FirebaseAuthInvalidCredentialsException,
                    is FirebaseAuthInvalidUserException -> "Invalid email or password"
                    is FirebaseNetworkException -> "No internet connection"
                    else -> "Login failed. Please try again."
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
    }

    private fun setLoading(isLoading: Boolean) {
        progressLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
    }

    //forgot pass//
    private fun resetPassword() {
        val email = etEmail.text.toString().trim()
        tilEmail.error = null

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Enter a valid email to reset your password"
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "If this email is registered, a reset link has been sent.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.e("ResetPassword", "Error: ${e.message}", e)
                val message = if (e is FirebaseNetworkException) "No internet connection"
                else "Failed to send reset email. Please try again."
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
    }
}