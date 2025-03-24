package com.example.ibookproject.repository

import androidx.annotation.Nullable
import com.example.ibookproject.data.dao.BookDao
import com.example.ibookproject.data.entities.BookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookRepository(private val bookDao: BookDao, private val firebaseRepository: FirebaseRepository) {

    fun getBookById(bookId: Int): Flow<BookEntity> = flow {
        val cachedBook = bookDao.getBookById(bookId).first()
        if (cachedBook != null) {
            emit(cachedBook)
        } else {
            val bookFromFirebase = firebaseRepository.getBookFromFirebase(bookId)
            if (bookFromFirebase != null) {
                bookDao.insertBook(bookFromFirebase)
                emit(bookFromFirebase)
            }
        }
    }

    fun getAllBooks(): Flow<List<BookEntity>> = flow {
        val cachedBooks = bookDao.getAllBooks().first()
        if (cachedBooks.isNotEmpty()) {
            emit(cachedBooks)
        } else {
            val booksFromFirebase = firebaseRepository.getAllBooksFromFirebase()
            bookDao.insertBooks(booksFromFirebase)
            emit(booksFromFirebase)
        }
    }

    suspend fun insertBook(book: BookEntity): Int {
        val bookId = bookDao.insertBook(book).toInt()
        firebaseRepository.saveBookToFirebase(book)
        return bookId
    }

    suspend fun deleteBook(bookId: Int) {
        bookDao.deleteBook(bookId)
        firebaseRepository.deleteBookFromFirebase(bookId)
    }

    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
        firebaseRepository.saveBookToFirebase(book)
    }


    // Get books by IDs, first trying Room, then Firebase
    fun getBooksByIds(bookIds: List<Int>): Flow<List<BookEntity>> = flow {
        val localBooks = bookDao.getBooksByIds(bookIds).firstOrNull()
        if (!localBooks.isNullOrEmpty()) {
            emit(localBooks)
        } else {
            val firebaseBooks = firebaseRepository.getBooksByIds(bookIds)
            bookDao.insertBooks(firebaseBooks)
            emit(firebaseBooks)
        }
    }.flowOn(Dispatchers.IO)

    // Get books by uploading user ID, first trying Room, then Firebase
    fun getBooksByUploadingUser(userId: String): Flow<List<BookEntity>> = flow {
        val localBooks = bookDao.getBooksByUserId(userId).firstOrNull()
        if (!localBooks.isNullOrEmpty()) {
            emit(localBooks)
        } else {
            val firebaseBooks = firebaseRepository.getBooksByUploadingUser(userId)
            bookDao.insertBooks(firebaseBooks)
            emit(firebaseBooks)
        }
    }
}
