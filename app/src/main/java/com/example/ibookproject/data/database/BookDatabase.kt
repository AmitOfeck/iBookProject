package com.example.ibookproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ibookproject.data.dao.BookDao
import com.example.ibookproject.data.dao.CommentDao
import com.example.ibookproject.data.dao.RatingDao
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.entities.RatingEntity

@Database(entities = [BookEntity::class, CommentEntity::class, RatingEntity::class], version = 4, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun commentDao(): CommentDao
    abstract fun ratingDao(): RatingDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                )
                    .fallbackToDestructiveMigration()  // Handling migrations when the schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
