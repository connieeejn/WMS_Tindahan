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

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val backButton: ImageView = findViewById(R.id.loginBackBtn)
        val emailEditText: EditText = findViewById(R.id.loginEmailEditTxt)
        val passwordEditText: EditText = findViewById(R.id.loginPasswordEditTxt)
        val loginBtn: Button = findViewById(R.id.loginButton)
        val signUpBtn: TextView = findViewById(R.id.loginSignUpBtn)



        backButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }



        loginBtn.setOnClickListener {
            // TODO: authenticate then redirect to main page

            // if successful, redirect to main page
            val intent = Intent(this, Inventory::class.java)
            startActivity(intent)
        }


        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}