package com.example.ibookproject.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.BookEntity

class BooksAdapter(private var books: List<BookEntity>, private val onBookClick: (Int) -> Unit) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view, onBookClick)  // 🔹 מעביר את ה-Callback ל-ViewHolder
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    class BookViewHolder(itemView: View, private val onBookClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {  // 🔹 מקבל את ה-Callback
        private val ivBookCover: ImageView = itemView.findViewById(R.id.ivBookCover)
        private val tvBookTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        private val tvBookAuthor: TextView = itemView.findViewById(R.id.tvBookAuthor)
        private val tvBookGenre: TextView = itemView.findViewById(R.id.tvBookGenre)

        fun bind(book: BookEntity) {
            tvBookTitle.text = book.title
            tvBookAuthor.text = "by ${book.author}"
            tvBookGenre.text = "Genre: ${book.genre}"

            // טעינת תמונה עם Glide
            Glide.with(itemView.context)
                .load(book.coverImage)
                .into(ivBookCover)

            itemView.setOnClickListener {
                onBookClick(book.id)  // 🔹 קריאה ל-Callback עם ה-ID של הספר
            }
        }
    }

    fun updateBooks(newBooks: List<BookEntity>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
