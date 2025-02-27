package com.example.ibookproject.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ibookproject.databinding.FragmentProfileCreationBinding

class ProfileCreationFragment : Fragment() {
    private var _binding: FragmentProfileCreationBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null
    private val selectedGenres = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCreationBinding.inflate(inflater, container, false)

        // כפתור לבחירת תמונה
        binding.selectImageButton.setOnClickListener {
            openGallery()
        }

        // כפתור שמירה
        binding.saveButton.setOnClickListener {
            saveProfileData()
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                binding.profileImage.setImageURI(selectedImageUri)
            }
        }

    private fun saveProfileData() {
        val bioText = binding.bioInput.text.toString().trim()

        // בדיקת BIO
        if (bioText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a bio!", Toast.LENGTH_SHORT).show()
            return
        }

        // בדיקת ז'אנרים
        selectedGenres.clear()
        val checkboxes = listOf(
            binding.checkboxMystery, binding.checkboxSciFi,
            binding.checkboxRomance, binding.checkboxFantasy,
            binding.checkboxNonFiction
        )

        for (checkbox in checkboxes) {
            if (checkbox.isChecked) {
                selectedGenres.add(checkbox.text.toString())
            }
        }

        if (selectedGenres.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one genre!", Toast.LENGTH_SHORT).show()
            return
        }

        // הצלחה - נציג הודעה (בהמשך אפשר לשמור ל-ViewModel או Firebase)
        Toast.makeText(requireContext(), "Profile saved!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
