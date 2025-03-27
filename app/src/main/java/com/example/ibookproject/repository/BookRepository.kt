package com.example.ibookproject.repository

import com.example.ibookproject.data.dao.BookDao
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.data.remote.BooksRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookRepository(private val bookDao: BookDao) {
    private val booksRemoteDataSource: BooksRemoteDataSource = BooksRemoteDataSource()

    fun getBookById(bookId: String): Flow<BookEntity> = flow {
        val cachedBook = bookDao.getBookById(bookId).first()
        if (cachedBook != null) {
            emit(cachedBook)
        } else {
            val bookFromFirebase = booksRemoteDataSource.getBookFromFirebase(bookId)
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
            val booksFromFirebase = booksRemoteDataSource.getAllBooksFromFirebase()
            bookDao.insertBooks(booksFromFirebase)
            emit(booksFromFirebase)
        }
    }

    suspend fun insertBook(book: BookEntity) {
        bookDao.insertBook(book).toInt()
        booksRemoteDataSource.saveBookToFirebase(book)
    }

    suspend fun deleteBook(bookId: String) {
        bookDao.deleteBook(bookId)
        booksRemoteDataSource.deleteBookFromFirebase(bookId)
    }

    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
        booksRemoteDataSource.saveBookToFirebase(book)
    }


    // Get books by IDs, first trying Room, then Firebase
    fun getBooksByIds(bookIds: List<String>): Flow<List<BookEntity>> = flow {
        val localBooks = bookDao.getBooksByIds(bookIds).firstOrNull()
        if (!localBooks.isNullOrEmpty()) {
            emit(localBooks)
        } else {
            val firebaseBooks = booksRemoteDataSource.getBooksByIds(bookIds)
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
            val firebaseBooks = booksRemoteDataSource.getBooksByUploadingUser(userId)
            bookDao.insertBooks(firebaseBooks)
            emit(firebaseBooks)
        }
    }
}
