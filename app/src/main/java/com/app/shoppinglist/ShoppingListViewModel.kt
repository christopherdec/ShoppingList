package com.app.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.shoppinglist.data.model.ShoppingItem
import com.app.shoppinglist.data.repository.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val repository: ShoppingListRepository
) : ViewModel() {

    private val _itemList = MutableStateFlow<List<ShoppingItem>>(listOf())

    val itemList: StateFlow<List<ShoppingItem>> = _itemList

    init {
        viewModelScope.launch {
            repository.getItems().collect { items ->
                _itemList.value = items
            }
        }
    }

    fun add(item: ShoppingItem) {
        viewModelScope.launch {
            _itemList.update {
                it + item
            }
            repository.saveItems(_itemList.value)
        }
    }

    fun remove(item: ShoppingItem) {
        viewModelScope.launch {
            _itemList.update {
                it.filter { i -> i !== item }
            }
            repository.saveItems(_itemList.value)
        }
    }

    fun rename(item: ShoppingItem, name: String) {
        viewModelScope.launch {
            _itemList.update {
                it.map { i ->
                    if (i == item) {
                        i.copy(name = name)
                    } else {
                        i
                    }
                }
            }
            repository.saveItems(_itemList.value)
        }
    }

    fun toggleOnCart(item: ShoppingItem) {
        viewModelScope.launch {
            _itemList.update {
                it.map { i ->
                    if (i == item) {
                        i.copy(onCart = !i.onCart)
                    } else {
                        i
                    }
                }
            }
            repository.saveItems(_itemList.value)
        }
    }
}