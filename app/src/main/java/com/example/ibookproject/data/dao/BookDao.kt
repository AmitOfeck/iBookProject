package com.example.ibookproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.data.entities.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE genre IN (:genres)")
    fun getBooksByGenres(genres: List<String>): Flow<List<BookEntity>>

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE uploadingUserId = :userId")
    fun getBooksByUserId(userId: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: String): Flow<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity) : Long

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("SELECT * FROM books WHERE id IN (:bookIds)")
    fun getBooksByIds(bookIds: List<String>): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE uploadingUserId = :userId ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestBookByUser(userId: String): BookEntity?

    @Query("SELECT * FROM books ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestBook(): BookEntity?

    @Query("SELECT * FROM books WHERE id IN (:bookIds) ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestBookById(bookIds: List<String>): BookEntity?

    @Query("DELETE FROM books")
    suspend fun clearBooks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>) {
        // Update the lastUpdated time for each book before inserting
        val updatedBooks = books.map { book ->
            book.copy(lastUpdated = System.currentTimeMillis()) // or use your desired timestamp method
        }
        // Insert the updated books
        insertBooksList(updatedBooks)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooksList(books: List<BookEntity>)
}
