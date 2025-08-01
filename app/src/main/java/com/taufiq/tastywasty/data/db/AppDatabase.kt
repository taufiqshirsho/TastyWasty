package com.taufiq.tastywasty.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.taufiq.tastywasty.data.model.*
import com.taufiq.tastywasty.data.dao.*

@Database(
    entities = [User::class, FoodItem::class, Recipe::class, ShoppingList::class],
    version = 4
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    abstract fun recipeDao(): RecipeDao
    abstract fun shoppingDao(): ShoppingDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_app_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build().also { INSTANCE = it }
            }
        }
    }
}
