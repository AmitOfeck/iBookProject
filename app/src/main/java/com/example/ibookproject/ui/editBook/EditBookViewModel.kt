package com.example.ibookproject.ui.editBook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.data.remote.BooksRemoteDataSource

class EditBookViewModel(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())

    suspend fun updateBook(updatedBook: BookEntity) {
        bookRepository.updateBook(updatedBook)
    }
}