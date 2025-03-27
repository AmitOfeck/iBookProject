package com.example.ibookproject.data.remote

import com.example.ibookproject.data.entities.RatingEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RatingsRemoteDataSource {

    private val db = FirebaseFirestore.getInstance()
    private val ratingsCollection = db.collection("ratings")

    suspend fun addRating(rating: RatingEntity) {
        val ratingMap = hashMapOf(
            "bookId" to rating.bookId,
            "userId" to rating.userId,
            "rating" to rating.rating,
            "timestamp" to rating.timestamp,
        )
        ratingsCollection.add(ratingMap).await()
    }

    suspend fun updateRating(rating: RatingEntity) {
        val querySnapshot = ratingsCollection
            .whereEqualTo("bookId", rating.bookId)
            .whereEqualTo("userId", rating.userId)
            .get()
            .await()

        for (doc in querySnapshot.documents) {
            doc.reference.update(
                "rating", rating.rating
            ).await()
        }
    }

    suspend fun getUserRatingForBook(userId: String, bookId: String): RatingEntity? {
        val querySnapshot = ratingsCollection
            .whereEqualTo("bookId", bookId)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return querySnapshot.documents.firstOrNull()?.let { doc ->
            RatingEntity(
                id = doc.id.hashCode(),
                bookId = doc.getString("bookId") ?: "",
                userId = doc.getString("userId") ?: "",
                rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
            )
        }
    }

    suspend fun getAllRatings(): List<RatingEntity> {
        val querySnapshot = ratingsCollection.get().await()
        return querySnapshot.documents.map { doc ->
            RatingEntity(
                id = doc.id.hashCode(),
                bookId = doc.getString("bookId") ?: "",
                userId = doc.getString("userId") ?: "",
                rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
            )
        }
    }
}
