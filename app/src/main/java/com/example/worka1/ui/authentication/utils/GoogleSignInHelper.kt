package com.example.worka1.ui.authentication.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.worka1.MainActivity
import com.example.worka1.R
import com.example.worka1.utils.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInHelper(private val activity: Activity) {
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient

    fun initialize() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)) // Replace with your Web Client ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(activity, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    // Check if user already exists in FireStore
                    val userRef = Firebase.firestore.collection("users").document(it.uid)

                    userRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            // If user already exists, just redirect to MainActivity
                            Toast.makeText(activity, "Welcome back, ${it.displayName}!", Toast.LENGTH_SHORT).show()
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                            activity.finish()
                        } else {
                            // If user doesn't exist, save data and redirect
                            UserRepository.saveUserToFirestore(
                                user = it,
                                userName = it.displayName,
                                phoneNumber = it.phoneNumber,
                                onSuccess = {
                                    Toast.makeText(activity, "Welcome, ${it.displayName}!", Toast.LENGTH_SHORT).show()
                                    activity.startActivity(Intent(activity, MainActivity::class.java))
                                    activity.finish()
                                },
                                onFailure = { exception ->
                                    Toast.makeText(
                                        activity,
                                        "Failed to save user: ${exception.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            } else {
                Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}