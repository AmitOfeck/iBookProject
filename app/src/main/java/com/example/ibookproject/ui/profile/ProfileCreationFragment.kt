package com.example.ibookproject.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.databinding.FragmentProfileCreationBinding
import com.google.firebase.auth.FirebaseAuth

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
            val imageUri = selectedImageUri?.toString() ?: ""

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "שם לא יכול להיות ריק", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val userId = firebaseUser?.uid ?: "UNKNOWN"

            val user = UserEntity(userId, name, bio, genresString, imageUri)

            userViewModel.insertUser(user)

            userViewModel.saveUserToRemote(user) { success ->
                if (success) {
                    Log.d("ProfileCreation", "User saved to Firestore")
                } else {
                    Log.e("ProfileCreation", "Failed to save user to Firestore")
                }
            }

            Toast.makeText(requireContext(), "פרופיל נשמר!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileCreationFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(binding.profileImage)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
