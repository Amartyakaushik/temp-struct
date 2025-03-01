package com.example.worka1.ui.cart.components

class CartItem (
    val category_id: String = "",
    val service_id: String = "",
    val service_name: String = "",
    val service_image: String = "",
    val service_items_count: Int = 0,
    val service_total_cost: Int = 0,
    val service_items_list: MutableList<ServiceItem> = mutableListOf()
)