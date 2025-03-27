package com.example.ibookproject.ui.addBook

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.data.remote.BooksRemoteDataSource
import com.example.ibookproject.data.remote.ImageUploadService
import com.example.ibookproject.repository.ImageUploadRepository
import kotlinx.coroutines.launch

class AddBookViewModel(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())
    private val uploadImageRepository: ImageUploadRepository = ImageUploadRepository()

    private val _bookId = MutableLiveData<Int>()
    val bookId: LiveData<Int> get() = _bookId

    fun addBook(book: BookEntity) {
        viewModelScope.launch {
            val bookId = bookRepository.insertBook(book)
            _bookId.postValue(bookId)
        }
    }

    fun uploadImage(imageUri: Uri, storagePath: String, onResult: (String?) -> Unit) {
        uploadImageRepository.uploadImage(imageUri, storagePath, onResult)
    }
}