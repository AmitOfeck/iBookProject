package com.example.ibookproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bookId: Int,
    val userId: String,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis(),
    var lastUpdated: Long = System.currentTimeMillis()
)
