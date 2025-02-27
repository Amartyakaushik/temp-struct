package com.example.worka1.ui.show_category_details.components

data class Subcategory (
    val id: String = "",
    val name: String = "",
    val items: List<SubcategoryItem> = emptyList()
)