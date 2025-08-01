package com.taufiq.tastywasty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufiq.tastywasty.data.model.FoodItem
import com.taufiq.tastywasty.data.model.Recipe
import com.taufiq.tastywasty.data.model.ShoppingList
import com.taufiq.tastywasty.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repo: ShoppingRepository) : ViewModel() {
    val shoppingLists = repo.getAllLists().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addList(name: String, items: String) {
        viewModelScope.launch {
            repo.insertList(ShoppingList(name = name, items = items))
        }
    }

    fun updateList(list: ShoppingList) {
        viewModelScope.launch {
            repo.updateList(list)
        }
    }

    fun deleteList(list: ShoppingList) {
        viewModelScope.launch {
            repo.deleteList(list)
        }
    }

    fun toggleDone(list: ShoppingList) {
        viewModelScope.launch {
            repo.updateList(list.copy(done = !list.done))
        }
    }

    fun generateShoppingListFromRecipe(recipe: Recipe, inventory: List<FoodItem>) {
        viewModelScope.launch {
            val validStock = inventory
                .filter { it.expiryDate > System.currentTimeMillis() }
                .map { it.name.trim().lowercase() }

            val missingIngredients = recipe.ingredients.filterNot {
                validStock.contains(it.trim().lowercase())
            }

            if (missingIngredients.isNotEmpty()) {
                val list = ShoppingList(
                    name = recipe.title,
                    items = missingIngredients.joinToString(", "),
                    done = false
                )
                repo.insertList(list)
            }
        }
    }

}
