package com.example.wms_tindahan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginBtn: Button
    private lateinit var signUpBtn: TextView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        emailEditText = findViewById(R.id.loginEmailEditTxt)
        passwordEditText = findViewById(R.id.loginPasswordEditTxt)
        loginBtn = findViewById(R.id.loginButton)
        signUpBtn = findViewById(R.id.loginSignUpBtn)
        backButton = findViewById(R.id.loginBackBtn)

        // Back button click redirects to SignUpActivity
        backButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        // Sign-up button click redirects to SignUpActivity
        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        // Login button click logic
        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInputs(email, password)) {
                performLogin(email, password)
            }
        }
    }

    // Validates user input
    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Invalid email format"
            emailEditText.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return false
        }
        return true
    }

    // Perform login using Retrofit
    private fun performLogin(email: String, password: String) {
        val apiService = RetrofitClient.getInstance(this)
        val loginRequest = LoginRequest(email, password)

        apiService.loginUser(loginRequest).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()
                    Toast.makeText(this@Login, apiResponse?.message ?: "Login successful", Toast.LENGTH_SHORT).show()

                    // Redirect to Inventory activity
                    startActivity(Intent(this@Login, Inventory::class.java))
                    finish()
                } else {
                    Toast.makeText(this@Login, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Login, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
