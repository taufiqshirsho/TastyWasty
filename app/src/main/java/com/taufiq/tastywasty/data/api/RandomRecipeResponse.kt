package com.taufiq.tastywasty.data.api


data class RandomRecipeResponse(
    val meals: List<Meal>
)

data class Meal(
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String
)
