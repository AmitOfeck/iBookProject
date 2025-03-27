package com.example.ibookproject.repository

import com.example.ibookproject.data.dao.CommentDao
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.remote.CommentsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CommentRepository(private val commentDao: CommentDao) {
    private val commentsRemoteDataSource: CommentsRemoteDataSource = CommentsRemoteDataSource()

    private val cacheValidityTime = 60 * 1000

     fun getCommentsForBook(bookId: String): Flow<List<CommentEntity>> = flow {
        val cachedComment = commentDao.getLatestCommentForBook(bookId)
        val currentTime = System.currentTimeMillis()

        if (cachedComment != null && currentTime - cachedComment.lastUpdated < cacheValidityTime) {
            emit(commentDao.getCommentsForBook(bookId).first())
        } else {
            val remoteComments = commentsRemoteDataSource.getCommentsForBook(bookId)

            commentDao.deleteCommentsForBook(bookId)

            remoteComments.forEach {
                it.lastUpdated = currentTime
                commentDao.insertComment(it)
            }

            emit(commentDao.getCommentsForBook(bookId).first())
        }
    }

    suspend fun addComment(comment: CommentEntity) {
        commentsRemoteDataSource.addComment(comment)
        commentDao.insertComment(comment)
    }

    fun getCommentsByUserId(userId: String): Flow<List<CommentEntity>> = flow {
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

        emit(commentDao.getCommentsByUserId(userId).first())
    }
}
