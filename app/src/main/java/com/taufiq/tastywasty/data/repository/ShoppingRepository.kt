package com.taufiq.tastywasty.data.repository

import com.taufiq.tastywasty.data.dao.ShoppingDao
import com.taufiq.tastywasty.data.model.ShoppingList
import kotlinx.coroutines.flow.Flow
class ShoppingRepository(private val dao: ShoppingDao) {
    fun getAllLists(): Flow<List<ShoppingList>> = dao.getAllLists()

    suspend fun insertList(list: ShoppingList) = dao.insertList(list)
    suspend fun updateList(list: ShoppingList) = dao.updateList(list)
    suspend fun deleteList(list: ShoppingList) = dao.deleteList(list)
}
