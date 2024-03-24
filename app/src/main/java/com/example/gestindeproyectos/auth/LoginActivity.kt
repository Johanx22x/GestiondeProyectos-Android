package com.example.gestindeproyectos.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.gestindeproyectos.MainActivity
import com.example.gestindeproyectos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var errorTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var resetPasswordTextView: TextView

    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        errorTextView = findViewById(R.id.error_text_view)
        loginButton = findViewById(R.id.login_button)
        resetPasswordTextView = findViewById(R.id.reset_password_text_view)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateInput(email, password)) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        resetPasswordTextView.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        NavUtils.navigateUpFromSameTask(this)
        return true
    }

    private fun validateInput(email: String, password: String): Boolean {
        // Add your own validation logic here
        return email.isNotEmpty() && password.isNotEmpty()
    }

    @SuppressLint("SetTextI18n")
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    updateUI(null)
                    checkForMultiFactorFailure(task.exception!!)
                }
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                // TODO: Handle the response, it doesn't say anything when the email doesn't exist
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error sending password reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in
            // Navigate to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // User is signed out
            // Show an error message
            errorTextView.text = "Authentication failed, please try again."
        }
    }

    private fun checkForMultiFactorFailure(e: Exception) {
        // TODO: Check this later, I don't consider this is necessary for now
        if (e is FirebaseAuthMultiFactorException) {
            Log.w(TAG, "multiFactorFailure", e)
            // Handle multi-factor authentication failure
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
