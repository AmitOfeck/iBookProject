package com.example.ibookproject.ui.bookDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository

class BookDetailsViewModel(application: Application) : AndroidViewModel(application)  {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())

    fun getBookById(bookId: Int): LiveData<BookEntity> {
        return bookRepository.getBoosById(bookId).asLiveData()
    }
}