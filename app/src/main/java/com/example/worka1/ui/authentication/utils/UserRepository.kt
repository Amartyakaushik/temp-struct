package com.example.worka1.utils

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

object UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    fun saveUserToFirestore(
        user: FirebaseUser?,
        userName: String? = null,
        phoneNumber: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (user == null) {
            onFailure(Exception("User is null"))
            return
        }
        val userId = user.uid
        val userMap = hashMapOf(
            "userId" to userId,
            "userName" to (userName ?: user.displayName ?: "Unknown User"), // Handle missing display name
            "email" to (user.email ?: "No Email"), // Handle missing email
            "profilePicture" to (user.photoUrl.toString()?: ""),
            "phoneNumber" to phoneNumber

        )
        firestore.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
