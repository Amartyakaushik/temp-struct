package com.example.worka1.ui.checkout.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.worka1.R
import com.example.worka1.ui.checkout.CheckoutActivity
import com.google.firebase.firestore.FirebaseFirestore

class FATAdapter(
    private val checkoutActivity: CheckoutActivity,
    private val fatItems: MutableList<FATItem>,
    private val serviceId: String,
    private val updateTotalCallback: () -> Unit
) : RecyclerView.Adapter<FATAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceTitle: TextView = view.findViewById(R.id.service_title)
        val servicePrice: TextView = view.findViewById(R.id.service_price)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val addButton: TextView = view.findViewById(R.id.add_button_normal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkout_fat_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkoutItem = fatItems[position]
        holder.serviceTitle.text = checkoutItem.name
        holder.servicePrice.text = "₹${checkoutItem.price}"

        Glide.with(holder.itemView.context)
            .load(checkoutItem.image_url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)

        holder.addButton.setOnClickListener {
            val existingItem = checkoutActivity.checkoutItems.find { it.id == checkoutItem.id }

            if (existingItem != null) {
                existingItem.count++
                updateItemInFirestore(existingItem)
            } else {
                val newItem = CheckoutItems(
                    id = checkoutItem.id,
                    name = checkoutItem.name,
                    price = checkoutItem.price,
                    count = 1
                )
                checkoutActivity.checkoutItems.add(newItem)
                addItemToFirestore(newItem)
            }

            fatItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, fatItems.size)
            checkoutActivity.recyclerViewItems.adapter?.notifyDataSetChanged()
            updateTotalCallback()
        }
    }

    override fun getItemCount(): Int = fatItems.size

    private fun addItemToFirestore(item: CheckoutItems) {
        val cartRef = FirebaseFirestore.getInstance().collection("users")
            .document(checkoutActivity.userId)
            .collection("cart").document("current_cart")

        val itemPath = "$serviceId.${item.id}"
        val itemData = mapOf(
            "id" to item.id,
            "service_name" to item.name,
            "items_count" to item.count,
            "price" to item.price
        )

        cartRef.update(itemPath, itemData).addOnSuccessListener {
            checkoutActivity.recyclerViewItems.adapter?.notifyDataSetChanged()
        }
    }

    private fun updateItemInFirestore(item: CheckoutItems) {
        val cartRef = FirebaseFirestore.getInstance().collection("users")
            .document(checkoutActivity.userId)
            .collection("cart").document("current_cart")

        val itemPath = "$serviceId.${item.id}.items_count"

        cartRef.update(itemPath, item.count).addOnSuccessListener {
            checkoutActivity.recyclerViewItems.adapter?.notifyDataSetChanged()
        }
    }
}