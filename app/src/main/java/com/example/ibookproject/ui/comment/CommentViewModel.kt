package com.example.ibookproject.ui.comment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private val commentRepository: CommentRepository = CommentRepository(BookDatabase.getDatabase(application).commentDao())

    private val _comments = MutableStateFlow<List<CommentEntity>>(emptyList())
    val comments: StateFlow<List<CommentEntity>> = _comments

    fun getCommentsForBook(bookId: Int) {
        viewModelScope.launch {
            commentRepository.getCommentsForBook(bookId).collect { commentList ->
                _comments.value = commentList
            }
        }
    }

    fun addComment(comment: CommentEntity) {
        viewModelScope.launch {
            commentRepository.addComment(comment)
            getCommentsForBook(comment.bookId) // טעינת התגובות מחדש לאחר ההוספה
        }
    }
}
