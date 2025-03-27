package com.example.ibookproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey var id: String = "",
    var title: String = "",
    var author: String = "",
    var genre: String = "",
    var description: String = "",
    val rating: Float = 0.0f,
    var coverImage: String = "",
    val uploadingUserId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    var lastUpdated: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", 0.0f, "", "", 0L)
}
