package com.example.ibookproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var author: String,
    var genre: String,
    var description: String,
    val rating: Float,
    var coverImage: String,
    val uploadingUserId: String,
    val timestamp: Long = System.currentTimeMillis()
)
