package com.example.ibookproject.data.remote

import com.example.ibookproject.data.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    fun getUserProfileFromFirestore(userId: String): Flow<UserEntity?> = callbackFlow {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")

        // Firestore call to fetch user data
        val documentRef = usersCollection.document(userId)

        // שמירה על ה-ListenerRegistration
        val listenerRegistration = documentRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                trySend(null) // Emit null if there is an error
            } else {
                val user = documentSnapshot?.toObject(UserEntity::class.java)
                trySend(user) // Emit the user object to the flow
            }
        }

        // Make sure to clean up the listener when the flow is collected
        awaitClose {
            listenerRegistration.remove() // Remove the listener when the flow is closed
        }
    }
}
