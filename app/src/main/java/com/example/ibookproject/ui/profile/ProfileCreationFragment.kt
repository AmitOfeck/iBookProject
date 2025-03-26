package com.example.ibookproject.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.databinding.FragmentProfileCreationBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileCreationFragment : Fragment() {
    private var _binding: FragmentProfileCreationBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCreationBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.profileImage.setOnClickListener { openGallery() }
        binding.selectImageButton.setOnClickListener { openGallery() }

        binding.saveButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val bio = binding.bioInput.text.toString().trim()

            val selectedGenres = mutableListOf<String>()
            if (binding.checkboxMystery.isChecked) selectedGenres.add("Mystery")
            if (binding.checkboxSciFi.isChecked) selectedGenres.add("Science Fiction")
            if (binding.checkboxRomance.isChecked) selectedGenres.add("Romance")
            if (binding.checkboxFantasy.isChecked) selectedGenres.add("Fantasy")
            if (binding.checkboxHorror.isChecked) selectedGenres.add("Horror")
            if (binding.checkboxThriller.isChecked) selectedGenres.add("Thriller")

            val genresString = selectedGenres.joinToString(",")

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "שם לא יכול להיות ריק", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            if (selectedImageUri != null) {
                val path = "images/profile/$userId.jpg"
                userViewModel.uploadImage(selectedImageUri!!, path) { imageUrl ->
                    if (imageUrl != null) {
                        saveUserProfile(userId, name, bio, genresString, imageUrl)
                    } else {
                        Toast.makeText(requireContext(), "שגיאה בהעלאת תמונה", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                saveUserProfile(userId, name, bio, genresString, "")
            }
        }

        return binding.root
    }

    private fun saveUserProfile(userId: String, name: String, bio: String, genres: String, imageUrl: String) {
        val user = UserEntity(userId, name, bio, genres, imageUrl)

        userViewModel.insertUser(user)
        userViewModel.saveUserToRemote(user) { success ->
            if (success) {
                Log.d("ProfileCreation", "User saved to Firestore")
                Toast.makeText(requireContext(), "פרופיל נשמר!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_profileCreationFragment_to_loginFragment)
            } else {
                Toast.makeText(requireContext(), "שגיאה בשמירת פרופיל", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                Picasso.get()
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.profileImage)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
