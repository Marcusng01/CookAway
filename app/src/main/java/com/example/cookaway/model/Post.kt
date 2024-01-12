package com.example.cookaway.model

import com.google.firebase.firestore.DocumentId

data class Post(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val price: Double = 0.0,
)
