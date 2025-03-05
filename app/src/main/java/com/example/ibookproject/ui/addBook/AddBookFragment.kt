package com.example.ibookproject.ui.addBook

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ibookproject.databinding.FragmentAddBookBinding

class AddBookFragment : Fragment() {
    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.submitButton.setOnClickListener {
            val title = binding.bookTitleInput.text.toString().trim()
            val author = binding.authorInput.text.toString().trim()
            val genre = binding.genreInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim()
            val rating = binding.ratingBar.rating

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "נא למלא את כל השדות", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri == null) {
                Toast.makeText(requireContext(), "נא להעלות תמונה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // כאן ניתן להוסיף את הלוגיקה לשמירת הספר במסד נתונים או ב-Firebase
            Toast.makeText(requireContext(), "הספר נוסף בהצלחה!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
