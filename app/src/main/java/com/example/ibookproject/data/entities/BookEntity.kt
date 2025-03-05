package com.example.ibookproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String,
    val rating: Int,
    val coverImage: String,
    val uploadingUserId: String
)
