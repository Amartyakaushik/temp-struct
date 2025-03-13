package com.example.worka1.ui.schedule.ui

import com.example.worka1.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.ui.schedule.repository.ScheduleRepository
import com.example.worka1.ui.schedule.viewModel.ScheduleViewModel

class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyScheduleView: LinearLayout
    private lateinit var exploreService: TextView
    private val repository = ScheduleRepository()
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView_schedule)
        emptyScheduleView = view.findViewById(R.id.empty_schedule_view)
        exploreService = view.findViewById(R.id.explore_service)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        scheduleAdapter = ScheduleAdapter(emptyList())
        recyclerView.adapter = scheduleAdapter

        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (schedules.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                emptyScheduleView.visibility = View.GONE
                scheduleAdapter.updateData(schedules)
            } else {
                recyclerView.visibility = View.GONE
                emptyScheduleView.visibility = View.VISIBLE
            }
        }

        viewModel.fetchSchedules()

         repository.addMockData()

        exploreService.setOnClickListener {
            findNavController().navigate(R.id.navigation_services)
        }
    }
}
