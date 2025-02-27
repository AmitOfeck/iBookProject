package com.example.ibookproject.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.databinding.FragmentLoginBinding
import com.example.ibookproject.R


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // כפתור ה-Login (כרגע לא עושה כלום)
        binding.loginButton.setOnClickListener {
            // TODO: נכניס לוגיקת התחברות בהמשך
        }

        // ניווט למסך ההרשמה
        binding.goToRegisterText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
