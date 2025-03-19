package com.example.worka1.ui.schedule.repository

import com.example.worka1.ui.schedule.model.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

class ScheduleRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getUpcomingSchedules(): List<Schedule> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        return try {
            db.collection("users").document(userId)
                .collection("schedules")
                .whereGreaterThan("scheduledTime", Timestamp.now())
                .orderBy("scheduledTime", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Schedule::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addMockData() {
        val userId = auth.currentUser?.uid ?: return
        val schedulesRef = db.collection("users").document(userId)
            .collection("schedules")

        val mockSchedules = listOf(
            Schedule("Teresa Miranda", "Mani-Pedi & Lashes", Timestamp.now(), "Assigned"),
            Schedule("Sameem", "Refrigerator Repair", Timestamp.now(), "Completed"),
            Schedule("Vikas", "RO Service & Repair", Timestamp.now(), "Assigned")
        )

        mockSchedules.forEach { schedule ->
            schedulesRef.add(schedule)
                .addOnSuccessListener { println("Mock schedule added!") }
                .addOnFailureListener { println("Failed to add schedule.") }
        }
    }
}