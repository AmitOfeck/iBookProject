package com.example.ibookproject.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class ImageUploadService {

    private val storageRef = FirebaseStorage.getInstance().reference

    /**
     * Uploads an image to Firebase Storage under the given path and returns its download URL.
     *
     * @param imageUri URI of the image to upload.
     * @param storagePath Path in Firebase Storage to save the image, e.g., "images/profile/userId.jpg"
     * @param onResult Callback with the download URL string or null on failure.
     */
    fun uploadImage(imageUri: Uri, storagePath: String, onResult: (String?) -> Unit) {
        val fileRef = storageRef.child(storagePath)

        fileRef.putFile(imageUri)
            .addOnSuccessListener {
                fileRef.downloadUrl
                    .addOnSuccessListener { uri -> onResult(uri.toString()) }
                    .addOnFailureListener { onResult(null) }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
