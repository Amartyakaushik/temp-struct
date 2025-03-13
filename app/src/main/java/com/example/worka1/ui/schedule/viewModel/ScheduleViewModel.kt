package com.example.worka1.ui.schedule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worka1.ui.schedule.model.Schedule
import com.example.worka1.ui.schedule.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()
    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>> get() = _schedules

    fun fetchSchedules() {
        viewModelScope.launch {
            _schedules.value = repository.getUpcomingSchedules()
        }
    }
}
