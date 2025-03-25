package com.example.ibookproject.ui.comment

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private val commentRepository: CommentRepository = CommentRepository(BookDatabase.getDatabase(application).commentDao())

    suspend fun getCommentsForBook(bookId: Int): Flow<List<CommentEntity>> {
        return commentRepository.getCommentsForBook(bookId)
        }

    fun addComment(comment: CommentEntity) {
        viewModelScope.launch {
            commentRepository.addComment(comment)
        }
    }
}
