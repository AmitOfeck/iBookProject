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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.Utils
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.entities.RatingEntity
import com.example.ibookproject.ui.comment.CommentViewModel
import com.example.ibookproject.ui.profile.UserViewModel
import com.example.ibookproject.ui.rating.RatingViewModel

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
    private val userViewModel: UserViewModel by activityViewModels()

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

        ratingViewModel.getUserRatingForBook(userId,bookId).observe(viewLifecycleOwner) { userRating ->
            userRating?.let {
                userRatingBar.rating = it.rating
                userBookRating = it
            }
        }

        commentViewModel.getCommentsForBook(bookId).observe(viewLifecycleOwner) { comments ->
            val userNamesMap = mutableMapOf<String, String>()

            comments.forEach { comment ->
                userViewModel.getUserById(comment.userId).observe(viewLifecycleOwner) { user ->
                    userNamesMap[comment.userId] = user?.name ?: "unknown user"
                    tvComments.text = comments.joinToString("\n") {
                        "${userNamesMap[it.userId] ?: "loading..."}: ${it.comment}"
                    }
                }
            }
        }

        setupListeners()

        bookId.let { bookId ->
            bookViewModel.getBookById(bookId).observe(viewLifecycleOwner) { book ->
                val comments = emptyList<String>()

                tvBookTitle.text = book.title
                tvBookAuthor.text = "by ${book.author}"
                tvBookDescription.text = "description ${book.description}"
                ratingBar.rating = book.rating
                tvComments.text = comments.joinToString("\n")

                if (userId == book.uploadingUserId) {
                    btnEditBook.visibility = View.VISIBLE
                    btnDeleteBook.visibility = View.VISIBLE
                } else {
                    btnEditBook.visibility = View.GONE
                    btnDeleteBook.visibility = View.GONE
                }

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

            val rating = RatingEntity(bookId = bookId, userId = userId, rating = userRating)
            if (userBookRating != null){
                rating.id = userBookRating!!.id
                ratingViewModel.updateRating(rating)
            }
            else{
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
