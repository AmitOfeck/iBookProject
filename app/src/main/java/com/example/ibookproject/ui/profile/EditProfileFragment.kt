package com.example.ibookproject.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
import com.example.ibookproject.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        // טעינת הנתונים הנוכחיים מה-SharedPreferences או Firebase
        loadUserData()

        binding.saveButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val bio = binding.bioInput.text.toString().trim()
            val genres = getSelectedGenres()

            // שמירה של הנתונים
            saveUserData(name, bio, genres)

            Toast.makeText(requireContext(), "פרטים נשמרו!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editProfileFragment_to_userProfileFragment)
        }

        return binding.root
    }

    private fun loadUserData() {
        // טען את השם, הביו, והז'אנרים מה-SharedPreferences או Firebase
        val sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val name = sharedPref.getString("user_name", "Name") // דוגמה
        val bio = sharedPref.getString("user_bio", "Bio") // דוגמה
        // טוען את השם והביו לתוך השדות
        binding.nameInput.setText(name)
        binding.bioInput.setText(bio)

        // הז'אנרים יתממשו לפי הצורך
    }

    private fun saveUserData(name: String, bio: String, genres: String) {
        // שמור את הנתונים ב-SharedPreferences או Firebase
        val sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_name", name)
            putString("user_bio", bio)
            putString("user_genres", genres) // לאחסן את הז'אנרים
            apply()
        }

        // אם אתה רוצה לעדכן גם ב-Firebase
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.let {
            // עדכון בפרטי המשתמש ב-Firebase
            // למשל, משתמש ב-Firebase Realtime Database או Firestore לעדכון הפרופיל
        }
    }

    private fun getSelectedGenres(): String {
        val selectedGenres = mutableListOf<String>()
        if (binding.checkboxMystery.isChecked) selectedGenres.add("Mystery")
        if (binding.checkboxSciFi.isChecked) selectedGenres.add("Sci-Fi")
        if (binding.checkboxRomance.isChecked) selectedGenres.add("Romance")
        if (binding.checkboxFantasy.isChecked) selectedGenres.add("Fantasy")

        return selectedGenres.joinToString(",")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
