package com.example.ibookproject.repository

import androidx.lifecycle.LiveData
import com.example.ibookproject.data.dao.CommentDao
import com.example.ibookproject.data.entities.CommentEntity

class CommentRepository(private val commentDao: CommentDao) {

        fun getCommentsForBook(bookId: Int): LiveData<List<CommentEntity>> {
        return commentDao.getCommentsForBook(bookId)
    }

    suspend fun addComment(comment: CommentEntity) {
        commentDao.insertComment(comment)
    }

    suspend fun deleteComment(commentId: Int) {
        commentDao.deleteComment(commentId)
    }
}
