package com.example.ibookproject.ui.editBook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.repository.FirebaseRepository

class EditBookViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao(), firebaseRepository)

    suspend fun updateBook(updatedBook: BookEntity) {
        bookRepository.updateBook(updatedBook)
    }
}