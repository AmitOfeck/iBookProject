package com.example.ibookproject.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ibookproject.data.entities.RatingEntity

@Dao
interface RatingDao {

    @Insert
    suspend fun insertRating(rating: RatingEntity)

    @Update
    suspend fun updateRating(rating: RatingEntity)

    @Query("SELECT * FROM ratings WHERE bookId = :bookId AND userId = :userId")
    fun getUserRatingForBook(userId:String, bookId: Int): LiveData<RatingEntity>

    @Query("SELECT AVG(rating) FROM ratings WHERE bookId = :bookId")
    fun getAverageRating(bookId: Int): LiveData<Float>
}
