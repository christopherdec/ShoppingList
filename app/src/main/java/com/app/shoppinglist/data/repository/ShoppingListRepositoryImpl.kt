package com.app.shoppinglist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.app.shoppinglist.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

class ShoppingListRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ShoppingListRepository {

    companion object {
        private val SHOPPING_LIST_KEY = stringPreferencesKey("shopping_list")
    }

    override fun getItems(): Flow<List<ShoppingItem>> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val jsonString = preferences[SHOPPING_LIST_KEY] ?: "[]"
                try {
                    Json.decodeFromString<List<ShoppingItem>>(jsonString)
                } catch (_: Exception) {
                    emptyList()
                }
            }
    }

    override suspend fun saveItems(items: List<ShoppingItem>) {
        dataStore.edit { preferences ->
            val jsonString = Json.encodeToString(items)
            preferences[SHOPPING_LIST_KEY] = jsonString
        }
    }

    override suspend fun clearItems() {
        dataStore.edit { preferences ->
            preferences.remove(SHOPPING_LIST_KEY)
        }
    }
}
