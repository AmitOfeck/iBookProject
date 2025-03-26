package com.example.ibookproject.data.remote

import com.example.ibookproject.data.entities.Quote
import retrofit2.http.GET

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(): Quote
}
