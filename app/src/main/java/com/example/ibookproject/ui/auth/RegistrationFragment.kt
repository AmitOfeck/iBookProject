package com.example.ibookproject.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.R
import com.example.ibookproject.auth.AuthManager
import com.example.ibookproject.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        // לחיצה על כפתור הרשמה
        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.text?.toString()?.trim() ?: ""
            val password = binding.passwordInput.text?.toString()?.trim() ?: ""
            val confirmPassword = binding.confirmPasswordInput.text?.toString()?.trim() ?: ""

            // בדיקות תקינות
            if (!isValidEmail(email)) {
                Toast.makeText(requireContext(), "כתובת אימייל לא תקינה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(requireContext(), "סיסמה חייבת להכיל לפחות 6 תווים", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "הסיסמאות אינן תואמות", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // רישום המשתמש
            AuthManager.signUp(email, password) { success, errorMessage ->
                if (success) {
                    Toast.makeText(requireContext(), "נרשמת בהצלחה! המשך ביצירת פרופיל", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_profileCreationFragment)
                } else {
                    Toast.makeText(requireContext(), "שגיאה בהרשמה: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }

        // מעבר למסך ההתחברות אם יש כבר משתמש
        binding.goToLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
