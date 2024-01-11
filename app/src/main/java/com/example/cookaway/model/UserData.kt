package com.example.cookaway.model

import com.google.firebase.firestore.DocumentId

data class UserData (
    @DocumentId val id: String = "",
    val balance: Double = 0.0,
    val purchaseList: ArrayList<String> = ArrayList<String>(),
)