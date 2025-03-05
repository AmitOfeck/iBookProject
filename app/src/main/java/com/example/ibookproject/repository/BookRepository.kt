package com.example.ibookproject.repository

import com.example.ibookproject.data.dao.BookDao
import com.example.ibookproject.data.entities.BookEntity
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    fun getBooksByGenres(genres: List<String>): Flow<List<BookEntity>> {
        return bookDao.getBooksByGenres(genres)
    }

    fun getBooksByUploadingUser(userId: String): Flow<List<BookEntity>> {
        return bookDao.getBooksByUserId(userId)
    }

    fun getAllBooks(): Flow<List<BookEntity>> {
        return bookDao.getAllBooks()
    }

    suspend fun insertBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}