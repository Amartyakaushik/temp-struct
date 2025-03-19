package com.example.worka1.ui.schedule.model

import com.google.firebase.Timestamp

data class Schedule(
    val id: String = "", // Unique ID for the schedule
    val orderId: String = "", // ID of the associated order
    val vendorType: String = "", // Type of vendor (e.g., "Beautician", "Technician")
    val vendorId: String = "",
    val phoneNumber: String = "", // Vendor's phone number for the call button
    val partnerName: String = "",
    val serviceDetails: String = "",
    val scheduledTime: Timestamp? = null,
    val status: String = ""  // e.g., "Assigned", "Completed"
)