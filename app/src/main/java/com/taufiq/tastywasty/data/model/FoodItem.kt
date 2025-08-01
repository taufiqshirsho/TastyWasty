package com.taufiq.tastywasty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val expiryDate: Long,
    val quantity: String,
    val storageLocation: String // e.g., fridge, pantry
)
