package com.example.worka1.ui.home.components

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ui.show_category_details.ShowCategoryDetailsActivity

class ServiceCategoriesAdapter(
    private val categoryId: String?,
    private val subCategories: List<ServiceCategories>?
) : RecyclerView.Adapter<ServiceCategoriesAdapter.ViewHolder>() {

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

        if (subCategory != null) {
            holder.imageView.setImageResource(subCategory.image)
            holder.textView.text = subCategory.name

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ShowCategoryDetailsActivity::class.java)
                val subCategoriesDetails = HashMap<String, String>()
                subCategoriesDetails["category_id"] = categoryId ?: ""
                subCategoriesDetails["sub_category_id"] = subCategory.id
                subCategoriesDetails["item_id"] = (-1).toString()
                intent.putExtra("sub_categories_details", subCategoriesDetails)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return subCategories?.size ?: 0
    }
}
