package com.example.worka1.ui.home.components

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class ServiceCategories(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("image") @set:PropertyName("image") var image: String = "",
) : Serializable {
    constructor() : this("", "", "")
}
