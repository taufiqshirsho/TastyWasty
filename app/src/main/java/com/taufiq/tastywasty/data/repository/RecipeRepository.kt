package com.taufiq.tastywasty.data.repository

import com.taufiq.tastywasty.data.dao.RecipeDao
import com.taufiq.tastywasty.data.model.Recipe
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao: RecipeDao) {
    fun getAllRecipes(): Flow<List<Recipe>> = dao.getAll()

    //suspend fun insert(recipe: Recipe) = dao.insert(recipe)

    suspend fun delete(recipe: Recipe) {
        dao.delete(recipe)
    }

    suspend fun update(recipe: Recipe) {
        dao.update(recipe)
    }

}
