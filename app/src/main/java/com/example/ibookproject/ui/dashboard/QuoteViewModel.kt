package com.example.ibookproject.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibookproject.data.entities.Quote
import com.example.ibookproject.data.remote.QuoteApiService
import com.example.ibookproject.data.repository.QuoteRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteViewModel : ViewModel() {

    private val api: QuoteApiService = Retrofit.Builder()
        .baseUrl("http://api.quotable.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuoteApiService::class.java)

    private val quoteRepository: QuoteRepository = QuoteRepository(api)

    private val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>> = _quotes

    fun loadQuotes() {
        viewModelScope.launch {
            try {
                val randomQuotes = quoteRepository.getRandomQuotes()
                _quotes.postValue(randomQuotes)
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "Error: ${e.message}", e)
                _quotes.postValue(emptyList())
            }
        }
    }
}
