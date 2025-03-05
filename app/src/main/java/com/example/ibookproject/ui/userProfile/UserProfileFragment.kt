package com.example.ibookproject.ui.userProfile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ibookproject.R
import com.example.ibookproject.ui.book.Book
import com.example.ibookproject.ui.book.BooksAdapter

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

    private val uploadedBooks = listOf(
        Book("The Great", "John Smith", "Adventure", 4, R.drawable.img),
        Book("Mystery at the", "Emily Clark", "Mystery",4, R.drawable.img),
        Book("Science and", "Dr. Alice Wong", "Non-Fiction",4, R.drawable.img)
    )

    private val commentedBooks = listOf(
        Book("Historical Tales", "Michael Brown", "History", 4,R.drawable.img),
        Book("Fantasy Realm", "Laura Green", "Fantasy", 4,R.drawable.img)
    )

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
        tvUploadedBooks = view.findViewById(R.id.tvUploadedBooks)
        rvUserBooks = view.findViewById(R.id.rvUserBooks)

        // הגדרת רשימת הספרים
        rvUserBooks.layoutManager = LinearLayoutManager(requireContext())
        booksAdapter = BooksAdapter(uploadedBooks) // ברירת מחדל: מציגים ספרים שהועלו
        rvUserBooks.adapter = booksAdapter

        // מאזינים ללחיצה על כפתורי הסטטיסטיקה
        tvCommentsSection.setOnClickListener {
            booksAdapter.updateBooks(commentedBooks)
            highlightSelectedTab(tvCommentsSection, tvUploadedSection)
        }

        tvUploadedSection.setOnClickListener {
            booksAdapter.updateBooks(uploadedBooks)
            highlightSelectedTab(tvUploadedSection, tvCommentsSection)

        }

        // טעינת מידע
        loadUserData()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun highlightSelectedTab(selected: TextView, unselected: TextView) {
        selected.setTextColor(resources.getColor(R.color.black, null))
        unselected.setTextColor(resources.getColor(R.color.gray, null))
    }

    private fun loadUserData() {
        tvUsername.text = "BookRate User"
        tvUserBio.text = "Find me on BookRate"
        tvCommentsCount.text = "12"
        tvUploadedBooks.text = "3"
    }
}

