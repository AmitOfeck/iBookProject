package com.example.ibookproject.ui.bookDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ibookproject.R
import com.example.ibookproject.ui.book.Book

class BookDetailsFragment : Fragment() {

    private lateinit var tvBookTitle: TextView
    private lateinit var tvBookAuthor: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var userRatingBar: RatingBar
    private lateinit var btnSubmitRating: Button
    private lateinit var tvComments: TextView
    private lateinit var etComment: EditText
    private lateinit var btnPostComment: Button
    private lateinit var ivBookCover: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_book_details, container, false)

        tvBookTitle = view.findViewById(R.id.tvBookTitle)
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor)
        ratingBar = view.findViewById(R.id.ratingBar)
        userRatingBar = view.findViewById(R.id.userRatingBar)
        btnSubmitRating = view.findViewById(R.id.btnSubmitRating)
        tvComments = view.findViewById(R.id.tvComments)
        etComment = view.findViewById(R.id.etComment)
        btnPostComment = view.findViewById(R.id.btnPostComment)
        ivBookCover = view.findViewById(R.id.ivBookCover)
        loadBookData()
        setupListeners()

        return view
    }

    private fun loadBookData() {
        // נתונים לדוגמה (יש להחליף בשליפה ממסד נתונים)
        val book = Book("The Great Adventure", "John Smith", "Adventure", R.drawable.img)
        val comments = listOf(
            "A thrilling read from start to finish! - Alice B.",
            "Couldn't put it down! - Robert L."
        )

        tvBookTitle.text = book.title
        tvBookAuthor.text = "by ${book.author}"
        ratingBar.rating = 4.5f // ניתן להחליף בנתונים אמיתיים
        tvComments.text = comments.joinToString("\n")
        ivBookCover.setImageResource(book.imageRes)
    }

    private fun setupListeners() {
        btnSubmitRating.setOnClickListener {
            val userRating = userRatingBar.rating
            // כאן ניתן לשמור את הדירוג במסד נתונים
        }

        btnPostComment.setOnClickListener {
            val newComment = etComment.text.toString().trim()
            if (newComment.isNotEmpty()) {
                tvComments.append("\n$newComment")
                etComment.text.clear()
                // כאן ניתן להוסיף את התגובה למסד נתונים
            }
        }
    }
}
