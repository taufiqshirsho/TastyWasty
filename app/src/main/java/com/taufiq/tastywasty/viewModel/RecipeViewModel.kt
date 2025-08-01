package com.taufiq.tastywasty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufiq.tastywasty.data.model.Recipe
import com.taufiq.tastywasty.data.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repo: RecipeRepository,
    private val foodViewModel: FoodViewModel // for expiring ingredients
) : ViewModel() {

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repo.insert(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repo.delete(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repo.update(recipe)
        }
    }

    val allRecipes = repo.getAllRecipes().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )


    val expiringIngredients = foodViewModel.expiringSoon
        .map { list -> list.map { it.name.lowercase() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val matchedRecipes = combine(allRecipes, expiringIngredients) { recipes, ingredients ->
        recipes.filter { recipe ->
            ingredients.any { recipe.ingredients.joinToString().contains(it, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
