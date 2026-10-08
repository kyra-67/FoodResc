package com.example.foodresc

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
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

class RegisterActivity : AppCompatActivity() {

    private lateinit var toggleRole: MaterialButtonToggleGroup
    private lateinit var tilName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toggleRole = findViewById(R.id.toggleRole)
        tilName = findViewById(R.id.tilName)
        tilEmail = findViewById(R.id.tilEmail)
        tilPhone = findViewById(R.id.tilPhone)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener { registerUser() }
        findViewById<TextView>(R.id.tvLogin).setOnClickListener { finish() }
    }

    private fun registerUser() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        val role = if (toggleRole.checkedButtonId == R.id.btnRoleVolunteer) "volunteer" else "community"

        listOf(tilName, tilEmail, tilPhone, tilPassword, tilConfirmPassword).forEach { it.error = null }

        var isValid = true

        if (name.isEmpty()) {
            tilName.error = "Please enter your full name"
            isValid = false
        }

        if (email.isEmpty()) {
            tilEmail.error = "Please enter your email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Invalid email format"
            isValid = false
        } else if (DummyData.users.any { it.email.equals(email, ignoreCase = true) }) {
            tilEmail.error = "This email is already registered"
            isValid = false
        }

        if (!Regex("^01\\d{8,9}$").matches(phone)) {
            tilPhone.error = "Enter a valid Malaysian phone number (e.g. 0123456789)"
            isValid = false
        }

        if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (confirmPassword != password || confirmPassword.isEmpty()) {
            tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        }

        if (!isValid) return

        // Simpan user baru
        DummyData.users.add(User(name, email, phone, password, role))
        Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_LONG).show()

        // Hantar email & role balik ke Login
        val result = Intent()
        result.putExtra("EMAIL", email)
        result.putExtra("ROLE", role)
        setResult(RESULT_OK, result)
        finish()
    }
}