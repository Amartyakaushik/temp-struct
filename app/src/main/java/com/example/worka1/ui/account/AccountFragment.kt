package com.example.worka1.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worka1.R
import com.example.worka1.databinding.FragmentAccountBinding
import com.example.worka1.ui.authentication.LogInActivity
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    companion object {
        fun newInstance() = AccountFragment()
    }
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        val editButton = binding.editprofile
        editButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_edit)
        }
        val myBookingCard = binding.cardMyBookings
        val helpSupportCard = binding.cardHelpSupport

        myBookingCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_mybookings)
        }


        helpSupportCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_helpandsupport)
        }

        val images = listOf(
            R.drawable.ic_my_rating,
            R.drawable.ic_my_location,
            R.drawable.payment_method,
            R.drawable.baseline_settings_24,
            R.drawable.ic_profile_24
        )
        val names = listOf(
            "My rating",
            "Manage addresses",
            "Manage payment methods",
            "Settings",
            "About WorkA1",
        )

        val adapter = AccountAdapter(images, names, findNavController())

        binding.accountRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.accountRecyclerView.adapter = adapter
        val logout_button = view.findViewById<LinearLayout>(R.id.logout_button)
        val logout_button_text = view.findViewById<TextView>(R.id.logout_button_text)

        if(auth.currentUser == null){
            logout_button_text.text = "Login"
            logout_button.setOnClickListener {
                val intent = Intent(requireContext(), LogInActivity::class.java)
                startActivity(intent)
            }
        }
        else{
            logout_button_text.text = "Logout"
            auth.signOut()
            logout_button.setOnClickListener {
                val intent = Intent(requireContext(), LogInActivity::class.java)
                startActivity(intent)
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
