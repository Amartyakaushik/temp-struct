package com.example.worka1.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.worka1.databinding.ActivitySignUpBinding
import com.example.worka1.ui.authentication.utils.GoogleSignInHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        googleSignInHelper = GoogleSignInHelper(this)
        googleSignInHelper.initialize()
        val phoneNumber = binding.phoneNumber.text.toString().trim()
        Log.d("SS", "Phone Number entered: $phoneNumber")
//        val phoneNumberRegex = "^[+]?[0-9]{10,13}\$".toRegex()
//        if (!phoneNumber.matches(phoneNumberRegex)) {
//            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
//        }

        binding.navBack.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
        binding.navLogin.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
        binding.regstrBtn.setOnClickListener {
            val userName = binding.userName.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmsPassword = binding.cnfrmPassword.text.toString().trim()
            if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmsPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the details first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmsPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser? = auth.currentUser
                        firebaseUser?.sendEmailVerification()
                            ?.addOnCompleteListener {
                                Toast.makeText(this, "Verification Email Sent", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                val intent = Intent(this, LogInActivity::class.java)
                                intent.putExtra("phoneNumber", phoneNumber)
                                intent.putExtra("userName", userName)
                                startActivity(intent)
                                finish()
                            }?.addOnFailureListener {
                            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Authentication Failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.googleBtn.setOnClickListener {
            googleSignInHelper.signInWithGoogle()
        }
    }

     @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignInHelper.handleSignInResult(requestCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("SignUpActivity destroyed")
        if (::authStateListener.isInitialized) {
            auth.removeAuthStateListener(authStateListener)
        }
    }
}
