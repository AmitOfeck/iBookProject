package com.example.ibookproject.data.repository

import com.example.ibookproject.data.dao.UserDao
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.data.remote.UserRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDao,
    private val remoteDataSource: UserRemoteDataSource
) {
    private val userTtl = 60 * 1000

    //  לוקאלי (Room)
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserEntity) {
        user.lastUpdated = System.currentTimeMillis()
        userDao.updateUser(user)
    }

    fun getUserById(userId: String): Flow<UserEntity?> = flow {
        val cachedUser = withContext(Dispatchers.IO) {userDao.getUserEntityById(userId)}
        val isExpired = (System.currentTimeMillis() - (cachedUser?.lastUpdated ?: 0)) > userTtl

        if (cachedUser != null && !isExpired) {
            emit(cachedUser)
        } else {
            fetchUserFromRemote(userId).collect { remoteUser ->
                if (remoteUser != null) {
                    userDao.insertUser(remoteUser) // שמור את הנתונים החדשים בבסיס הנתונים המקומי
                }

                emit(remoteUser)
            }
        }
    }


    //  מרוחק (Firestore)
    fun saveUserToRemote(user: UserEntity, onResult: (Boolean) -> Unit) {
        remoteDataSource.saveUserProfileToFirestore(user, onResult)
    }

    private fun fetchUserFromRemote(userId: String): Flow<UserEntity?> {
        return remoteDataSource.getUserProfileFromFirestore(userId)
    }
}
