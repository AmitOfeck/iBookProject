package com.example.ibookproject.ui.rating

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.database.BookDatabase
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.entities.RatingEntity
import com.example.ibookproject.data.repository.RatingRepository
import com.example.ibookproject.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RatingViewModel(application: Application) : AndroidViewModel(application) {

    private val ratingRepository: RatingRepository = RatingRepository(BookDatabase.getDatabase(application).ratingDao())

    private val _avgRating = MutableStateFlow(0.0f)
    val AVGrating: StateFlow<Float> = _avgRating

    private val _userRating = MutableStateFlow<RatingEntity?>(null)
    val userRating: StateFlow<RatingEntity?> = _userRating


    fun getAverageRating(bookId: String) {
        viewModelScope.launch {
            _avgRating.value = ratingRepository.getAverageRating(bookId)
        }
    }

    fun addRating(rating: RatingEntity) {
        viewModelScope.launch {
            ratingRepository.addRating(rating)
            getAverageRating(rating.bookId)
            _userRating.value = rating
        }
    }


    fun updateRating(rating: RatingEntity) {
        viewModelScope.launch {
            ratingRepository.updateRating(rating)
            getAverageRating(rating.bookId)
            _userRating.value = rating
        }
    }

     fun getUserRatingForBook(userId:String, bookId: String) {
        viewModelScope.launch {
            _userRating.value = ratingRepository.getUserRatingForBook(userId, bookId)
        }
    }

    fun resetUserRatingForBook() {
        viewModelScope.launch {
            _userRating.value = null
        }
    }

}
