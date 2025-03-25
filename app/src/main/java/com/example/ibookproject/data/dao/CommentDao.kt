package com.example.ibookproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ibookproject.data.entities.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Query("SELECT * FROM comments WHERE bookId = :bookId")
    fun getCommentsForBook(bookId: Int): Flow<List<CommentEntity>>

    @Query("DELETE FROM comments WHERE bookId = :bookId")
    suspend fun deleteCommentsForBook(bookId: Int)

    @Query("SELECT * FROM comments WHERE userId = :userId")
    fun getCommentsByUserId(userId: String): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE bookId = :bookId ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestCommentForBook(bookId: Int): CommentEntity?

    @Query("SELECT * FROM comments WHERE userId = :userId ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestCommentByUserId(userId: String): CommentEntity?

    @Query("DELETE FROM comments WHERE userId = :userId")
    suspend fun deleteCommentsByUserId(userId: String)
}
