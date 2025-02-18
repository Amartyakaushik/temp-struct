package com.example.worka1.ui.account

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.worka1.R
import com.example.worka1.ui.account.edit.EditProfileFragment
import com.google.android.material.card.MaterialCardView

class AccountFragment : Fragment() {


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
    ): View {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val editButton = view.findViewById<TextView>(R.id.editprofile)
        editButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_edit)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val myBookingCard = view.findViewById<MaterialCardView>(R.id.card_my_bookings)
        val paymentsCard = view.findViewById<MaterialCardView>(R.id.card_payments)
        val helpSupportCard = view.findViewById<MaterialCardView>(R.id.card_help_support)


        myBookingCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_mybookings)
        }

        paymentsCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_native_device)
        }

        helpSupportCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_helpandsupport)
        }

        return view
    }
}