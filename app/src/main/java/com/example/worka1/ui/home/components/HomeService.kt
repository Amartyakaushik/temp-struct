package com.example.worka1.ui.home.components

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class HomeService(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("image") @set:PropertyName("image") var image: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("subCategories") @set:PropertyName("subCategories") var subCategories: List<ServiceCategories> = emptyList()
) : Serializable {
    constructor() : this("", "", "", "", emptyList())
}
