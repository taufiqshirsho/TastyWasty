package com.taufiq.tastywasty.data.api

data class RandomRecipeResponse(
    val meals: List<Meal>
)

data class Meal(
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?
) {
    val ingredients: List<String>
        get() = listOfNotNull(
            strIngredient1,
            strIngredient2,
            strIngredient3,
            strIngredient4,
            strIngredient5,
            strIngredient6,
            strIngredient7,
            strIngredient8,
            strIngredient9,
            strIngredient10
        ).filter { it.isNotBlank() }
}
