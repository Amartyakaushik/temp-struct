package com.example.worka1.ui.account.helpandsupport.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worka1.databinding.FragmentAccountHelpBinding

class AccountHelp : Fragment() {

    private var _binding: FragmentAccountHelpBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountHelpBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val names = listOf(
            "I want to change my phone number",
            "Where can I check my saved addresses?",
            "I want to change my email address",
            "Where can I see my saved payment\ndetails?"
        )

        val adapter = AccountHelpAdapter(names, findNavController())

        binding.accountRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.accountRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
