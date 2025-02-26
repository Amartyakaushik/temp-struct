package com.example.worka1.ui.account.helpandsupport.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worka1.databinding.FragmentMembershipBinding

class MembershipFragment : Fragment() {

    private var _binding: FragmentMembershipBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembershipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val purchaseNames = listOf(
            "What are the benefits of the membership?",
            "What are the maximum discount that\nI can get by using WorkA1 Plus?",
            "How do I buy the membership?",
            "Can I pay for membership with cash\non delivery?",
            "Can I share membership with family?"
        )

        val modificationNames = listOf(
            "How do I cancel my membership plan?",
            "Can I pause my membership"
        )

        val purchaseAdapter = MembershipAdapter(purchaseNames, findNavController())
        val modificationAdapter = MembershipAdapter(modificationNames, findNavController())

        binding.membershipStartPurchaseRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.membershipStartPurchaseRecyclerview.adapter = purchaseAdapter

        binding.membershipModificationRecylerview.layoutManager = LinearLayoutManager(requireContext())
        binding.membershipModificationRecylerview.adapter = modificationAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
