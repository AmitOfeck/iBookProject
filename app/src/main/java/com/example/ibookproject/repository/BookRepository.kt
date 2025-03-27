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
    private val ratingsTtl = 60 * 1000

    fun getBookById(bookId: String): Flow<BookEntity> = flow {
        val cachedBook = bookDao.getBookById(bookId).first()
        val isExpired = (System.currentTimeMillis() - cachedBook.lastUpdated) > ratingsTtl

        if (!isExpired) {
            emit(cachedBook)
        } else {
            val bookFromFirebase = booksRemoteDataSource.getBookFromFirebase(bookId)
            if (bookFromFirebase != null) {
                bookDao.updateBook(bookFromFirebase)
                emit(bookFromFirebase)
            }
        }
    }

    fun getAllBooks(): Flow<List<BookEntity>> = flow {
        val cachedBook = bookDao.getLatestBook()
        val isExpired = (System.currentTimeMillis() - (cachedBook?.lastUpdated ?: 0)) > ratingsTtl

        if (cachedBook != null && !isExpired) {
            val cachedBooks = bookDao.getAllBooks().first()
            emit(cachedBooks)
        } else {
            val booksFromFirebase = booksRemoteDataSource.getAllBooksFromFirebase()
            bookDao.clearBooks()
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
        val cachedBook = bookDao.getLatestBookById(bookIds)
        val isExpired = (System.currentTimeMillis() - (cachedBook?.lastUpdated ?: 0)) > ratingsTtl

        if (!isExpired) {
            val localBooks = bookDao.getBooksByIds(bookIds).firstOrNull()
            emit(localBooks!!)
        } else {
            val firebaseBooks = booksRemoteDataSource.getBooksByIds(bookIds)
            bookDao.insertBooks(firebaseBooks)
            emit(firebaseBooks)
        }
    }.flowOn(Dispatchers.IO)

    // Get books by uploading user ID, first trying Room, then Firebase
    fun getBooksByUploadingUser(userId: String): Flow<List<BookEntity>> = flow {
        val cachedBook = bookDao.getLatestBookByUser(userId)
        val isExpired = (System.currentTimeMillis() - (cachedBook?.lastUpdated ?: 0)) > ratingsTtl

        if (cachedBook != null && !isExpired) {
            val localBooks = bookDao.getBooksByUserId(userId).firstOrNull()
            emit(localBooks!!)
        } else {
            val firebaseBooks = booksRemoteDataSource.getBooksByUploadingUser(userId)
            bookDao.insertBooks(firebaseBooks)
            emit(firebaseBooks)
        }
    }
}
