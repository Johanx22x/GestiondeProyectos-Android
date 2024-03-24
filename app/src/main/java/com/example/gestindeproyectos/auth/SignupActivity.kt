package com.example.gestindeproyectos.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestindeproyectos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var errorTextView: TextView
    private lateinit var signupButton: Button


    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        errorTextView = findViewById(R.id.error_text_view)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        // Email must be from the @itcr.ac.cr or @estudiantec.cr domain
        val email = emailEditText.text.toString()
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Required."
            valid = false
        } else if (!email.endsWith("@itcr.ac.cr") && !email.endsWith("@estudiantec.cr")) {
            emailEditText.error = "Invalid email."
            valid = false
        } else {
            emailEditText.error = null
        }

        val password = passwordEditText.text.toString()
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Required."
            valid = false
        } else {
            passwordEditText.error = null
        }

        return valid
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in
            // Navigate to the MainActivity
            finish()
        } else {
            // User is signed out
            // Show an error message
            errorTextView.text = "Authentication failed."
        }
    }

    companion object {
        private const val TAG = "SignupActivity"
    }
}
