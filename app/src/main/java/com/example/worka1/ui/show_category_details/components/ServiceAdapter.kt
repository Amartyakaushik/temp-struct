package com.example.worka1.ui.show_category_details.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worka1.R
import com.example.worka1.ui.home.components.HomeService
import com.example.worka1.ui.home.components.ServiceCategories

class ServiceAdapter(
    private val servicesList: List<ServiceCategories>,
    private val listener: OnServiceClickListener
) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.home_service_image)
        val textView: TextView = view.findViewById(R.id.home_service_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.subcategory_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = servicesList[position]
        Glide.with(holder.itemView.context)
            .load(service.image)
            .into(holder.imageView)
        holder.textView.text = service.name

        holder.itemView.setOnClickListener {
            listener.onServiceClick(service.id)
        }
    }

    override fun getItemCount(): Int = servicesList.size
}