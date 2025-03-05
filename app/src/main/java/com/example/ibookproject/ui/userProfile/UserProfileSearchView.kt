package com.example.ibookproject.ui.userProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository

class UserProfileSearchView(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())

    fun getBooksByUser(userId: String): LiveData<List<BookEntity>> {
        return bookRepository.getBooksByUploadingUser(userId).asLiveData()
    }
}