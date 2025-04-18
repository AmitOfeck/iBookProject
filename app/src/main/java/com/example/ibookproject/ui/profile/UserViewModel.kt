package com.example.ibookproject.ui.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.example.ibookproject.data.database.UserDatabase
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.data.remote.UserRemoteDataSource
import com.example.ibookproject.data.repository.UserRepository
import com.example.ibookproject.repository.ImageUploadRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    private val uploadImageRepository: ImageUploadRepository

    private val _userLiveData = MutableLiveData<UserEntity?>()
    val userLiveData: LiveData<UserEntity?> = _userLiveData

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        val remoteDataSource = UserRemoteDataSource()
        userRepository = UserRepository(userDao, remoteDataSource)
        uploadImageRepository = ImageUploadRepository()
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

     fun getUserById(userId: String): LiveData<UserEntity?> {
         return userRepository.getUserById(userId).asLiveData()
    }

    fun saveUserToRemote(user: UserEntity, onResult: (Boolean) -> Unit) {
        userRepository.saveUserToRemote(user, onResult)
    }

    fun uploadImage(imageUri: Uri, storagePath: String, onResult: (String?) -> Unit) {
        uploadImageRepository.uploadImage(imageUri, storagePath, onResult)
    }
}
