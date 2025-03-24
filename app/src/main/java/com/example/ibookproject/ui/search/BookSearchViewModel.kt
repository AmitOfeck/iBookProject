package com.example.ibookproject.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.repository.FirebaseRepository

class BookSearchViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao(), firebaseRepository)

    fun getAllBooks(): LiveData<List<BookEntity>> {
        return bookRepository.getAllBooks().asLiveData()
    }
}


