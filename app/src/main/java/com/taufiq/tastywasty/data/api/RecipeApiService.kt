package com.taufiq.tastywasty.data.api

import retrofit2.Response
import retrofit2.http.GET

interface RecipeApiService {
    @GET("random.php")
    suspend fun getRandomRecipe(): Response<RandomRecipeResponse>
}
