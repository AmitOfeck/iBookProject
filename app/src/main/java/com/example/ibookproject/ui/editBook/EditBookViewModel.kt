package com.example.ibookproject.ui.editBook

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.repository.ImageUploadRepository

class EditBookViewModel(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())
    private val uploadImageRepository: ImageUploadRepository = ImageUploadRepository()

    suspend fun updateBook(updatedBook: BookEntity) {
        bookRepository.updateBook(updatedBook)
    }

    fun uploadImage(imageUri: Uri, storagePath: String, onResult: (String?) -> Unit) {
        uploadImageRepository.uploadImage(imageUri, storagePath, onResult)
    }
}