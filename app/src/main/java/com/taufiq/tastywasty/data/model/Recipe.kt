package com.taufiq.tastywasty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val ingredients: List<String>, // stored as comma-separated string
    val instructions: String,
    val isLocal: Boolean = true
)
