package com.taufiq.tastywasty.ui.screens.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taufiq.tastywasty.data.model.FoodItem
import com.taufiq.tastywasty.data.model.Recipe
import com.taufiq.tastywasty.viewModel.FoodViewModel
import com.taufiq.tastywasty.viewModel.RecipeViewModel
import com.taufiq.tastywasty.viewModel.ShoppingViewModel
import kotlinx.coroutines.delay

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel,
    inventoryViewModel: FoodViewModel,
    shoppingViewModel: ShoppingViewModel
) {
    val matchedRecipes by viewModel.matchedRecipes.collectAsStateWithLifecycle()
    val allRecipes by viewModel.allRecipes.collectAsStateWithLifecycle()
    val inventory by inventoryViewModel.allItems.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var showAll by remember { mutableStateOf(false) }
    var recipeToEdit by remember { mutableStateOf<Recipe?>(null) }
    var showSuccessAlert by remember { mutableStateOf(false) }

    val recipesToShow = if (showAll) allRecipes else matchedRecipes

    // Dismiss success alert after 2 seconds
    if (showSuccessAlert) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSuccessAlert = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    recipeToEdit = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (showAll) "All Recipes" else "Recipes Using Expiring Items",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    FilterChip(
                        selected = !showAll,
                        onClick = { showAll = false },
                        label = { Text("Expiring Recipes") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    FilterChip(
                        selected = showAll,
                        onClick = { showAll = true },
                        label = { Text("All Recipes") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                if (recipesToShow.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showAll) "No recipes available. Add one with the + button!"
                            else "No matching recipes found.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(recipesToShow) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                inventory = inventory,
                                onEdit = {
                                    recipeToEdit = recipe
                                    showDialog = true
                                },
                                onDelete = { viewModel.deleteRecipe(recipe) },
                                onGenerateShoppingList = {
                                    shoppingViewModel.generateShoppingListFromRecipe(recipe, inventory)
                                    showSuccessAlert = true
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AddRecipeDialog(
                onDismiss = { showDialog = false },
                onAdd = {
                    if (recipeToEdit != null) {
                        viewModel.updateRecipe(it.copy(id = recipeToEdit!!.id))
                    } else {
                        viewModel.addRecipe(it)
                    }
                    showDialog = false
                },
                existing = recipeToEdit
            )
        }

        if (showSuccessAlert) {
            AlertDialog(
                onDismissRequest = { showSuccessAlert = false },
                title = { Text("Success!") },
                text = { Text("Shopping list generated successfully") },
                confirmButton = {
                    TextButton(
                        onClick = { showSuccessAlert = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    inventory: List<FoodItem>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onGenerateShoppingList: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row {
                    IconButton(
                        onClick = onEdit,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = onDelete,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = recipe.ingredients.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = recipe.instructions,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGenerateShoppingList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Generate Shopping List")
            }
        }
    }
}

@Composable
fun AddRecipeDialog(
    onDismiss: () -> Unit,
    onAdd: (Recipe) -> Unit,
    existing: Recipe? = null
) {
    var title by remember { mutableStateOf(existing?.title ?: "") }
    var ingredientsText by remember { mutableStateOf(existing?.ingredients?.joinToString(", ") ?: "") }
    var instructions by remember { mutableStateOf(existing?.instructions ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existing == null) "Add New Recipe" else "Edit Recipe",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ingredientsText,
                    onValueChange = { ingredientsText = it },
                    label = { Text("Ingredients (comma-separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. eggs, milk, flour") }
                )
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            //Button(
                onClick = {
                    val ingredients = ingredientsText
                        .split(",")
                        .map { it.trim().lowercase() }
                        .filter { it.isNotEmpty() }
                    val recipe = Recipe(
                        id = existing?.id ?: 0,
                        title = title,
                        ingredients = ingredients,
                        instructions = instructions,
                        isLocal = true
                    )
                    onAdd(recipe)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancel")
            }
        }
    )
}