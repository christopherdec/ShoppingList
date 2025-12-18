package com.app.shoppinglist.data.repository

import com.app.shoppinglist.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getItems(): Flow<List<ShoppingItem>>
    suspend fun saveItems(items: List<ShoppingItem>)
    suspend fun clearItems()
}
