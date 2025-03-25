package com.example.ibookproject.ui.bookDetails

import android.annotation.SuppressLint
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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.Utils
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.entities.RatingEntity
import com.example.ibookproject.ui.comment.CommentViewModel
import com.example.ibookproject.ui.rating.RatingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookDetailsFragment : Fragment() {

    private lateinit var tvBookTitle: TextView
    private lateinit var tvBookAuthor: TextView
    private lateinit var tvBookDescription: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var userRatingBar: RatingBar
    private lateinit var btnSubmitRating: Button
    private lateinit var tvComments: TextView
    private lateinit var etComment: EditText
    private lateinit var btnPostComment: Button
    private lateinit var ivBookCover: ImageView
    private var bookId: Int = -1
    private lateinit var userId: String
    private var userBookRating: RatingEntity? = null
    private lateinit var btnEditBook: Button
    private lateinit var btnDeleteBook: Button

    private val bookViewModel: BookDetailsViewModel by activityViewModels()
    private val ratingViewModel: RatingViewModel by activityViewModels()
    private val commentViewModel: CommentViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookId = arguments?.getInt("bookId") ?: -1
        userId = Utils.getUserId(requireContext()) ?: ""
        if (bookId == -1 || userId == "") {
            findNavController().navigate(R.id.loginFragment)
            return null
        }
        val view = inflater.inflate(R.layout.fragment_book_details, container, false)

        tvBookTitle = view.findViewById(R.id.tvBookTitle)
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor)
        tvBookDescription = view.findViewById(R.id.tvBookDescription)
        ratingBar = view.findViewById(R.id.ratingBar)
        userRatingBar = view.findViewById(R.id.userRatingBar)
        btnSubmitRating = view.findViewById(R.id.btnSubmitRating)
        tvComments = view.findViewById(R.id.tvComments)
        etComment = view.findViewById(R.id.etComment)
        btnPostComment = view.findViewById(R.id.btnPostComment)
        ivBookCover = view.findViewById(R.id.ivBookCover)
        btnEditBook = view.findViewById(R.id.btnEditBook)
        btnDeleteBook = view.findViewById(R.id.btnDeleteBook)

        ratingViewModel.getAverageRating(bookId).observe(viewLifecycleOwner) { avgRating ->
            ratingBar.rating = avgRating ?: 0f
        }

        ratingViewModel.getUserRatingForBook(userId, bookId).observe(viewLifecycleOwner) { userRating ->
            userRating?.let {
                userRatingBar.rating = it.rating
                userBookRating = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getCommentsForBook(bookId).collect { comments ->
                val commentList: List<CommentEntity> = comments
                tvComments.text = commentList.joinToString("\n") {
                    "${it.userId}: ${it.comment}"
                }
            }
        }

        setupListeners()

        return view
    }

    private fun setupListeners() {
        btnSubmitRating.setOnClickListener {
            val userRating = userRatingBar.rating

            val rating = RatingEntity(bookId = bookId, userId = userId, rating = userRating)
            if (userBookRating != null) {
                rating.id = userBookRating!!.id
                ratingViewModel.updateRating(rating)
            } else {
                ratingViewModel.addRating(rating)
            }
        }

        btnPostComment.setOnClickListener {
            val newComment = etComment.text.toString().trim()
            if (newComment.isNotEmpty()) {
                val comment = CommentEntity(bookId = bookId, userId = userId, comment = newComment)
                commentViewModel.addComment(comment)
                etComment.text.clear()
            }
        }

        btnEditBook.setOnClickListener {
            val bundle = Bundle().apply { putInt("bookId", bookId) }
            findNavController().navigate(R.id.action_bookDetailsFragment_to_editBookFragment, bundle)
        }

        btnDeleteBook.setOnClickListener {
            bookViewModel.deleteBookById(bookId)
            findNavController().navigateUp()
        }
    }
}
