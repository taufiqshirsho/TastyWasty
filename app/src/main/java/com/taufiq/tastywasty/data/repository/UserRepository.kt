package com.taufiq.tastywasty.data.repository

import com.taufiq.tastywasty.data.dao.UserDao
import com.taufiq.tastywasty.data.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun register(user: User): Long = userDao.insert(user)
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    suspend fun login(email: String, password: String): User? = userDao.login(email, password)
}

