package com.example.ibookproject.ui.editBook

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.ui.bookDetails.BookDetailsViewModel
import kotlinx.coroutines.launch

class EditBookFragment : Fragment() {
    private lateinit var etTitle: EditText
    private lateinit var etAuthor: EditText
    private lateinit var etGenre: EditText
    private lateinit var etDescription: EditText
    private lateinit var coverImage: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var btnSave: Button
    private lateinit var bookDetails: BookEntity
    private var imageUri: Uri? = null

    private val editBookViewModel: EditBookViewModel by activityViewModels()
    private val bookDetailsViewModel: BookDetailsViewModel by activityViewModels()
    private var bookId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_book, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etAuthor = view.findViewById(R.id.etAuthor)
        etGenre = view.findViewById(R.id.etGenre)
        etDescription = view.findViewById(R.id.etDescription)
        coverImage = view.findViewById(R.id.eCoverImage)
        btnUploadImage = view.findViewById(R.id.uploadImageButton)
        btnSave = view.findViewById(R.id.btnSave)

        bookId = arguments?.getInt("bookId") ?: -1

        if (bookId == -1) {
            findNavController().popBackStack()
            return view
        }

        bookDetailsViewModel.getBookById(bookId).observe(viewLifecycleOwner) { book ->
            bookDetails = book
            etTitle.setText(book.title)
            etAuthor.setText(book.author)
            etGenre.setText(book.genre)
            etDescription.setText(book.description)

            imageUri = Uri.parse(book.coverImage)
            Glide.with(requireContext())
                .load(book.coverImage)
                .into(coverImage)
        }

        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                coverImage.setImageURI(uri)
            }
        }

        btnUploadImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            bookDetails.title = etTitle.text.toString().trim()
            bookDetails.author = etAuthor.text.toString().trim()
            bookDetails.genre = etGenre.text.toString().trim()
            bookDetails.description = etDescription.text.toString().trim()
            bookDetails.coverImage = imageUri.toString()

            lifecycleScope.launch {
                editBookViewModel.updateBook(bookDetails)
                findNavController().popBackStack()
            }
        }

        return view
    }
}
