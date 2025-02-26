package com.example.worka1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worka1.ui.home.components.HomeService
import com.example.worka1.ui.home.components.ServiceCategoriesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ServicesAdapter(private val servicesList: List<HomeService>) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.home_service_image)
        val textView: TextView = view.findViewById(R.id.home_service_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_services_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = servicesList[position]
        Glide.with(holder.itemView.context)
            .load(service.image)
            .into(holder.imageView)
        holder.textView.text = service.name

        holder.itemView.setOnClickListener {
            showBottomSheetDialog(holder.itemView.context, service)
        }
    }

    override fun getItemCount(): Int = servicesList.size

    private fun showBottomSheetDialog(context: Context, service: HomeService) {
        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.service_details_bottom_sheet, null)
        dialog.setContentView(view)

        val serviceImage = view.findViewById<ImageView>(R.id.bottom_sheet_service_image)
        val serviceTitle = view.findViewById<TextView>(R.id.bottom_sheet_service_title)
        val serviceDescription = view.findViewById<TextView>(R.id.bottom_sheet_service_description)
        val serviceSubCategories = view.findViewById<RecyclerView>(R.id.services_category_container)
        val layoutManager = GridLayoutManager(context, 3)
        serviceSubCategories.layoutManager = layoutManager
        serviceSubCategories.adapter = ServiceCategoriesAdapter(service.id, service.subCategories)
        Glide.with(view.context)
            .load(service.image)
            .into(serviceImage)
        serviceTitle.text = service.name
        serviceDescription.text = service.description
        dialog.show()
    }
}
