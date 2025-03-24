package com.example.ibookproject.ui.profile

import android.app.Activity
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ibookproject.R
import com.example.ibookproject.data.entities.UserEntity
import com.example.ibookproject.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private lateinit var userViewModel: UserViewModel
    private var currentUser: UserEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        loadUserData()

        binding.profileImage.setOnClickListener { openGallery() }
        binding.selectImageButton.setOnClickListener { openGallery() }

        binding.saveButton.setOnClickListener {
            saveUserData()
        }

        return binding.root
    }

    private fun loadUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        userViewModel.getUserById(userId).observe(viewLifecycleOwner) { localUser ->
            if (localUser != null) {
                currentUser = localUser
                updateUI(localUser)
            } else {
                userViewModel.fetchUserFromRemoteAndCache(userId)
            }
        }

        userViewModel.userLiveData.observe(viewLifecycleOwner) { remoteUser ->
            remoteUser?.let {
                currentUser = it
                updateUI(it)
            }
        }
    }

    private fun updateUI(user: UserEntity) {
        binding.nameInput.setText(user.name)
        binding.bioInput.setText(user.bio)
        setSelectedGenres(user.favoriteGenres)

        if (!user.profileImage.isNullOrEmpty()) {
            Glide.with(requireContext()).load(user.profileImage).into(binding.profileImage)
        }
    }

    private fun setSelectedGenres(genres: String) {
        val selectedGenres = genres.split(",").map { it.trim() }
        binding.checkboxMystery.isChecked = "Mystery" in selectedGenres
        binding.checkboxSciFi.isChecked = "Science Fiction" in selectedGenres
        binding.checkboxRomance.isChecked = "Romance" in selectedGenres
        binding.checkboxFantasy.isChecked = "Fantasy" in selectedGenres
        binding.checkboxHorror.isChecked = "Horror" in selectedGenres
        binding.checkboxThriller.isChecked = "Thriller" in selectedGenres
    }

    private fun getSelectedGenres(): String {
        val selectedGenres = mutableListOf<String>()
        if (binding.checkboxMystery.isChecked) selectedGenres.add("Mystery")
        if (binding.checkboxSciFi.isChecked) selectedGenres.add("Science Fiction")
        if (binding.checkboxRomance.isChecked) selectedGenres.add("Romance")
        if (binding.checkboxFantasy.isChecked) selectedGenres.add("Fantasy")
        if (binding.checkboxHorror.isChecked) selectedGenres.add("Horror")
        if (binding.checkboxThriller.isChecked) selectedGenres.add("Thriller")
        return selectedGenres.joinToString(",")
    }

    private fun saveUserData() {
        val name = binding.nameInput.text.toString().trim()
        val bio = binding.bioInput.text.toString().trim()
        val genres = getSelectedGenres()
        val imageUri = selectedImageUri?.toString() ?: currentUser?.profileImage ?: ""

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "שם לא יכול להיות ריק", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = currentUser?.copy(
            name = name,
            bio = bio,
            favoriteGenres = genres,
            profileImage = imageUri
        ) ?: return

        lifecycleScope.launch {
            userViewModel.updateUser(updatedUser)
        }

        userViewModel.saveUserToRemote(updatedUser) { success ->
            if (!success) {
                Toast.makeText(requireContext(), "שגיאה בשמירה לשרת", Toast.LENGTH_SHORT).show()
            }
        }

        Toast.makeText(requireContext(), "פרטים נשמרו!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_editProfileFragment_to_userProfileFragment)
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
