package com.example.ibookproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String = "",
    val name: String = "",
    val bio: String = "",
    val favoriteGenres: String = "",
    val profileImage: String = "",
    var lastUpdated: Long = System.currentTimeMillis()) {

    fun getGenresList(): List<String> {
        return favoriteGenres.split(",").map { it.trim() }
    }

    fun genresToString(genres: List<String>): String {
        return genres.joinToString(",")
    }

    fun stringToGenres(genresString: String): List<String> {
        return genresString.split(",").map { it.trim() }
    }
}
