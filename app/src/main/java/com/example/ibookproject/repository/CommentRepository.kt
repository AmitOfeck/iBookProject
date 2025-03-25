package com.example.ibookproject.repository

import androidx.lifecycle.LiveData
import com.example.ibookproject.data.dao.CommentDao
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.remote.CommentsRemoteDataSource

class CommentRepository(private val commentDao: CommentDao) {
    private val commentsRemoteDataSource: CommentsRemoteDataSource = CommentsRemoteDataSource()

    private val cacheValidityTime = 10 * 1000

    suspend fun getCommentsForBook(bookId: Int): LiveData<List<CommentEntity>> {
        val cachedComment = commentDao.getLatestCommentForBook(bookId)
        val currentTime = System.currentTimeMillis()

        if (cachedComment != null && currentTime - cachedComment.lastUpdated < cacheValidityTime) {
            return commentDao.getCommentsForBook(bookId)
        } else {
            val remoteComments = commentsRemoteDataSource.getCommentsForBook(bookId)

            commentDao.deleteCommentsForBook(bookId)

            remoteComments.forEach {
                it.lastUpdated = currentTime
                commentDao.insertComment(it)
            }

            return commentDao.getCommentsForBook(bookId)
        }
    }

    suspend fun addComment(comment: CommentEntity) {
        commentsRemoteDataSource.addComment(comment)
        commentDao.insertComment(comment)
    }

    suspend fun getCommentsByUserId(userId: String): LiveData<List<CommentEntity>> {
        val cachedComment = commentDao.getLatestCommentByUserId(userId)
        val currentTime = System.currentTimeMillis()
        if (cachedComment != null && currentTime - cachedComment.lastUpdated > cacheValidityTime) {
            val remoteComments = commentsRemoteDataSource.getCommentsByUserId(userId)
            commentDao.deleteCommentsByUserId(userId)

            remoteComments.forEach {
                it.lastUpdated = currentTime
                commentDao.insertComment(it)
            }
        }

        return commentDao.getCommentsByUserId(userId)
    }
}