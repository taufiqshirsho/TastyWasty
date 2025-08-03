package com.taufiq.tastywasty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufiq.tastywasty.data.api.Meal
import com.taufiq.tastywasty.data.api.RecipeApiService
import com.taufiq.tastywasty.data.api.RandomRecipeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

class OnlineRecipeViewModel : ViewModel() {

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal = _meal.asStateFlow()

    private val api: RecipeApiService = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApiService::class.java)

    fun fetchRandomRecipe() {
        viewModelScope.launch {
            try {
                val response: Response<RandomRecipeResponse> = api.getRandomRecipe()

                if (response.isSuccessful) {
                    val randomMeal = response.body()?.meals?.firstOrNull()
                    _meal.value = randomMeal
                } else {
                    println("❌ API Error: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                println("❌ Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
