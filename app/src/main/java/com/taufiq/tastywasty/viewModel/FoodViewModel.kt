package com.taufiq.tastywasty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufiq.tastywasty.data.model.FoodItem
import com.taufiq.tastywasty.data.repository.FoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodViewModel(private val repo: FoodRepository) : ViewModel() {

    val allItems = repo.getAllItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val expiringSoon = repo.getExpiringSoonItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addFood(item: FoodItem) {
        viewModelScope.launch {
            repo.addItem(item)
        }
    }

    fun deleteFood(item: FoodItem) {
        viewModelScope.launch {
            repo.deleteItem(item)
        }
    }
}
