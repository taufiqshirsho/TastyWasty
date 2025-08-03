package com.taufiq.tastywasty.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.taufiq.tastywasty.data.model.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    //@Insert
    suspend fun insertList(list: ShoppingList)

    @Update
    suspend fun updateList(list: ShoppingList)

    @Delete
    suspend fun deleteList(list: ShoppingList)

    @Query("SELECT * FROM shopping_lists")
    fun getAllLists(): Flow<List<ShoppingList>>
}
