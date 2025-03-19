package com.example.ibookproject.ui.auth

import android.content.Context
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
import com.example.ibookproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.util.Log


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text?.toString()?.trim() ?: ""
            val password = binding.passwordInput.text?.toString()?.trim() ?: ""

            if (!isValidEmail(email)) {
                Toast.makeText(requireContext(), "כתובת אימייל לא תקינה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "נא להזין סיסמה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.loginButton.isEnabled = false

            // התחברות המשתמש
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    binding.loginButton.isEnabled = true

                    if (task.isSuccessful) {
                        // שמירת ה- userId אחרי התחברות מוצלחת
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        firebaseUser?.let {
                            saveUserId(requireContext(), it.uid)
                        }

                        activity?.invalidateOptionsMenu()

                        Toast.makeText(requireContext(), "התחברת בהצלחה!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    } else {
                        Toast.makeText(requireContext(), "שגיאה: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // מעבר למסך הרשמה
        binding.goToRegisterText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        return binding.root
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // שמירת ה- userId ב- SharedPreferences
    private fun saveUserId(context: Context, userId: String) {
        val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", userId)
            apply()
        }
        Log.d("LoginFragment", "User ID saved: $userId")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
