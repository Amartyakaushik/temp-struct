package com.example.worka1.ui.cart.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.databinding.CartItemServiceBinding

class ServiceItemAdapter(
    private val serviceItemsList: MutableList<ServiceItem>
) : RecyclerView.Adapter<ServiceItemAdapter.ServiceItemViewHolder>() {

    inner class ServiceItemViewHolder(private val binding: CartItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(serviceItem: ServiceItem) {
            binding.serviceName.text = serviceItem.service_name
            binding.serviceCount.text = " X ${serviceItem.items_count}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        val binding = CartItemServiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ServiceItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        holder.bind(serviceItemsList[position])
    }

    override fun getItemCount(): Int = serviceItemsList.size
}
