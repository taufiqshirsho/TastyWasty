package com.taufiq.tastywasty.ui.screens.inventory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taufiq.tastywasty.data.model.FoodItem
import com.taufiq.tastywasty.viewModel.FoodViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryScreen(viewModel: FoodViewModel) {
    val items by viewModel.allItems.collectAsStateWithLifecycle()
    val expiringSoon by viewModel.expiringSoon.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    val currentTime = System.currentTimeMillis()
    val expiredItems = items.filter { it.expiryDate < currentTime }
    val nonExpiredItems = items.filter { it.expiryDate >= currentTime }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Food")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Expiring Soon Section (always visible)
            if (expiringSoon.isNotEmpty()) {
                //Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Expiring Soon",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(expiringSoon) { item ->
                            FoodItemRow(
                                item = item,
                                highlight = true,
                                onDelete = viewModel::deleteFood
                            )
                        }
                    }
                }
            }

            // Tabs for Expired/Non-expired items
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.padding(top = 16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Available (${nonExpiredItems.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Expired (${expiredItems.size})") }
                )
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> {
                    if (nonExpiredItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No available items",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(nonExpiredItems) { item ->
                                FoodItemRow(
                                    item = item,
                                    highlight = false,
                                    onDelete = viewModel::deleteFood
                                )
                            }
                        }
                    }
                }
                1 -> {
                    if (expiredItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No expired items",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(expiredItems) { item ->
                                FoodItemRow(
                                    item = item,
                                    highlight = false,
                                    onDelete = viewModel::deleteFood
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AddFoodDialog(
                onDismiss = { showDialog = false },
                onAdd = {
                    viewModel.addFood(it)
                    showDialog = false
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDialog(
    onDismiss: () -> Unit,
    onAdd: (FoodItem) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Food Item",
                style = MaterialTheme.typography.titleLarge
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val expiryMillis = selectedDateMillis ?: System.currentTimeMillis()
                    onAdd(
                        FoodItem(
                            name = name,
                            category = category,
                            quantity = quantity,
                            storageLocation = location,
                            expiryDate = expiryMillis
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Add")
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
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE)
                    } ?: "",
                    onValueChange = {},
                    label = { Text("Expiry Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick date")
                        }
                    }
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Storage Location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodItemRow(item: FoodItem, highlight: Boolean = false, onDelete: (FoodItem) -> Unit) {
    val expiryDaysLeft = ((item.expiryDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24))
    val isExpired = expiryDaysLeft < 0
    val expiryText = when {
        isExpired -> "Expired ${-expiryDaysLeft} days ago"
        expiryDaysLeft == 0L -> "Expires today"
        expiryDaysLeft == 1L -> "Expires tomorrow"
        else -> "Expires in $expiryDaysLeft days"
    }
    val expiryColor = if (isExpired) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = expiryText,
                        color = expiryColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• ${item.quantity}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• ${item.storageLocation}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(
                onClick = { onDelete(item) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}