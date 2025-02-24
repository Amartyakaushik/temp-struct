package com.example.worka1.ui.home.components

data class SubCategoryItem(
    val id: String,
    val category_id: String,
    val subcategory_id: String,
    val name: String,
    val imageResId: Int,
    val rating: Float,
    val ratingsCount: Int,
    val price: String
)
