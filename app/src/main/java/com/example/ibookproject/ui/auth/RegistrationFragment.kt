package com.example.ibookproject.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ibookproject.databinding.FragmentRegistrationBinding
import com.example.ibookproject.R


class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        // כרגע כפתור ה-Register לא עושה כלום (נוסיף לוגיקת הרשמה בהמשך)
        binding.registerButton.setOnClickListener {
            // TODO: נכניס לוגיקת הרשמה בהמשך
        }

        // ניווט חזרה למסך ה-Login
        binding.goToLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
