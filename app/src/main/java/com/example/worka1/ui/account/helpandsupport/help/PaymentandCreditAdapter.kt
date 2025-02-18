package com.example.worka1.ui.account.helpandsupport.help


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class PaymentandCreditAdapter(
    private val name: List<String>,
    private val navController: NavController
) : RecyclerView.Adapter<PaymentandCreditAdapter.ViewHolder>() {

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
                0 -> navController.navigate(R.id.navigation_payment_credits)
                1 -> navController.navigate(R.id.navigation_payment_credits)
                2 -> navController.navigate(R.id.navigation_payment_credits)
                3 -> navController.navigate(R.id.navigation_payment_credits)

            }
        }
    }

    override fun getItemCount(): Int = name.size
}
