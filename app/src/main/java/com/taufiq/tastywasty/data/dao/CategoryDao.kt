package com.taufiq.tastywasty.data.dao

import androidx.room.*
import com.taufiq.tastywasty.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories WHERE userId = :userId")
    fun getCategoriesForUser(userId: Int): Flow<List<Category>>

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?
}
