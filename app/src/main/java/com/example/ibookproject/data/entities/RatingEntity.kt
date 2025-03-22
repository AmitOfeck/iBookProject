package com.example.ibookproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratings")
data class RatingEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val bookId: Int,
    val userId: String,
    val rating: Float,
    val timestamp: Long = System.currentTimeMillis()
)
