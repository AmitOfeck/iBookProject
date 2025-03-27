package com.example.ibookproject

import android.content.Context
import android.view.View
import android.widget.ProgressBar

class Utils {
    companion object {
        fun getUserId(requireContext: Context): String? {
            val sharedPref = requireContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            return sharedPref.getString("user_id", null)
        }

        fun manageLoadingSpinner(progressBar: ProgressBar, show: Boolean) {
            if (show) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}
