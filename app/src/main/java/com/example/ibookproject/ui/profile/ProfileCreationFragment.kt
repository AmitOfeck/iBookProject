package com.example.ibookproject.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ibookproject.databinding.FragmentProfileCreationBinding

class ProfileCreationFragment : Fragment() {
    private var _binding: FragmentProfileCreationBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCreationBinding.inflate(inflater, container, false)

        // לחיצה על תמונת הפרופיל תפתח את הגלריה
        binding.profileImage.setOnClickListener {
            openGallery()
        }

        // גם כפתור "Upload Picture" יאפשר בחירת תמונה
        binding.selectImageButton.setOnClickListener {
            openGallery()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
