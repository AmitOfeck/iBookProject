package com.example.ibookproject.data.dao

import androidx.room.*
import com.example.ibookproject.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserEntityById(userId: String): UserEntity?


    @Delete
    suspend fun deleteUser(user: UserEntity)
}
