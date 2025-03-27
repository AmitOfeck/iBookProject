package com.example.ibookproject.ui.editBook

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.ui.Genres
import com.example.ibookproject.ui.bookDetails.BookDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class EditBookFragment : Fragment() {
    private lateinit var etTitle: EditText
    private lateinit var etAuthor: EditText
    private lateinit var etDescription: EditText
    private lateinit var coverImage: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var btnSave: Button
    private lateinit var genreSpinner: Spinner
    private lateinit var bookDetails: BookEntity
    private var imageUri: Uri? = null
    private var bookId: Int = -1

    private val editBookViewModel: EditBookViewModel by activityViewModels()
    private val bookDetailsViewModel: BookDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_book, container, false)

        genreSpinner = view.findViewById(R.id.genreSpinner)
        etTitle = view.findViewById(R.id.etTitle)
        etAuthor = view.findViewById(R.id.etAuthor)
        etDescription = view.findViewById(R.id.etDescription)
        coverImage = view.findViewById(R.id.eCoverImage)
        btnUploadImage = view.findViewById(R.id.uploadImageButton)
        btnSave = view.findViewById(R.id.btnSave)

        // אתחול ה-Spinner עם רשימת הז'אנרים
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Genres.getAll())
        genreSpinner.adapter = adapter

        bookId = arguments?.getInt("bookId") ?: -1

        if (bookId == -1) {
            findNavController().popBackStack()
            return view
        }

        bookDetailsViewModel.getBookById(bookId).observe(viewLifecycleOwner) { book ->
            bookDetails = book
            etTitle.setText(book.title)
            etAuthor.setText(book.author)
            etDescription.setText(book.description)
            imageUri = Uri.parse(book.coverImage)

            if(!book.coverImage.isNullOrEmpty()) {
                Picasso.get()
                    .load(book.coverImage)
                    .placeholder(R.drawable.missing_book_cover)
                    .error(R.drawable.missing_book_cover)
                    .fit()
                    .centerCrop()
                    .into(coverImage)
            }
            else{
                Picasso.get()
                    .load(R.drawable.missing_book_cover)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .fit()
                    .centerCrop()
                    .into(coverImage)
            }

            // קביעת הז'אנר הנבחר
            val position = Genres.getAll().indexOf(book.genre)
            if (position != -1) {
                genreSpinner.setSelection(position)
            }
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
            val selectedGenre = genreSpinner.selectedItem.toString()
            bookDetails.title = etTitle.text.toString().trim()
            bookDetails.author = etAuthor.text.toString().trim()
            bookDetails.genre = selectedGenre
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
