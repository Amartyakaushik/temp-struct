package com.example.worka1.ui.home.components

data class HomeService(
    val id: String,
    val name: String,
    val image: Int,
    val description: String?,
    val subCategories: List<ServiceCategories>?
)
