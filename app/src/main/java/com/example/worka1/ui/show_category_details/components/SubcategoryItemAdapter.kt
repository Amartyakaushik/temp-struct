package com.example.worka1.ui.show_category_details.components

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worka1.R

class SubcategoryItemAdapter(private val items: List<SubcategoryItem>): RecyclerView.Adapter<SubcategoryItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceTitle: TextView = view.findViewById(R.id.service_title)
        val serviceRating: TextView = view.findViewById(R.id.service_rating)
        val servicePrice: TextView = view.findViewById(R.id.service_price)
        val serviceDuration: TextView = view.findViewById(R.id.service_duration)
        val serviceDescription: TextView = view.findViewById(R.id.service_description)
        val textViewDetails: TextView = view.findViewById(R.id.text_view_details)
        val addButtonNormal: Button = view.findViewById(R.id.add_button_normal)
        val imageView: ImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.subcategory_service_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Log.d("item", "${item.itemName}")
        holder.serviceTitle.text = item.itemName
        holder.serviceRating.text = "★ ${item.ratingCount} (${item.reviewsCount} reviews)"
        holder.servicePrice.text = item.price.toString()
        holder.serviceDuration.text = item.duration.toString()
        holder.serviceDescription.text = item.description
        holder.textViewDetails.text = "View details"
        holder.addButtonNormal.text = "Add"
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}