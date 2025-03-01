package com.example.ibookproject.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ibookproject.R

class BooksAdapter(private var books: List<Book>) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = books.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBookCover: ImageView = itemView.findViewById(R.id.ivBookCover)
        private val tvBookTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        private val tvBookAuthor: TextView = itemView.findViewById(R.id.tvBookAuthor)
        private val tvBookGenre: TextView = itemView.findViewById(R.id.tvBookGenre)

        fun bind(book: Book) {
            ivBookCover.setImageResource(book.imageRes)
            tvBookTitle.text = book.title
            tvBookAuthor.text = "by ${book.author}"
            tvBookGenre.text = "Genre: ${book.genre}"
        }
    }

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
