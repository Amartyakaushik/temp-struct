package com.example.worka1.ui.cart

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.worka1.R
import com.example.worka1.ui.cart.components.CartItem
import com.example.worka1.ui.cart.components.ServiceItemAdapter
import com.example.worka1.ui.checkout.CheckoutActivity
import com.example.worka1.ui.show_category_details.ShowCategoryDetailsActivity

class CartAdapter(private val context: CartActivity, private val cartItems: MutableList<CartItem>, private val user_id: String="DH8j7CdzJHioSBFlrPav") :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val service_name: TextView = view.findViewById(R.id.services_name)
        val service_total: TextView = view.findViewById(R.id.services_total)
        val delete_button: ImageView = view.findViewById(R.id.delete_button)
        val services_image: ImageView = view.findViewById(R.id.services_image)
        val services_list_container: RecyclerView = view.findViewById(R.id.services_list_container)
        val add_service_button: TextView = view.findViewById(R.id.add_service_button)
        val checkout_button: TextView = view.findViewById(R.id.checkout_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.service_name.text = cartItem.service_name
        holder.service_total.text = "${cartItem.service_items_count} service • ₹${cartItem.service_total_cost}"
        Glide.with(holder.itemView.context)
            .load(cartItem.service_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.services_image)

        holder.services_list_container.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        val adapter = ServiceItemAdapter(cartItem.service_items_list)
        holder.services_list_container.adapter = adapter

        holder.add_service_button.setOnClickListener {
            val intent = Intent(context, ShowCategoryDetailsActivity::class.java)
            val subCategoriesDetails = HashMap<String, String>()
            subCategoriesDetails["category_id"] = cartItem.category_id
            subCategoriesDetails["sub_category_id"] = cartItem.service_id
            subCategoriesDetails["item_id"] = ""
            intent.putExtra("sub_categories_details", subCategoriesDetails)
            context.startActivity(intent)
        }

        holder.checkout_button.setOnClickListener {
            val intent = Intent(context, CheckoutActivity::class.java)
            intent.putExtra("user_id", user_id)
            intent.putExtra("service_id", cartItem.service_id)
            context.startActivity(intent)
        }

        holder.delete_button.setOnClickListener {
            context.removeFromFirebase("-1", cartItem.service_id, user_id)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)

            if (cartItems.isEmpty()) {
                (holder.itemView.context as? CartActivity)?.showEmptyCartView()
            }
        }

    }

    override fun getItemCount(): Int = cartItems.size
}
