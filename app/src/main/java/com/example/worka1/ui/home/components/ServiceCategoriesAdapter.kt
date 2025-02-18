package com.example.worka1.ui.home.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class ServiceCategoriesAdapter(private val subCategories: List<ServiceCategories>?) :
    RecyclerView.Adapter<ServiceCategoriesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.home_service_image)
        val textView: TextView = view.findViewById(R.id.home_service_title)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_services_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCategory = subCategories?.get(position)
        holder.imageView.setImageResource(subCategory!!.image)
        holder.textView.text = subCategory.name
        holder.itemView.setOnClickListener {
            // Handle item click if needed
        }
    }

    override fun getItemCount(): Int {
        return subCategories?.size ?: 0
    }
}
