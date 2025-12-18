package com.app.shoppinglist.data.model

import com.app.shoppinglist.data.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ShoppingItem(
    val name: String,
    val onCart: Boolean = false,
    @Serializable(with = DateSerializer::class)
    val date: Date = Date()
)
