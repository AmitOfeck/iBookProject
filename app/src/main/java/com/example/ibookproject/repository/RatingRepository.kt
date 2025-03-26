package com.example.ibookproject.data.repository

import com.example.ibookproject.data.dao.RatingDao
import com.example.ibookproject.data.entities.RatingEntity
import com.example.ibookproject.data.remote.RatingsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class RatingRepository(
    private val ratingDao: RatingDao,
) {
    private val ratingsRemoteDataSource: RatingsRemoteDataSource = RatingsRemoteDataSource()
    private val TTL_MILLIS = 60 * 1000

    suspend fun addRating(rating: RatingEntity) {
        withContext(Dispatchers.IO) {
            ratingsRemoteDataSource.addRating(rating)
            ratingDao.insertRating(rating.copy(lastUpdated = System.currentTimeMillis()))
        }
    }

    suspend fun updateRating(rating: RatingEntity) {
        withContext(Dispatchers.IO) {
            ratingsRemoteDataSource.updateRating(rating)
            ratingDao.updateRating(rating.copy(lastUpdated = System.currentTimeMillis()))
        }
    }

    suspend fun getUserRatingForBook(userId: String, bookId: Int): RatingEntity? {
        return withContext(Dispatchers.IO) {
            var cachedRating = ratingDao.getUserRatingForBook(userId, bookId)
            val isExpired = cachedRating == null ||
                    (System.currentTimeMillis() - cachedRating.lastUpdated) > TTL_MILLIS

            if (isExpired) {
                refreshRatings()
                cachedRating = ratingDao.getUserRatingForBook(userId, bookId)
            }

            return@withContext cachedRating
        }
    }

    suspend fun getAverageRating(bookId: Int): Float {
        return withContext(Dispatchers.IO) {
            val lastUpdated = ratingDao.getLastUpdatedForBook(bookId) ?: 0L
            val isExpired = (System.currentTimeMillis() - lastUpdated) > TTL_MILLIS

            if (isExpired) {
                refreshRatings()
            }

            return@withContext ratingDao.getAverageRating(bookId) ?: 0.0f
        }
    }

    private suspend fun refreshRatings() {
        withContext(Dispatchers.IO) {
            val remoteRatings = ratingsRemoteDataSource.getAllRatings()

            // Clear outdated cache
            ratingDao.clearRatings()

            // Insert fresh data
            remoteRatings.forEach { rating ->
                ratingDao.insertRating(rating.copy(lastUpdated = System.currentTimeMillis()))
            }
        }
    }
}
