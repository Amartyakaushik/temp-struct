package com.example.worka1.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.example.worka1.MainActivity
import com.example.worka1.databinding.ActivityLogInBinding
import com.example.worka1.ui.authentication.utils.GoogleSignInHelper
import com.example.worka1.utils.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInHelper: GoogleSignInHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        googleSignInHelper = GoogleSignInHelper(this)
        googleSignInHelper.initialize()
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        binding.logInBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showAlertDialog("Invalid Email", "Please enter a valid email address.")
                return@setOnClickListener
            }
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            if (user.isEmailVerified) {
                                val userId = user.uid
                                FirebaseFirestore.getInstance().collection("users")
                                    .document(userId).get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()){
                                            val userName = document.getString("userName") ?: "User"
                                            Toast.makeText(this, "Welcome ${userName}", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        }else{
                                            // Save user to Firestore after successful login
                                            val phoneNumber = intent.getStringExtra("phoneNumber")
                                            val userName = intent.getStringExtra("userName")
                                            UserRepository.saveUserToFirestore(user, userName, phoneNumber,
                                                onSuccess = {
                                                    Toast.makeText(this, "Welcome ${userName}", Toast.LENGTH_SHORT).show()
                                                    startActivity(Intent(this, MainActivity::class.java))
                                                    finish()
                                                },
                                                onFailure = {
                                                    Toast.makeText(this, "Failed to store user data", Toast.LENGTH_SHORT).show()
                                                })
                                        }
                                    }
                            } else {
                                showAlertDialog("Verify Email", "Please verify your email before logging in.")
                                auth.signOut() // Sign out the user to prevent access
                            }
                        }
                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.googleBtn.setOnClickListener {
            googleSignInHelper.signInWithGoogle()
        }
        binding.forgetPassBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email in the email field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            resetPassword(email)
        }
    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignInHelper.handleSignInResult(requestCode, data)
    }
    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
    // Function to send password reset email
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}