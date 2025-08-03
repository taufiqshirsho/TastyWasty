package com.taufiq.tastywasty

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taufiq.tastywasty.data.db.AppDatabase
import com.taufiq.tastywasty.data.repository.FoodRepository
import com.taufiq.tastywasty.data.repository.RecipeRepository
import com.taufiq.tastywasty.data.repository.ShoppingRepository
import com.taufiq.tastywasty.data.repository.UserRepository
import com.taufiq.tastywasty.ui.screens.MainScreen
import com.taufiq.tastywasty.ui.screens.auth.LoginScreen
import com.taufiq.tastywasty.ui.screens.auth.SignupScreen
import com.taufiq.tastywasty.ui.theme.SaveFoodTheme
import com.taufiq.tastywasty.viewModel.FoodViewModel
import com.taufiq.tastywasty.viewModel.FoodViewModelFactory
import com.taufiq.tastywasty.viewModel.RecipeViewModel
import com.taufiq.tastywasty.viewModel.RecipeViewModelFactory
import com.taufiq.tastywasty.viewModel.ShoppingViewModel
import com.taufiq.tastywasty.viewmodel.AuthViewModel
import com.taufiq.tastywasty.viewmodel.AuthViewModelFactory
import com.taufiq.tastywasty.viewmodel.ShoppingViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())
        val foodRepository = FoodRepository(db.foodDao())

        val authViewModel = AuthViewModelFactory(userRepository)
            .create(AuthViewModel::class.java)
        val foodViewModel = FoodViewModelFactory(foodRepository)
            .create(FoodViewModel::class.java)

        val recipeRepo = RecipeRepository(db.recipeDao())
        val recipeViewModel = RecipeViewModelFactory(recipeRepo, foodViewModel)
            .create(RecipeViewModel::class.java)

        val shoppingViewModel = ShoppingViewModelFactory(ShoppingRepository(db.shoppingDao()))
            .create(ShoppingViewModel::class.java)



        setContent {
            SaveFoodTheme {
                val navController = rememberNavController()
                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = { navController.navigate("main") },
                            onNavigateToSignup = { navController.navigate("signup") }
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            viewModel = authViewModel,
                            onSignupSuccess = { navController.navigate("main") },
                            onNavigateToLogin = { navController.navigate("login") }
                        )
                    }
                    composable("main") {
                        MainScreen(
                            foodViewModel = foodViewModel,
                            recipeViewModel = recipeViewModel,
                            shoppingViewModel = shoppingViewModel,
                            authViewModel=authViewModel,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }

                        )
                    }
                }
            }
        }
    }
}
