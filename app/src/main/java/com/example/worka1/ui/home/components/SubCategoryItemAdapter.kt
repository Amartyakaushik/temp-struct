package com.example.worka1.ui.home.components

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.worka1.R
import com.example.worka1.ui.show_category_details.ShowCategoryDetailsActivity
import com.example.worka1.utils.formatNumber

class SubCategoryItemAdapter(
    private val context: Context,
    private val subCategoryItemList: List<SubCategoryItem>
) : RecyclerView.Adapter<SubCategoryItemAdapter.SubCategoryItemViewHolder>() {

    class SubCategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.subcategory_item_image)
        val titleTextView: TextView = itemView.findViewById(R.id.subcategory_item_name)
        val ratingTextView: TextView = itemView.findViewById(R.id.subcategory_item_rating)
        val ratingsCountTextView: TextView = itemView.findViewById(R.id.subcategory_item_ratings_count)
        val priceTextView: TextView = itemView.findViewById(R.id.subcategory_item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subcategory_item, parent, false)
        return SubCategoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubCategoryItemViewHolder, position: Int) {
        val subCategoryItem = subCategoryItemList[position]
        holder.titleTextView.text = subCategoryItem.name
        Glide.with(holder.itemView.context)
            .load(subCategoryItem.imageResId)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_add_to_cart_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(holder.imageView)

        holder.ratingTextView.text = subCategoryItem.rating.toString()
        holder.ratingsCountTextView.text = "(${formatNumber(subCategoryItem.ratingsCount)})"
        holder.priceTextView.text = "₹${subCategoryItem.price}"
        val category_id = subCategoryItem.category_id
        val subcategory_id = subCategoryItem.subcategory_id
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ShowCategoryDetailsActivity::class.java)
            val subCategoriesDetails = HashMap<String, String>()
            subCategoriesDetails["category_id"] = category_id
            subCategoriesDetails["sub_category_id"] = subcategory_id
            subCategoriesDetails["item_id"] = subCategoryItem.id
            intent.putExtra("sub_categories_details", subCategoriesDetails)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = subCategoryItemList.size
}
