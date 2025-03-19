package com.example.worka1.ui.schedule.repository

import android.util.Log
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
            Log.e("ScheduleRepository", "Error fetching schedules", e)
            emptyList()
        }
    }
//    // Fetch vendor details (e.g., phone number, rating)
//    suspend fun getVendorDetails(vendorId: String): Map<String, Any>? {
//        return try {
//            db.collection("vendors").document(vendorId).get().await().data
//        } catch (e: Exception) {
//            null
//        }
//    }

    suspend fun getVendorDetails(vendorType : String, vendorId: String, itemName : String): Map<String, Any>? {
        return try {
            db.collection("vendors")
                .document(vendorType)
                .collection(itemName)
                .document(vendorId)
                .get()
                .await()
                .data
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Error fetching vendor details", e)
            null
        }
    }
//    // Fetch order details (e.g., order name, status)
//    suspend fun getOrderDetails(orderId: String): Map<String, Any>? {
//        return try {
//            db.collection("orders").document(orderId).get().await().data
//        } catch (e: Exception) {
//            null
//        }
//    }
suspend fun getOrderDetails(orderId: String): Map<String, Any>? {

//    fun getOrderDetails(orderId: String): Map<String, Any>? {
        val userId = auth.currentUser?.uid ?: return null
        return try {
            db.collection("users").document(userId)
                .collection("orders")
                .document(orderId)
                .get()
                .await()
                .data
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Error fetching order details", e)
            null
        }
    }


//    fun addMockData() {
//        val userId = auth.currentUser?.uid ?: return
//        val schedulesRef = db.collection("users").document(userId)
//            .collection("schedules")
//
//        val mockSchedules = listOf(
//            Schedule("Teresa Miranda", "Mani-Pedi & Lashes", Timestamp.now(), "Assigned"),
//            Schedule("Sameem", "Refrigerator Repair", Timestamp.now(), "Completed"),
//            Schedule("Vikas", "RO Service & Repair", Timestamp.now(), "Assigned")
//        )
//
//        mockSchedules.forEach { schedule ->
//            schedulesRef.add(schedule)
//                .addOnSuccessListener { println("Mock schedule added!") }
//                .addOnFailureListener { println("Failed to add schedule.") }
//        }
//    }
}