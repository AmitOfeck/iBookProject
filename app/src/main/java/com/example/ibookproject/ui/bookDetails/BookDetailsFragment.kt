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
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.ibookproject.R

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

    private val bookViewModel: BookDetailsViewModel by activityViewModels()

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
        setupListeners()

        arguments?.getInt("bookId")?.let { bookId ->
            bookViewModel.getBookById(bookId).observe(viewLifecycleOwner) { book ->
                val comments = emptyList<String>()

                tvBookTitle.text = book.title
                tvBookAuthor.text = "by ${book.author}"
                ratingBar.rating = book.rating
                tvComments.text = comments.joinToString("\n")

                Glide.with(requireContext())
                    .load(book.coverImage)
                    .into(ivBookCover)
            }
        }

        return view
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
