package com.example.ibookproject.data.remote

import com.example.ibookproject.data.entities.BookEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class BooksRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val booksCollection = db.collection("books")

    suspend fun getBookFromFirebase(bookId: String): BookEntity? {
        return try {
            val document = booksCollection.document(bookId.toString()).get().await()
            document.toObject(BookEntity::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Get books by list of IDs from Firebase
    suspend fun getBooksByIds(bookIds: List<String>): List<BookEntity> {
        return try {
            val snapshot = booksCollection.whereIn("id", bookIds).get().await()
            snapshot.toObjects(BookEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Get books by uploading user ID from Firebase
    suspend fun getBooksByUploadingUser(userId: String): List<BookEntity> {
        return try {
            val snapshot = booksCollection.whereEqualTo("uploadingUserId", userId).get().await()
            snapshot.toObjects(BookEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveBookToFirebase(book: BookEntity): Boolean {
        val documentRef = booksCollection.document(book.id)
        val documentSnapshot = documentRef.get().await()

        return if (documentSnapshot.exists()) {
            // Document exists, update it
            documentRef.set(book, SetOptions.merge()).await()
            true  // Document updated
        } else {
            // Document does not exist, create new document
            documentRef.set(book).await()
            false  // Document created
        }
    }

    suspend fun deleteBookFromFirebase(bookId: String) {
        booksCollection.document(bookId.toString()).delete().await()
    }

    suspend fun getAllBooksFromFirebase(): List<BookEntity> {
        return try {
            val snapshot = booksCollection.get().await()
            snapshot.toObjects(BookEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
