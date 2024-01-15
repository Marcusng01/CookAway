

package com.example.cookaway.model.service

import android.content.Context
import androidx.compose.runtime.MutableState
import com.example.cookaway.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserStorageService {
  val currentUserData: Flow<UserData>
  suspend fun topUp(topUpAmount:Double , userId: String)
  suspend fun withdraw(topUpAmount: Double, userId: String, context: Context, popUpScreen: () -> Unit)
  suspend fun deductMoney(deductAmount: Double, userId:String): Unit

  //  suspend fun getUserData(): UserData?
  suspend fun createUser(userId:String)

  suspend fun updatePurchaseList(postID: String, userId: String)
}
