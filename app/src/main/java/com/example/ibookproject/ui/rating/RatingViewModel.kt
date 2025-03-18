package com.example.ibookproject.ui.rating

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.RatingEntity
import com.example.ibookproject.data.repository.RatingRepository
import kotlinx.coroutines.launch

class RatingViewModel(application: Application) : AndroidViewModel(application) {

    private val ratingRepository: RatingRepository = RatingRepository(BookDatabase.getDatabase(application).ratingDao())

    fun addRating(rating: RatingEntity) {
        viewModelScope.launch {
            ratingRepository.addRating(rating)
        }
    }

    fun updateRating(rating: RatingEntity) {
        viewModelScope.launch {
            ratingRepository.updateRating(rating)
        }
    }

    fun getUserRatingForBook(userId:String, bookId: Int) = ratingRepository.getUserRatingForBook(userId, bookId)

    fun getAverageRating(bookId: Int) = ratingRepository.getAverageRating(bookId)
}
