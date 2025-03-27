package com.example.ibookproject.repository

import android.net.Uri
import com.example.ibookproject.data.remote.ImageUploadService

class ImageUploadRepository {
    private val imageUploadService = ImageUploadService()

    //  Firebase Storage – העלאת תמונה
    fun uploadImage(imageUri: Uri, storagePath: String, onResult: (String?) -> Unit) {
        imageUploadService.uploadImage(imageUri, storagePath, onResult)
    }
}