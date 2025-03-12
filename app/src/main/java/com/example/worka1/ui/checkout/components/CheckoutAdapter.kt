package com.example.worka1.ui.checkout.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.checkout.CheckoutActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CheckoutAdapter(
    private val context: CheckoutActivity,
    private val checkoutItems: MutableList<CheckoutItems>,
    private val userId: String,
    private val serviceId: String,
    private val updateTotalCallback: () -> Unit
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    private val db = Firebase.firestore

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceTitle: TextView = view.findViewById(R.id.service_title)
        val servicePrice: TextView = view.findViewById(R.id.service_price)
        val tvCount: TextView = view.findViewById(R.id.tv_count)
        val btnIncrease: ImageButton = view.findViewById(R.id.btn_increase)
        val btnDecrease: ImageButton = view.findViewById(R.id.btn_decrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkout_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkoutItem = checkoutItems[position]
        holder.serviceTitle.text = checkoutItem.name
        holder.servicePrice.text = "₹${checkoutItem.price * checkoutItem.count}"
        holder.tvCount.text = checkoutItem.count.toString()

        holder.btnIncrease.setOnClickListener {
            checkoutItem.count++
            updateItemInFirestore(checkoutItem)
            notifyItemChanged(position)
            updateTotalCallback()
        }

        holder.btnDecrease.setOnClickListener {
            if (checkoutItem.count > 1) {
                checkoutItem.count--
                updateItemInFirestore(checkoutItem)
                notifyItemChanged(position)
                updateTotalCallback()
            } else {
                removeItemFromFirestore(checkoutItem)
                checkoutItems.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, checkoutItems.size)
                updateTotalCallback()
            }
        }
    }

    override fun getItemCount(): Int = checkoutItems.size

    private fun updateItemInFirestore(checkoutItem: CheckoutItems) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRef = db.collection("users").document(userId)
                    .collection("cart").document("current_cart")
                cartRef.update("$serviceId.${checkoutItem.id}.items_count", checkoutItem.count).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun removeItemFromFirestore(checkoutItem: CheckoutItems) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRef = db.collection("users").document(userId)
                    .collection("cart").document("current_cart")
                cartRef.update("$serviceId.${checkoutItem.id}", FieldValue.delete()).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
