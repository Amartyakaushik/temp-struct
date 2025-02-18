package com.example.worka1.ui.account.helpandsupport.help

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worka1.R
import com.example.worka1.databinding.FragmentAccountHelpBinding
import com.example.worka1.databinding.FragmentPaymentAndCreditBinding

class PaymentAndCreditFragment : Fragment() {

    private var _binding:FragmentPaymentAndCreditBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentAndCreditBinding.inflate(inflater, container, false)
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
            "Iam unable to make payment", "How do I check my wallet balance?", "How do I use my WorkA1 credits?",
            "Can I extend the validity of rewards?","How does referral works?","I have not received a reward for referral","Where can I see my saved payment \ndeatils?"
        )

        val adapter = AccountHelpAdapter(names, findNavController())

        binding.paymentCreditRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.paymentCreditRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
