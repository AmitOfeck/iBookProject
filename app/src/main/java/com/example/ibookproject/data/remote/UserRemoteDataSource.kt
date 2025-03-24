package com.example.ibookproject.data.remote

import com.example.ibookproject.data.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRemoteDataSource {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun saveUserProfileToFirestore(user: UserEntity, onResult: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(false)
        usersCollection.document(userId)
            .set(user)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUserProfileFromFirestore(userId: String, onResult: (UserEntity?) -> Unit) {
        usersCollection.document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(UserEntity::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
