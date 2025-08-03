package com.taufiq.tastywasty.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.taufiq.tastywasty.ui.navigation.BottomNavigationBar
import com.taufiq.tastywasty.ui.navigation.BottomNavItem
import com.taufiq.tastywasty.ui.screens.home.HomeScreen
import com.taufiq.tastywasty.ui.screens.inventory.InventoryScreen
import com.taufiq.tastywasty.ui.screens.recipe.RecipeScreen
import com.taufiq.tastywasty.ui.screens.setting.SettingsScreen
import com.taufiq.tastywasty.ui.screens.shopping.ShoppingScreen
import com.taufiq.tastywasty.viewModel.FoodViewModel
import com.taufiq.tastywasty.viewModel.RecipeViewModel
import com.taufiq.tastywasty.viewModel.ShoppingViewModel
import com.taufiq.tastywasty.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    foodViewModel: FoodViewModel,
    recipeViewModel: RecipeViewModel,
    shoppingViewModel: ShoppingViewModel,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit

) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomNavItem.Inventory.route) {
                InventoryScreen(foodViewModel)
            }
            composable(BottomNavItem.Recipes.route) {
                RecipeScreen(
                    viewModel = recipeViewModel,
                    inventoryViewModel = foodViewModel,
                    shoppingViewModel = shoppingViewModel
                )
            }

            composable("shopping") {
                ShoppingScreen(viewModel = shoppingViewModel)
            }

            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    authViewModel = authViewModel,
                    onLogout = onLogout
                )
            }

            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    authViewModel = authViewModel,
                    foodViewModel = foodViewModel,
                    shoppingViewModel = shoppingViewModel,
                    recipeViewModel = recipeViewModel,
                    onNavigateToShopping = { navController.navigate("shopping") },
                    onNavigateToInventory = { navController.navigate(BottomNavItem.Inventory.route) },
                    onNavigateToRecipes = { navController.navigate(BottomNavItem.Recipes.route) }
                )
            }



        }
    }
}
