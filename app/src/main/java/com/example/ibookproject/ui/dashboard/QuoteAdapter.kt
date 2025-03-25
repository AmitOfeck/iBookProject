package com.example.ibookproject.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.Quote

class QuoteAdapter(private var quotes: List<Quote>) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val currentQuote = quotes[position]
        holder.authorTextView.text = currentQuote.author
        holder.contentTextView.text = currentQuote.content
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    fun updateQuotes(newQuotes: List<Quote>) {
        quotes = newQuotes
        notifyDataSetChanged()
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView: TextView = itemView.findViewById(R.id.tvAuthor)
        val contentTextView: TextView = itemView.findViewById(R.id.tvContent)
    }
}
