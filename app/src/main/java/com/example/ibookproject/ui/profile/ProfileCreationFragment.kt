package com.example.ibookproject.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.databinding.FragmentProfileCreationBinding
import android.util.Log
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
            val selectedGenres = listOf("Fantasy", "Sci-Fi", "Mystery")
            val genresString = selectedGenres.joinToString(",")

            val imageUri = selectedImageUri?.toString() ?: ""

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "שם לא יכול להיות ריק", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val userId = firebaseUser?.uid ?: "UNKNOWN"

            Log.d("ProfileCreationFragment", "Firebase UID: $userId")

            val user = UserEntity(userId, name, bio, genresString, imageUri)
            userViewModel.insertUser(user)

            userViewModel.getUserById(userId).observe(viewLifecycleOwner) { savedUser ->
                if (savedUser != null) {
                    Log.d("ProfileCreationFragment", "User loaded: $savedUser")
                } else {
                    Log.e("ProfileCreationFragment", "User not found after saving")
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
                binding.profileImage.setImageURI(selectedImageUri)
            }
        }

    private fun getUserId(context: Context): String {
        val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return sharedPref.getString("user_id", "default_user_id") ?: "default_user_id"
    }
}
