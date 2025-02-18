package com.example.worka1.ui.account.helpandsupport.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worka1.databinding.FragmentClaimWarrantyBinding

class ClaimWarrantyFragment : Fragment() {

    private var _binding: FragmentClaimWarrantyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClaimWarrantyBinding.inflate(inflater, container, false)
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
            "Which services are covered under WorkA1 warranty?", "Do I have to pay for the service under \nwarranty?"

        )

        val adapter = ClaimWarrantyAdapter(names, findNavController())

        binding.claimWarrantyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.claimWarrantyRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
