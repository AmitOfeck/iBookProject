package com.example.ibookproject.data.repository

import android.net.Uri
import com.example.ibookproject.data.dao.UserDao
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.data.remote.ImageUploadService
import com.example.ibookproject.data.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
    private val remoteDataSource: UserRemoteDataSource
) {

    private val imageUploadService = ImageUploadService()

    //  לוקאלי (Room)
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    fun getUserById(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)

    //  מרוחק (Firestore)
    fun saveUserToRemote(user: UserEntity, onResult: (Boolean) -> Unit) {
        remoteDataSource.saveUserProfileToFirestore(user, onResult)
    }

    fun fetchUserFromRemote(userId: String, onResult: (UserEntity?) -> Unit) {
        remoteDataSource.getUserProfileFromFirestore(userId, onResult)
    }

    //  Firebase Storage – העלאת תמונה
    fun uploadImage(imageUri: Uri, storagePath: String, onResult: (String?) -> Unit) {
        imageUploadService.uploadImage(imageUri, storagePath, onResult)
    }
}
