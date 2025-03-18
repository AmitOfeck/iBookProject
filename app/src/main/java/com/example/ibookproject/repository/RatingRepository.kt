package com.example.ibookproject.data.repository

import com.example.ibookproject.data.dao.RatingDao
import com.example.ibookproject.data.entities.RatingEntity

class RatingRepository(private val ratingDao: RatingDao) {

    suspend fun addRating(rating: RatingEntity) {
        ratingDao.insertRating(rating)
    }

    suspend fun updateRating(rating: RatingEntity) {
        ratingDao.updateRating(rating)
    }

    fun getUserRatingForBook(userId:String, bookId: Int) = ratingDao.getUserRatingForBook(userId, bookId)

    fun getAverageRating(bookId: Int) = ratingDao.getAverageRating(bookId)
}
