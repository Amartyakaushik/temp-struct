package com.example.worka1.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class ScheduleFragment : Fragment() {

    private lateinit var recyclerViewSchedule: RecyclerView
    private lateinit var emptyScheduleView: LinearLayout
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        recyclerViewSchedule = view.findViewById(R.id.recyclerView_schedule)
        emptyScheduleView = view.findViewById(R.id.empty_schedule_view)
        val exploreService: TextView = view.findViewById(R.id.explore_service)

        exploreService.setOnClickListener {
            findNavController().navigate(R.id.navigation_services)
        }

        val scheduledItems = getScheduledItems()

        if (scheduledItems.isNotEmpty()) {
            recyclerViewSchedule.visibility = View.VISIBLE
            emptyScheduleView.visibility = View.GONE

            recyclerViewSchedule.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewSchedule.adapter = ScheduleAdapter(scheduledItems)
        } else {
            recyclerViewSchedule.visibility = View.GONE
            emptyScheduleView.visibility = View.VISIBLE
        }

        return view
    }

    private fun getScheduledItems(): List<String> {
        return listOf()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
