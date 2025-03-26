package com.example.ibookproject.data.repository

import com.example.ibookproject.data.entities.Quote
import com.example.ibookproject.data.remote.QuoteApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

class QuoteRepository(private val api: QuoteApiService) {

    suspend fun getRandomQuotes(count: Int = 7): List<Quote> = coroutineScope {
        val quoteRequests = mutableListOf<Deferred<Quote>>()

        for (i in 1..count) {
            quoteRequests.add(async {
                api.getRandomQuote()
            })
        }

        quoteRequests.awaitAll()
    }
}
