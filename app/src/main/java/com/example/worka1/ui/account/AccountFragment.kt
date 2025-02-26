package com.example.worka1.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worka1.R
import com.example.worka1.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

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
        val paymentsCard = binding.cardPayments
        val helpSupportCard = binding.cardHelpSupport

        myBookingCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_mybookings)
        }

        paymentsCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_native_device)
        }

        helpSupportCard.setOnClickListener {
            findNavController().navigate(R.id.navigation_helpandsupport)
        }

        val images = listOf(
            R.drawable.ic_my_plan,
            R.drawable.wallet,
            R.drawable.ic_plus_membership,
            R.drawable.ic_my_rating,
            R.drawable.ic_my_location,
            R.drawable.payment_method,
            R.drawable.baseline_settings_24,
            R.drawable.ic_profile_24
        )
        val names = listOf(
            "My Plans",
            "Wallet",
            "Plus membership",
            "My rating",
            "Manage addresses",
            "Manage payment methods",
            "Settings",
            "About WorkA1",

        )

        val adapter = AccountAdapter(images, names, findNavController())

        binding.accountRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.accountRecyclerView.adapter = adapter

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
