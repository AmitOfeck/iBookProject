package com.example.ibookproject.ui.addBook

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
import com.example.ibookproject.Utils
import com.example.ibookproject.data.entities.BookEntity
import com.example.ibookproject.data.entities.CommentEntity
import com.example.ibookproject.data.entities.RatingEntity
import com.example.ibookproject.databinding.FragmentAddBookBinding
import com.example.ibookproject.ui.Genres
import com.example.ibookproject.ui.comment.CommentViewModel
import com.example.ibookproject.ui.rating.RatingViewModel

class AddBookFragment : Fragment() {
    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    private val addBookViewModel: AddBookViewModel by activityViewModels()
    private val ratingViewModel: RatingViewModel by activityViewModels()
    private val commentViewModel: CommentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ScrollView? {
        val userId = Utils.getUserId(requireContext()) ?: ""
        if (userId == "") {
            findNavController().navigate(R.id.loginFragment)
            return null
        }
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)

        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.coverImage.setImageURI(uri)
            }
        }

        binding.uploadImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Genres.getAll())
        binding.genreSpinner.adapter = adapter

        binding.submitButton.setOnClickListener {
            val selectedGenre = binding.genreSpinner.selectedItem.toString()
            val title = binding.bookTitleInput.text.toString().trim()
            val author = binding.authorInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim()
            val rating = binding.ratingBar.rating
            val comment = binding.commentsInput.text.toString().trim()

            if (title.isEmpty() || author.isEmpty() || selectedGenre.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "נא למלא את כל השדות", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri == null) {
                Toast.makeText(requireContext(), "נא להעלות תמונה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newBook = BookEntity(
                uploadingUserId = userId,
                title = title,
                author = author,
                description = description,
                genre = selectedGenre,
                rating = rating,
                coverImage = imageUri.toString()
            )

            addBookViewModel.addBook(newBook)

            Toast.makeText(requireContext(), "הספר נוסף בהצלחה!", Toast.LENGTH_SHORT).show()

            // ניקוי השדות אחרי הוספה
            binding.bookTitleInput.text.clear()
            binding.authorInput.text.clear()
            binding.descriptionInput.text.clear()
            binding.ratingBar.rating = 0f

            addBookViewModel.bookId.observe(viewLifecycleOwner) { bookId ->
                bookId?.let {
                    if (rating > 0) {
                        val ratingEntity = RatingEntity(bookId = bookId, userId = userId, rating = rating)
                        ratingViewModel.addRating(ratingEntity)
                    }

                    if (comment.isNotEmpty()) {
                        val commentEntity = CommentEntity(bookId = bookId, userId = userId, comment = comment)
                        commentViewModel.addComment(commentEntity)
                    }

                    val bundle = Bundle().apply {
                        putInt("bookId", bookId)
                    }
                    findNavController().navigate(R.id.action_addBookFragment_to_bookDetailsFragment, bundle)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
