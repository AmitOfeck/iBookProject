package com.example.ibookproject.ui.comment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.repository.CommentRepository
import kotlinx.coroutines.launch

class CommentViewModel(application: Application) : AndroidViewModel(application) {

    private val commentRepository: CommentRepository = CommentRepository(BookDatabase.getDatabase(application).commentDao())

    fun getCommentsForBook(bookId: Int): LiveData<List<CommentEntity>> {
        return commentRepository.getCommentsForBook(bookId)
    }

    fun addComment(comment: CommentEntity) {
        viewModelScope.launch {
            commentRepository.addComment(comment)
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            commentRepository.deleteComment(commentId)
        }
    }
}
