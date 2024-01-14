/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.cookaway.model.service.impl

import androidx.compose.runtime.MutableState
import com.example.cookaway.model.UserData
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.model.service.trace
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
  UserStorageService {
  override val currentUserData: Flow<UserData> get() = callbackFlow {
    val documentRef = firestore.collection(USERS_COLLECTION).document(auth.currentUserId)

    val listener = documentRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
      } else if (snapshot != null && snapshot.exists()) {
        val userData = snapshot.toObject<UserData>()
        trySend(userData ?: UserData())
      }
    }
    awaitClose { listener.remove() }
  }
  override suspend fun createUser(userId:String): Unit =
    trace(CREATE_USER_TRACE) {
      val emptyUser = mapOf("balance" to 0.0, "purchaseList" to ArrayList<String>())
      val docRef = firestore.collection(USERS_COLLECTION).document(userId)
      docRef.set(emptyUser).await()
    }

    override suspend fun topUp(topUpAmount: Double, userId:String ): Unit =
    trace(TOP_UP_BALANCE_TRACE) {
      val docRef = firestore.collection(USERS_COLLECTION).document(userId)
      docRef.update(BALANCE_FIELD, FieldValue.increment(topUpAmount)).await()
    }

  override suspend fun withdraw(deductAmount: Double, userId:String): Unit =
    trace(WITHDRAW_BALANCE_TRACE) {
      val docRef = firestore.collection(USERS_COLLECTION).document(userId)
      docRef.update(BALANCE_FIELD, FieldValue.increment(-deductAmount)).await()
    }

  override suspend fun updatePurchaseList(postID: String, userId: String):Unit =
    trace(PURCHASE_TRACE){
      val docRef = firestore.collection(USERS_COLLECTION).document(userId)
      docRef.get().addOnSuccessListener { document ->
        if (document != null) {
          val currentArray = document.get(PURCHASE_LIST_FIELD) as List<String>
          val newArray = currentArray + postID
          docRef.update(PURCHASE_LIST_FIELD, newArray)
        }
      }
    }
  companion object {
    private const val CREATE_USER_TRACE = "createUser"
    private const val USERS_COLLECTION = "users"
    private const val BALANCE_FIELD = "balance"
    private const val PURCHASE_LIST_FIELD = "purchaseList"
    private const val TOP_UP_BALANCE_TRACE = "topUpBalance"
    private const val WITHDRAW_BALANCE_TRACE = "withdrawBalance"
    private const val PURCHASE_TRACE = "purchase"
  }
}
