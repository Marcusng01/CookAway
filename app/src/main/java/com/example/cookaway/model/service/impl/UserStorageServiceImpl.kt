

package com.example.cookaway.model.service.impl

import android.app.AlertDialog
import android.content.Context
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

  override suspend fun deductMoney(deductAmount: Double, userId:String): Unit =
    trace(WITHDRAW_BALANCE_TRACE) {
      val docRef = firestore.collection(USERS_COLLECTION).document(userId)
      docRef.update(BALANCE_FIELD, FieldValue.increment(-deductAmount)).await()
    }

  override suspend fun withdraw(deductAmount: Double, userId: String, context: Context, popUpScreen: () -> Unit): Unit =
    trace(WITHDRAW_BALANCE_TRACE) {
      val docRef = firestore.collection(USERS_COLLECTION).document(userId)
      val snapshot = docRef.get().await()
      val currentBalance = snapshot[BALANCE_FIELD] as? Double ?: 0.0
      if (currentBalance - deductAmount < 0) {
        AlertDialog.Builder(context)
          .setTitle("Error")
          .setMessage("Withdrawal amount exceeds the current balance.")
          .setPositiveButton("OK", null)
          .show()
      } else {
        docRef.update(BALANCE_FIELD, FieldValue.increment(-deductAmount)).await()
        popUpScreen()
      }
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
