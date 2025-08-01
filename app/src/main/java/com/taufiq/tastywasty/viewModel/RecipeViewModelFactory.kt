package com.taufiq.tastywasty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taufiq.tastywasty.data.repository.RecipeRepository

class RecipeViewModelFactory(
    private val recipeRepo: RecipeRepository,
    private val foodViewModel: FoodViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(recipeRepo, foodViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
