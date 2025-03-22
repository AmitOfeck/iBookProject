package com.example.ibookproject

import android.content.Context

class Utils {
    companion object {
        fun getUserId(requireContext: Context): String? {
                val sharedPref = requireContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
                return sharedPref.getString("user_id", null)
        }
    }
}