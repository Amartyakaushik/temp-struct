package com.example.worka1.ui.account.helpandsupport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class HelpItemsAdapter(
    private val image: List<Int>,
    private val name: List<String>,

) : RecyclerView.Adapter<HelpItemsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image1: ImageView = view.findViewById(R.id.ic_all_topic)
        val name: TextView = view.findViewById(R.id.items_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_topics_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image1.setImageResource(image[position])
        holder.name.text = name[position]

    }

    override fun getItemCount(): Int = name.size
}
