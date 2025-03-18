package com.example.ibookproject.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.UserDatabase
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.repository.UserRepository
import kotlinx.coroutines.launch
import android.util.Log

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository = UserRepository(UserDatabase.getDatabase(application).userDao())

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user)
            Log.d("UserViewModel", "User saved: $user")
        }
    }

    fun getUserById(userId: String) = repository.getUserById(userId).asLiveData()

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}
