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
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
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

        // לחיצה על תמונת הפרופיל או הכפתור לבחירת תמונה
        binding.profileImage.setOnClickListener { openGallery() }
        binding.selectImageButton.setOnClickListener { openGallery() }

        // לחיצה על "שמירה" - כרגע רק ניווט (בעתיד ניתן לשמור נתונים)
        binding.saveButton.setOnClickListener {
            Toast.makeText(requireContext(), "פרופיל נשמר בהצלחה!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileCreationFragment_to_loginFragment)
        }

        // לחיצה על "דילוג"
        binding.skipText.setOnClickListener {
            findNavController().navigate(R.id.action_profileCreationFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
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
