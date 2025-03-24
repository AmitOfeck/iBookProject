package com.example.ibookproject.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ibookproject.data.entities.CommentEntity

@Dao
interface CommentDao {
    @Insert
    suspend fun insertComment(comment: CommentEntity)

    @Query("SELECT * FROM comments WHERE bookId = :bookId")
    fun getCommentsForBook(bookId: Int): LiveData<List<CommentEntity>>

    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: Int)

    @Query("SELECT * FROM comments WHERE userId = :userId")
    fun getCommentsByUserId(userId: String): LiveData<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)
}
