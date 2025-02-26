package com.example.worka1.ui.account
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R

class AccountAdapter(
    private val image: List<Int>,
    private val name: List<String>,
    private val navController: NavController
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image1: ImageView = view.findViewById(R.id.ic_image1)
        val name: TextView = view.findViewById(R.id.items_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.getting_start_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image1.setImageResource(image[position])
        holder.name.text = name[position]


        holder.itemView.setOnClickListener {
            when (position) {
                0 -> navController.navigate(R.id.navigation_account_help)
                1 -> navController.navigate(R.id.navigation_getting_start)
                2 -> navController.navigate(R.id.navigation_payment_credits)
                3 -> navController.navigate(R.id.navigation_membership)
                4 -> navController.navigate(R.id.navigation_safety)
                5 -> navController.navigate(R.id.navigation_claim_warranty)
            }
        }
    }
    override fun getItemCount(): Int = name.size
}

