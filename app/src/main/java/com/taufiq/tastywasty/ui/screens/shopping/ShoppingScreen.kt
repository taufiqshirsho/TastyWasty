package com.taufiq.tastywasty.ui.screens.shopping

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taufiq.tastywasty.data.model.ShoppingList
import com.taufiq.tastywasty.viewModel.ShoppingViewModel

@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel) {
    val lists by viewModel.shoppingLists.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<ShoppingList?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editTarget = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Shopping List")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Shopping Lists",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(lists) { list ->
                    ShoppingListCard(
                        list = list,
                        onEdit = {
                            editTarget = list
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteList(list) },
                        onToggleDone = { viewModel.toggleDone(list) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        var name by remember { mutableStateOf(editTarget?.name ?: "") }
        var items by remember { mutableStateOf(editTarget?.items ?: "") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editTarget == null) "Add Shopping List" else "Edit Shopping List") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("List Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = items,
                        onValueChange = { items = it },
                        label = { Text("Items (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editTarget != null) {
                        viewModel.updateList(editTarget!!.copy(name = name, items = items))
                    } else {
                        viewModel.addList(name, items)
                    }
                    showDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ShoppingListCard(
    list: ShoppingList,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleDone: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (list.done) Color(0xFFE0E0E0) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = list.done,
                    onCheckedChange = { onToggleDone() }
                )
                Text(
                    text = if (list.done) "Completed" else "Mark as Done",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (list.done) Color.Gray else MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Text(
                text = list.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Items: ${list.items}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
