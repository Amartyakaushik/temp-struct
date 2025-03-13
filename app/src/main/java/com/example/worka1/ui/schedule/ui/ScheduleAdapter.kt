package com.example.worka1.ui.schedule.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.schedule.model.Schedule
class ScheduleAdapter(private var schedules: List<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceDetails: TextView = itemView.findViewById(R.id.tvServiceDetails)
        private val scheduledTime: TextView = itemView.findViewById(R.id.tvScheduledTime)
        private val partnerName: TextView = itemView.findViewById(R.id.tvPartnerName)
        private val partnerRating: TextView = itemView.findViewById(R.id.tvPartnerRating)
        private val partnerReviews: TextView = itemView.findViewById(R.id.tvPartnerReviews)
        private val imgPartnerProfile: ImageView = itemView.findViewById(R.id.imgPartnerProfile)
        private val btnCall: ImageView = itemView.findViewById(R.id.btnCall)
        private val btnNeedHelp: LinearLayout = itemView.findViewById(R.id.btnNeedHelp)
        private val btnViewProject: LinearLayout = itemView.findViewById(R.id.btnViewProject)

        fun bind(schedule: Schedule) {
            serviceDetails.text = schedule.serviceDetails
            scheduledTime.text = schedule.scheduledTime?.toDate().toString()
            partnerName.text = schedule.partnerName
            partnerRating.text = "4.57" // Hardcoded for now, can be dynamic
            partnerReviews.text = "• 218 Ratings"

            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:+1234567890") // Replace with real phone number
                it.context.startActivity(intent)
            }

            btnNeedHelp.setOnClickListener {
                Toast.makeText(it.context, "Need Help clicked!", Toast.LENGTH_SHORT).show()
            }
            btnViewProject.setOnClickListener {
                Toast.makeText(it.context, "View Project clicked!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(schedules[position])
    }

    override fun getItemCount(): Int = schedules.size

    // 🔥 Function to update the data dynamically
    fun updateData(newSchedules: List<Schedule>) {
        schedules = newSchedules
        notifyDataSetChanged()
    }
}
