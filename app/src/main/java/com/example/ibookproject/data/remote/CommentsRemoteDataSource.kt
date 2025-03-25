package com.example.ibookproject.data.remote

import com.example.ibookproject.data.entities.CommentEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class CommentsRemoteDataSource {

    private val db = FirebaseFirestore.getInstance()
    private val commentsCollection = db.collection("comments")

    suspend fun addComment(comment: CommentEntity) {
        val commentMap = hashMapOf(
            "bookId" to comment.bookId,
            "userId" to comment.userId,
            "comment" to comment.comment,
            "timestamp" to comment.timestamp
        )
        commentsCollection.add(commentMap).await()
    }

    suspend fun getCommentsForBook(bookId: Int): List<CommentEntity> {
        val querySnapshot = commentsCollection
            .whereEqualTo("bookId", bookId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.documents.map { doc ->
            CommentEntity(
                id = doc.id.hashCode(),
                bookId = doc.getLong("bookId")?.toInt() ?: 0,
                userId = doc.getString("userId") ?: "",
                comment = doc.getString("comment") ?: "",
                timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
            )
        }
    }

    suspend fun getCommentsByUserId(userId: String): List<CommentEntity> {
        val querySnapshot = commentsCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.documents.map { doc ->
            CommentEntity(
                id = doc.id.hashCode(),
                bookId = doc.getLong("bookId")?.toInt() ?: 0,
                userId = doc.getString("userId") ?: "",
                comment = doc.getString("comment") ?: "",
                timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
            )
        }
    }
}

