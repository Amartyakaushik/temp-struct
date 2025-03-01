package com.example.worka1.ui.show_category_details.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R


class SubcategoryAdapter(
    private val subCategoriesList: List<Subcategory>,
    private val userId: String,
    private val categoryId: String
) : RecyclerView.Adapter<SubcategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subcategory_name_container: TextView = view.findViewById(R.id.subcategory_name)
        val items_container: RecyclerView = view.findViewById(R.id.items_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.subcategory_container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subcategory = subCategoriesList[position]
        holder.subcategory_name_container.text = subcategory.name
        if (holder.items_container.layoutManager == null) {
            holder.items_container.layoutManager = LinearLayoutManager(
                holder.itemView.context, LinearLayoutManager.VERTICAL, false
            )
        }
        val adapter = SubcategoryItemAdapter(subcategory.items, subcategory.id, userId, categoryId)
        holder.items_container.adapter = adapter
    }

    override fun getItemCount(): Int = subCategoriesList.size
}