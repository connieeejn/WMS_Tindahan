package com.example.wms_tindahan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var signUpLoginBtn:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize UI components
        editTextUsername = findViewById(R.id.signUpNameEditTxt)
        editTextEmail = findViewById(R.id.signUpEmailEditTxt)
        editTextPassword = findViewById(R.id.signUpPasswordEditTxt)
        buttonSignUp = findViewById(R.id.signUpButton)
        signUpLoginBtn = findViewById(R.id.signUpLoginBtn)

        // Set button click listener
        buttonSignUp.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validate inputs
            if (validateInputs(username, email, password)) {
                // Create Retrofit instance and API service
                val apiService = RetrofitClient.getInstance(this)

                // Prepare request body
                val request = RegisterRequest(email,username, password)

                // Perform network call
                apiService.registerUser(request).enqueue(object : Callback<RegisterApiResponse> {
                    override fun onResponse(call: Call<RegisterApiResponse>, response: Response<RegisterApiResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val apiResponse = response.body()
                            Toast.makeText(this@SignUp, apiResponse?.message ?: "Sign up successful", Toast.LENGTH_SHORT).show()

                            // Navigate to Login activity after successful sign-up
                            val intent = Intent(this@SignUp, Login::class.java)  // Assuming LoginActivity is your login screen
                            startActivity(intent)

                            // Finish the SignUp activity so the user cannot go back to it
                            finish()
                        } else {
                            Toast.makeText(this@SignUp, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterApiResponse>, t: Throwable) {
                        Toast.makeText(this@SignUp, "Sign up failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        // Sign-up button click redirects to SignUpActivity
        signUpLoginBtn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

    }




    private fun validateInputs(username: String, email: String, password: String): Boolean {
        if (username.isEmpty()) {
            editTextUsername.error = "Username is required"
            editTextUsername.requestFocus()
            return false
        }
        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            editTextEmail.requestFocus()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Invalid email format"
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            editTextPassword.requestFocus()
            return false
        }

        return true
    }
}
