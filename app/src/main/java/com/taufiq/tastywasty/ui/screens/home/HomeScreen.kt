//package com.taufiq.tastywasty.ui.screens.home
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.taufiq.tastywasty.viewModel.FoodViewModel
//import com.taufiq.tastywasty.viewModel.RecipeViewModel
//import com.taufiq.tastywasty.viewModel.ShoppingViewModel
//import com.taufiq.tastywasty.viewmodel.AuthViewModel
//import java.time.LocalDate
//import java.time.ZoneId
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun HomeScreen(
//    authViewModel: AuthViewModel,
//    foodViewModel: FoodViewModel,
//    shoppingViewModel: ShoppingViewModel,
//    recipeViewModel: RecipeViewModel,
//    onNavigateToShopping: () -> Unit
//) {
//    val user by authViewModel.currentUser.collectAsStateWithLifecycle()
//    val allItems by foodViewModel.allItems.collectAsStateWithLifecycle()
//    val expiringSoon by foodViewModel.expiringSoon.collectAsStateWithLifecycle()
//    val shoppingLists by shoppingViewModel.shoppingLists.collectAsStateWithLifecycle()
//    val allRecipes by recipeViewModel.allRecipes.collectAsStateWithLifecycle()
//
//    val today = remember {
//        val now = LocalDate.now()
//        val startOfDay = now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
//        val endOfDay = now.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
//        startOfDay to endOfDay
//    }
//
//    val expiringToday = allItems.filter {
//        it.expiryDate in today.first..today.second
//    }
//
//    val suggestedRecipe = allRecipes.firstOrNull { recipe ->
//        recipe.ingredients.any { ingredient ->
//            expiringSoon.any { item -> item.name.contains(ingredient, ignoreCase = true) }
//        }
//    }
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        Text(
//            text = "Welcome back, ${user?.email ?: "Guest"} 👋",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Quick Stats
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            StatCard("🍎", "Inventory", allItems.size)
//            StatCard("⏳", "Expiring Soon", expiringSoon.size)
//        }
//        Spacer(Modifier.height(8.dp))
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            StatCard("🛒", "Pending Lists", shoppingLists.count { !it.done })
//            StatCard("🍽️", "Recipes", allRecipes.size)
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // Highlights
//        Text("🔥 Expiring Today", style = MaterialTheme.typography.titleMedium)
//        if (expiringToday.isEmpty()) {
//            Text("Nothing expiring today", color = MaterialTheme.colorScheme.onSurfaceVariant)
//        } else {
//            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                expiringToday.take(3).forEach {
//                    Text("• ${it.name} (${it.quantity})", style = MaterialTheme.typography.bodyMedium)
//                }
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        Text("📌 Suggested Recipe", style = MaterialTheme.typography.titleMedium)
//        if (suggestedRecipe != null) {
//            Text("Try: ${suggestedRecipe.title}", style = MaterialTheme.typography.bodyLarge)
//        } else {
//            Text("No suggestions at the moment", color = MaterialTheme.colorScheme.onSurfaceVariant)
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // Action Button
//        Button(onClick = onNavigateToShopping, modifier = Modifier.align(Alignment.End)) {
//            Text("🛍️ Go to Shopping List")
//        }
//    }
//}
//
//@Composable
//fun StatCard(icon: String, label: String, value: Int) {
//    Card(
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            Text(icon, style = MaterialTheme.typography.headlineSmall)
//            Text(label, style = MaterialTheme.typography.labelMedium)
//            Text(value.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
//        }
//    }
//}

package com.taufiq.tastywasty.ui.screens.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taufiq.tastywasty.viewModel.FoodViewModel
import com.taufiq.tastywasty.viewModel.RecipeViewModel
import com.taufiq.tastywasty.viewModel.ShoppingViewModel
import com.taufiq.tastywasty.viewmodel.AuthViewModel
import java.time.LocalDate
import java.time.ZoneId

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    foodViewModel: FoodViewModel,
    shoppingViewModel: ShoppingViewModel,
    recipeViewModel: RecipeViewModel,
    onNavigateToShopping: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToRecipes: () -> Unit
) {
    val user by authViewModel.currentUser.collectAsStateWithLifecycle()
    val allItems by foodViewModel.allItems.collectAsStateWithLifecycle()
    val expiringSoon by foodViewModel.expiringSoon.collectAsStateWithLifecycle()
    val shoppingLists by shoppingViewModel.shoppingLists.collectAsStateWithLifecycle()
    val allRecipes by recipeViewModel.allRecipes.collectAsStateWithLifecycle()

    val today = remember {
        val now = LocalDate.now()
        val startOfDay = now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endOfDay = now.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        startOfDay to endOfDay
    }

    val expiringToday = allItems.filter {
        it.expiryDate in today.first..today.second
    }

    val suggestedRecipe = remember(allRecipes, expiringSoon) {
        allRecipes.firstOrNull { recipe ->
            recipe.ingredients.any { ingredient ->
                expiringSoon.any { item -> item.name.contains(ingredient, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user?.email?.split("@")?.firstOrNull() ?: "Guest",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Quick Stats Cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                icon = Icons.Default.Kitchen,
                label = "Inventory",
                value = allItems.size,
                onClick = onNavigateToInventory
            )
            StatCard(
                icon = Icons.Default.Warning,
                label = "Expiring Soon",
                value = expiringSoon.size,
                onClick = onNavigateToInventory
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                icon = Icons.Default.ShoppingCart,
                label = "Shopping Lists",
                value = shoppingLists.count { !it.done },
                onClick = onNavigateToShopping
            )
            StatCard(
                icon = Icons.Default.MenuBook,
                label = "Recipes",
                value = allRecipes.size,
                onClick = onNavigateToRecipes
            )
        }

        // Highlights Section
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Today's Overview",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Expiring Today
        HighlightCard(
            title = "Expiring Today",
            icon = Icons.Default.Schedule,
            isEmpty = expiringToday.isEmpty(),
            emptyText = "Nothing expiring today",
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                expiringToday.take(3).forEach { item ->
                    FoodItemRow(
                        name = item.name,
                        quantity = item.quantity,
                        expiryDate = item.expiryDate
                    )
                }
                if (expiringToday.size > 3) {
                    Text(
                        text = "+${expiringToday.size - 3} more...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HighlightCard(
            title = "Suggested Recipe",
            icon = Icons.Default.LocalDining,
            isEmpty = suggestedRecipe == null,
            emptyText = "No suggestions available",
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestedRecipe?.let { recipe ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Uses: ${recipe.ingredients.take(3).joinToString()}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    label: String,
    value: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
//            .fontW(1f)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HighlightCard(
    title: String,
    icon: ImageVector,
    isEmpty: Boolean,
    emptyText: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (isEmpty) {
                Text(
                    text = emptyText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                content()
            }
        }
    }
}

@Composable
fun FoodItemRow(name: String, quantity: String, expiryDate: Long) {
    val daysLeft = remember(expiryDate) {
        ((expiryDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    color = when {
                        daysLeft < 0 -> MaterialTheme.colorScheme.error
                        daysLeft < 3 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$name ($quantity)",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = when {
                daysLeft < 0 -> "Expired"
                daysLeft == 0 -> "Today"
                daysLeft == 1 -> "Tomorrow"
                else -> "$daysLeft days"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}