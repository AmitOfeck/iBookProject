package com.example.ibookproject.ui.userProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.repository.CommentRepository
import com.example.ibookproject.data.remote.BooksRemoteDataSource

class UserProfileSearchView(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())
    private val commentRepository: CommentRepository = CommentRepository(BookDatabase.getDatabase(application).commentDao())

    fun getBooksByUser(userId: String): LiveData<List<BookEntity>> {
        return bookRepository.getBooksByUploadingUser(userId).asLiveData()
    }

    fun getCommentsByUser(userId: String): LiveData<List<CommentEntity>> {
        return commentRepository.getCommentsByUserId(userId)
    }

    fun getBooksById(bookIds: List<Int>): LiveData<List<BookEntity>> {
        return bookRepository.getBooksByIds(bookIds).asLiveData()
    }
}