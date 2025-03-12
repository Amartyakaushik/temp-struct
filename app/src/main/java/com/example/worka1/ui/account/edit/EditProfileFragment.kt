package com.example.worka1.ui.account.edit

import android.os.Bundle
import android.view.*
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

        firestore = FirebaseFirestore.getInstance()
        usernameEditText = view.findViewById(R.id.editFullName)
        emailEditText = view.findViewById(R.id.editEmail)
        phoneEditText = view.findViewById(R.id.editPhone)

        val userId = getUserId()

        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return view
        }

        loadUserData(userId)

        val btnSave: Button = view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            updateUserData(userId)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        return view
    }

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun loadUserData(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    usernameEditText.setText(document.getString("userName") ?: "")
                    emailEditText.setText(document.getString("email") ?: "")
                    phoneEditText.setText(document.getString("phoneNumber") ?: "")
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserData(userId: String) {
        val newUsername = usernameEditText.text.toString().trim()
        val newEmail = emailEditText.text.toString().trim()
        val newPhone = phoneEditText.text.toString().trim()

        if (newUsername.isBlank() || newEmail.isBlank() || newPhone.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userUpdates = hashMapOf<String, Any>(
            "userName" to newUsername,
            "email" to newEmail,
            "phoneNumber" to newPhone
        )

        firestore.collection("users").document(userId).set(userUpdates, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show()
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
