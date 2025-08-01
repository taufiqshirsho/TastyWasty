package com.taufiq.tastywasty.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taufiq.tastywasty.data.model.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items ORDER BY expiryDate ASC")
    fun getAll(): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FoodItem)

    @Delete
    suspend fun delete(item: FoodItem)

    @Query("SELECT * FROM food_items WHERE expiryDate <= :soon")
    fun getExpiringSoon(soon: Long): Flow<List<FoodItem>>
}
