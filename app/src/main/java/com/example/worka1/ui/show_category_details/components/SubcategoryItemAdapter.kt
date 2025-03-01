package com.example.worka1.ui.show_category_details.components

import CartViewModel
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.worka1.ui.show_category_details.ShowCategoryDetailsActivity

class SubcategoryItemAdapter(
    private val items: List<SubcategoryItem>,
    private val subcategoryId: String,
    private val userId: String,
    private val categoryId: String,
    private val cartViewModel: CartViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<SubcategoryItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceTitle: TextView = view.findViewById(R.id.service_title)
        val serviceRating: TextView = view.findViewById(R.id.service_rating)
        val servicePrice: TextView = view.findViewById(R.id.service_price)
        val serviceDuration: TextView = view.findViewById(R.id.service_duration)
        val serviceDescription: TextView = view.findViewById(R.id.service_description)
        val textViewDetails: TextView = view.findViewById(R.id.text_view_details)
        val addButtonNormal: Button = view.findViewById(R.id.add_button_normal)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val specialButton: LinearLayout = view.findViewById(R.id.special_button)
        val btnIncrease: ImageButton = view.findViewById(R.id.btn_increase)
        val btnDecrease: ImageButton = view.findViewById(R.id.btn_decrease)
        val tvCount: TextView = view.findViewById(R.id.tv_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.subcategory_service_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.serviceTitle.text = item.itemName
        holder.serviceRating.text = "★ ${item.ratingCount} (${formatNumber(item.reviewsCount)} reviews)"
        holder.servicePrice.text = "₹${item.price}"
        holder.serviceDuration.text = item.duration
        holder.serviceDescription.apply {
            text = item.description
            visibility = if (item.description.isNotEmpty()) View.VISIBLE else View.GONE
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

        cartViewModel.fetchItemCount(userId, subcategoryId, item.id)

        cartViewModel.itemCount.observe(lifecycleOwner, Observer { countMap ->
            val count = countMap[item.id] ?: 0
            updateUI(holder, item, count)
        })

        holder.addButtonNormal.setOnClickListener {
            val newCount = 1
            updateUI(holder, item, newCount)
            handleItems(item.id, item.itemName, newCount)
        }

        holder.btnIncrease.setOnClickListener {
            val newCount = (holder.tvCount.text.toString().toIntOrNull() ?: 0) + 1
            updateUI(holder, item, newCount)
            handleItems(item.id, item.itemName, newCount)
        }

        holder.btnDecrease.setOnClickListener {
            val newCount = (holder.tvCount.text.toString().toIntOrNull() ?: 1) - 1
            if (newCount > 0) {
                updateUI(holder, item, newCount)
                handleItems(item.id, item.itemName, newCount)
            } else {
                holder.specialButton.visibility = View.GONE
                holder.addButtonNormal.visibility = View.VISIBLE
                CartActivity().removeFromFirebase(item.id, subcategoryId, userId)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    private fun updateUI(holder: ViewHolder, item: SubcategoryItem, count: Int) {
        if (count > 0) {
            holder.tvCount.text = count.toString()
            holder.specialButton.visibility = View.VISIBLE
            holder.addButtonNormal.visibility = View.GONE
        } else {
            holder.specialButton.visibility = View.GONE
            holder.addButtonNormal.visibility = View.VISIBLE
        }
        cartViewModel.fetchCartTotal(userId)
    }

    private fun handleItems(id: String, itemName: String, count: Int) {
        CartActivity().addToFirebase(
            subcategoryId,
            mutableListOf(ServiceItem(id, itemName, count)),
            userId,
            categoryId
        )
    }
}
