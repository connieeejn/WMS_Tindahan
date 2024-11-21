package com.example.wms_tindahan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUp : AppCompatActivity() {
    private lateinit var apiService: WmsApiService

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText
    : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val baseUrl = readBaseUrl(this)
        apiService = RetrofitClient.getRetrofitInstance(baseUrl).create(WmsApiService::class.java)

        val backButton: ImageView = findViewById(R.id.signUpBackBtn)
        val nameEditText: EditText = findViewById(R.id.signUpNameEditTxt)
        val emailEditText: EditText = findViewById(R.id.signUpEmailEditTxt)
        val passwordEditText: EditText = findViewById(R.id.signUpPasswordEditTxt)
        val signUpBtn: Button = findViewById(R.id.signUpButton)
        val loginBtn: TextView = findViewById(R.id.signUpLoginBtn)


        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        signUpBtn.setOnClickListener {
            // TODO: create user then redirect to main page
            registerUser()
        }


        loginBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }

    private fun registerUser() {
        TODO("Not yet implemented")
    }
}