package com.taufiq.tastywasty.data.repository

import com.taufiq.tastywasty.data.dao.FoodDao
import com.taufiq.tastywasty.data.model.FoodItem
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val dao: FoodDao) {
    fun getAllItems(): Flow<List<FoodItem>> = dao.getAll()

    fun getExpiringSoonItems(): Flow<List<FoodItem>> {
        val threeDays = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000)
        return dao.getExpiringSoon(threeDays)
    }

    suspend fun addItem(item: FoodItem) = dao.insert(item)

    suspend fun deleteItem(item: FoodItem) = dao.delete(item)
}
