package com.example.worka1.ui.account.helpandsupport.help


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class MembershipAdapter(
    private val name: List<String>,
    private val navController: NavController
) : RecyclerView.Adapter<MembershipAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.items_name)
        val icon: ImageView = view.findViewById(R.id.ic_all_next)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = name[position]
        holder.itemView.setOnClickListener {
            when (position) {
                0 -> navController.navigate(R.id.navigation_membership)
                1 -> navController.navigate(R.id.navigation_membership)
            }
        }
    }

    override fun getItemCount(): Int = name.size
}
