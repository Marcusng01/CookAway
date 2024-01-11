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

  override suspend fun topUp(balanceAmount: Double, topUpAmount: Double): Unit =
    trace(TOPUP_BALANCE_TRACE) {
      val docRef = firestore.collection(USERS_COLLECTION).document(auth.currentUserId)
      docRef.update(BALANCE_FIELD, balanceAmount + topUpAmount).await()
    }

  companion object {
    private const val USERS_COLLECTION = "users"
    private const val BALANCE_FIELD = "balance"
//    private const val PURCHASE_LIST_FIELD = "purchaseList"
    private const val TOPUP_BALANCE_TRACE = "topupBalance"
  }


}
