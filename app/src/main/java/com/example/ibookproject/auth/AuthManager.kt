package com.example.ibookproject.auth

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // רישום משתמש חדש
    fun signUp(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // התחברות משתמש קיים
    fun signIn(context: Context, email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        saveUserId(context, it) // שמירת מזהה המשתמש
                    }
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // התנתקות משתמש
    fun signOut(context: Context) {
        auth.signOut()
        clearUserId(context) // ניקוי המזהה מהזיכרון
    }

    // בדיקה אם המשתמש מחובר
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // שמירת מזהה המשתמש ב-SharedPreferences
    private fun saveUserId(context: Context, userId: String) {
        val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", userId)
            apply()
        }
    }

    // ניקוי מזהה המשתמש
    private fun clearUserId(context: Context) {
        val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("user_id")
            apply()
        }
    }
}
