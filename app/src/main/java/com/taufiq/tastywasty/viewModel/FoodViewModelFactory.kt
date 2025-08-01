package com.taufiq.tastywasty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taufiq.tastywasty.data.repository.FoodRepository

class FoodViewModelFactory(private val repo: FoodRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            return FoodViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
