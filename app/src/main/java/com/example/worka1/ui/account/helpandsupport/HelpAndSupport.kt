
package com.example.worka1.ui.account.helpandsupport

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
import com.example.worka1.databinding.FragmentHelpAndSupportBinding

class HelpAndSupport : Fragment() {

    private var _binding: FragmentHelpAndSupportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HelpAndSupportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpAndSupportBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val images = listOf(
            R.drawable.ic_profile_24,
            R.drawable.ic_profile_24,
            R.drawable.payment_method,
            R.drawable.payment_method,
            R.drawable.ic_profile_24,
            R.drawable.ic_profile_24
        )
        val names = listOf(
            "Account ",
            "Getting started with WorkA1",
            "Payment & WorkA1 Credits",
            "WorkA1 Plus Membership",
            "WorkA1 Safety",
            "Claim Warranty"
        )

        val adapter = HelpandSupportItemAdapter(images, names, findNavController())

        binding.allItemRecylerview.layoutManager = LinearLayoutManager(requireContext())
        binding.allItemRecylerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


