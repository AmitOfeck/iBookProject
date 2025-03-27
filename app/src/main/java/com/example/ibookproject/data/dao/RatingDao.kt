package com.example.ibookproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ibookproject.data.entities.RatingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: RatingEntity)

    @Update
    suspend fun updateRating(rating: RatingEntity)

    @Query("SELECT * FROM ratings WHERE bookId = :bookId AND userId = :userId LIMIT 1")
    fun getUserRatingForBook(userId: String, bookId: String): RatingEntity?

    @Query("SELECT AVG(rating) FROM ratings WHERE bookId = :bookId")
    suspend fun getAverageRating(bookId: String): Float?

    @Query("DELETE FROM ratings")
    suspend fun clearRatings()

    @Query("SELECT MAX(lastUpdated) FROM ratings WHERE bookId = :bookId")
    suspend fun getLastUpdatedForBook(bookId: String): Long?
}
