package com.example.ibookproject.ui.userProfile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.ui.book.BooksAdapter
import com.example.ibookproject.ui.profile.UserViewModel

class UserProfileFragment : Fragment() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvUserBio: TextView
    private lateinit var tvCommentsCount: TextView
    private lateinit var tvUploadedBooks: TextView
    private lateinit var tvCommentsSection: TextView
    private lateinit var tvUploadedSection: TextView
    private lateinit var rvUserBooks: RecyclerView
    private lateinit var booksAdapter: BooksAdapter

    private val userProfileSearchView: UserProfileSearchView by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var uploadedBooks = emptyList<BookEntity>()
    private var commentedBooks = emptyList<BookEntity>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvUserBio = view.findViewById(R.id.tvUserBio)
        tvCommentsSection = view.findViewById(R.id.tvCommentsSection)
        tvUploadedSection = view.findViewById(R.id.tvUploadedSection)
        tvUploadedBooks = view.findViewById(R.id.tvUploadedBooks)
        tvCommentsCount = view.findViewById(R.id.tvCommentsCount)
        rvUserBooks = view.findViewById(R.id.rvUserBooks)

        val editProfileButton: Button = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_editProfileFragment)
        }


        rvUserBooks.layoutManager = LinearLayoutManager(requireContext())
        booksAdapter = BooksAdapter(uploadedBooks, { bookId ->
            val bundle = Bundle().apply {
                putInt("bookId", bookId)
            }
            findNavController().navigate(R.id.action_editProfileFragment_to_userProfileFragment)
        })
        rvUserBooks.adapter = booksAdapter

        tvCommentsSection.setOnClickListener {
            booksAdapter.updateBooks(commentedBooks)
            highlightSelectedTab(tvCommentsSection, tvUploadedSection)
        }

        tvUploadedSection.setOnClickListener {
            booksAdapter.updateBooks(uploadedBooks)
            highlightSelectedTab(tvUploadedSection, tvCommentsSection)
        }

        val userId = getUserId(requireContext())

        userViewModel.getUserById(userId!!).observe(viewLifecycleOwner) { user ->
            if (user != null) {
                tvUsername.text = user.name
                tvUserBio.text = user.bio

                Log.d("UserProfileFragment", "Profile Image URI: ${user.profileImage}")

                if (!user.profileImage.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(user.profileImage)
                        .into(ivProfilePicture)
                }
            }
        }

        loadUserData(userId)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun highlightSelectedTab(selected: TextView, unselected: TextView) {
        selected.setTextColor(resources.getColor(R.color.black, null))
        unselected.setTextColor(resources.getColor(R.color.gray, null))
    }

    private fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return sharedPref.getString("user_id", null)
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData(userId: String) {
        userProfileSearchView.getBooksByUser(userId).observe(viewLifecycleOwner) { books ->
            uploadedBooks = books.toMutableList()
            tvUploadedBooks.text = uploadedBooks.size.toString()
            booksAdapter.updateBooks(uploadedBooks)
        }

        userProfileSearchView.getCommentsByUser(userId).observe(viewLifecycleOwner) { comments ->
            val bookIds = comments.map { it.bookId }
            userProfileSearchView.getBooksById(bookIds).observe(viewLifecycleOwner) { books ->
                commentedBooks = books.toMutableList()
                tvCommentsCount.text = commentedBooks.size.toString()
            }
        }
    }
}
