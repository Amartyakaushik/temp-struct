package com.example.worka1.ui.account.mybooking

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.worka1.R

class MybookingFragment : Fragment() {

    companion object {
        fun newInstance() = MybookingFragment()
    }

    private val viewModel: MybookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_mybooking, container, false)

        val exploreService: TextView = view.findViewById(R.id.explore_service)
        exploreService.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }

        return view
    }
}