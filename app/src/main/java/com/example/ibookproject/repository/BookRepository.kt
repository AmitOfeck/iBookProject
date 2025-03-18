package com.example.ibookproject.repository

import com.example.ibookproject.data.dao.BookDao
import com.example.ibookproject.data.entities.BookEntity
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    fun getBooksByUploadingUser(userId: String): Flow<List<BookEntity>> {
        return bookDao.getBooksByUserId(userId)
    }

    fun getBooksById(bookId: Int): Flow<BookEntity> {
        return bookDao.getBooksById(bookId)
    }

    fun getAllBooks(): Flow<List<BookEntity>> {
        return bookDao.getAllBooks()
    }

    suspend fun insertBook(book: BookEntity): Int {
        val bookId = bookDao.insertBook(book)
        return bookId.toInt()
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}