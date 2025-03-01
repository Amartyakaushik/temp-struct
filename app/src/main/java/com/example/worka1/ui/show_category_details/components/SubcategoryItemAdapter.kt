package com.example.worka1.ui.show_category_details.components

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.worka1.R
import com.example.worka1.ui.cart.CartActivity
import com.example.worka1.ui.cart.components.ServiceItem
import com.example.worka1.utils.formatNumber

class SubcategoryItemAdapter(
    private val items: List<SubcategoryItem>,
    private val subcategory_id: String,
    private val userId: String,
    private val categoryId: String
):
    RecyclerView.Adapter<SubcategoryItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceTitle: TextView = view.findViewById(R.id.service_title)
        val serviceRating: TextView = view.findViewById(R.id.service_rating)
        val servicePrice: TextView = view.findViewById(R.id.service_price)
        val serviceDuration: TextView = view.findViewById(R.id.service_duration)
        val serviceDescription: TextView = view.findViewById(R.id.service_description)
        val textViewDetails: TextView = view.findViewById(R.id.text_view_details)
        val addButtonNormal: Button = view.findViewById(R.id.add_button_normal)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val special_button: LinearLayout = view.findViewById(R.id.special_button)
        val btnIncrease: ImageButton = view.findViewById(R.id.btn_increase)
        val btnDecrease: ImageButton = view.findViewById(R.id.btn_decrease)
        val tvCount: TextView = view.findViewById(R.id.tv_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.subcategory_service_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.serviceTitle.text = item.itemName
        holder.serviceRating.text = "★ ${item.ratingCount} (${formatNumber(item.reviewsCount)} reviews)"
        holder.servicePrice.text = "₹${item.price}"
        holder.serviceDuration.text = item.duration
        if (item.description.isNotEmpty()){
            holder.serviceDescription.text = item.description
            holder.serviceDescription.visibility = View.VISIBLE
        } else {
            holder.serviceDescription.visibility = View.GONE
        }
        holder.textViewDetails.text = "View details"
        holder.addButtonNormal.text = "Add"
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_add_to_cart_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(holder.imageView)
        var count = 1

        holder.addButtonNormal.setOnClickListener {
            holder.addButtonNormal.visibility = View.GONE
            holder.special_button.visibility = View.VISIBLE
            holder.tvCount.text = count.toString()
            handleItems(item.id, item.itemName, count)
        }

        holder.btnIncrease.setOnClickListener {
            count++
            holder.tvCount.text = count.toString()
            handleItems(item.id, item.itemName, count)
        }

        holder.btnDecrease.setOnClickListener {
            if (count > 1) {
                count--
                holder.tvCount.text = count.toString()
                handleItems(item.id, item.itemName, count)
            } else {
                holder.special_button.visibility = View.GONE
                holder.addButtonNormal.visibility = View.VISIBLE
                count = 1
                Log.d("CartActivity", "Item ${item.id}")
                CartActivity().removeFromFirebase(item.id, subcategory_id, userId)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun handleItems(id: String, itemName:String, count: Int) {
        CartActivity()
            .addToFirebase(
                subcategory_id, mutableListOf(
                    ServiceItem(id, itemName, count),
                ),
                userId,
                categoryId
            )
    }
}