

package com.example.cookaway.model.service

import com.example.cookaway.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
  val currentUserId: String
  val currentUserEmail: String
  val hasUser: Boolean

  val currentUser: Flow<User>

  suspend fun authenticate(email: String, password: String)
  suspend fun sendRecoveryEmail(email: String)
  suspend fun createAnonymousAccount()
  suspend fun linkAccount(email: String, password: String)
  suspend fun deleteAccount()
  suspend fun signOut()

//   suspend fun createAccount(email: String, password: String)
}
