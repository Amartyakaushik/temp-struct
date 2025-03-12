package com.example.worka1.ui.account.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.worka1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class EditProfileFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val userId = auth.currentUser?.uid ?: "DH8j7CdzJHioSBFlrPav"
        firestore = FirebaseFirestore.getInstance()
        if (userId.isBlank()) {
            Toast.makeText(requireContext(), "User not authenticated. Returning to previous screen.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return view
        }
        usernameEditText = view.findViewById(R.id.editFullName)
        emailEditText = view.findViewById(R.id.editEmail)
        phoneEditText = view.findViewById(R.id.editPhone)

        loadUserData()

        val btnSave: Button = view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            updateUserData()
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        return view
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid  ?: "DH8j7CdzJHioSBFlrPav"
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: ""
                        val email = document.getString("email") ?: ""
                        val phone = document.getString("phone") ?: ""

                        usernameEditText.setText(username)
                        emailEditText.setText(email)
                        phoneEditText.setText(phone)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun updateUserData() {
        val newUsername = usernameEditText.text.toString().trim()
        val newEmail = emailEditText.text.toString().trim()
        val newPhone = phoneEditText.text.toString().trim()

        if (newUsername.isNotBlank() && newEmail.isNotBlank() && newPhone.isNotBlank()) {

            val userId = auth.currentUser?.uid ?: "DH8j7CdzJHioSBFlrPav"

            val userUpdates = hashMapOf<String, Any>()

            if (newUsername != "") userUpdates["username"] = newUsername
            if (newEmail != "") userUpdates["email"] = newEmail
            if (newPhone != "") userUpdates["phone"] = newPhone

            if (userId != null) {
                firestore.collection("users").document(userId).set(userUpdates, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(requireContext(), "Failed to update profile in Firestore.", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
