package com.example.ibookproject.repository

import com.example.ibookproject.data.dao.UserDao
import com.example.ibookproject.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    fun getUserById(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)
}
