package com.example.worka1.ui.checkout.components

data class CheckoutItems (
    val id: String = "",
    val name: String = "",
    var count: Int = 0,
    val price: Int = 0,
    val category_id: String = "",
    val subcategory_id: String = ""
)