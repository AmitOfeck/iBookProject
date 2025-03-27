package com.example.ibookproject.ui.userProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.repository.BookRepository
import com.example.ibookproject.repository.CommentRepository
import com.example.ibookproject.data.remote.BooksRemoteDataSource
import kotlinx.coroutines.launch

class UserProfileSearchView(application: Application) : AndroidViewModel(application) {
    private val bookRepository: BookRepository = BookRepository(BookDatabase.getDatabase(application).bookDao())
    private val commentRepository: CommentRepository = CommentRepository(BookDatabase.getDatabase(application).commentDao())

    private val _comments = MutableLiveData<List<CommentEntity>>()
    val comments: LiveData<List<CommentEntity>> get() = _comments

    fun getBooksByUser(userId: String): LiveData<List<BookEntity>> {
        return bookRepository.getBooksByUploadingUser(userId).asLiveData()
    }

//    fun getCommentsByUser(userId: String){
//        viewModelScope.launch {
//            commentRepository.getCommentsByUserId(userId).observeForever { commentList ->
//                _comments.value = commentList ?: emptyList()
//            }
//        }
//    }


    fun getBooksById(bookIds: List<String>): LiveData<List<BookEntity>> {
        return bookRepository.getBooksByIds(bookIds).asLiveData()
    }
}