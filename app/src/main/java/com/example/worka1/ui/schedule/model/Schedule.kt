package com.example.worka1.ui.schedule.model

import com.google.firebase.Timestamp

data class Schedule(
    val partnerName: String = "",
    val serviceDetails: String = "",
    val scheduledTime: Timestamp? = null,
    val status: String = ""  // e.g., "Assigned", "Completed"
)