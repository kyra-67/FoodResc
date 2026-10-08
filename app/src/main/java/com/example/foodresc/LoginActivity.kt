package com.example.foodresc

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    //sambungan komponen//
    private lateinit var toggleRole: MaterialButtonToggleGroup
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvCreateAccount: TextView

    // Terima email & role dari RegisterActivity
    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            etEmail.setText(result.data?.getStringExtra("EMAIL"))
            etPassword.text = null
            val role = result.data?.getStringExtra("ROLE")
            toggleRole.check(if (role == "volunteer") R.id.btnRoleVolunteer else R.id.btnRoleCommunity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toggleRole = findViewById(R.id.toggleRole)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvCreateAccount = findViewById(R.id.tvCreateAccount)

        btnLogin.setOnClickListener {
            if (validateInput()) {
                loginUser()
            }
        }
        tvCreateAccount.setOnClickListener {
            registerLauncher.launch(Intent(this, RegisterActivity::class.java))
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
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Invalid email format"
            isValid = false
        }

        if (password.isEmpty()) {
            tilPassword.error = "Please enter your password"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }

    //login guna data dummy dan semak role//
    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val selectedRole =
            if (toggleRole.checkedButtonId == R.id.btnRoleVolunteer) "volunteer" else "community"

        val user = DummyData.users.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        if (user == null) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
            return
        }

        if (user.role != selectedRole) {
            val roleName =
                if (selectedRole == "volunteer") "Volunteer Collector" else "Community User"
            Toast.makeText(this, "This account is not registered as $roleName", Toast.LENGTH_LONG).show()
            return
        }

        DummyData.currentUser = user
        Toast.makeText(this, "Welcome back, ${user.name}!", Toast.LENGTH_SHORT).show()
        val target = if (user.role == "volunteer") VolunteerMainActivity::class.java
        else MainActivity::class.java
        startActivity(Intent(this, target))
        finish()
    }
}