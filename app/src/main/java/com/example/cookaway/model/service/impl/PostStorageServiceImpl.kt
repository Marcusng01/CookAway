

package com.example.cookaway.model.service.impl

import androidx.compose.runtime.mutableStateOf
import com.example.cookaway.SETTINGS_SCREEN
import com.example.cookaway.model.Post
import com.example.cookaway.model.Task
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.PostStorageService
import com.example.cookaway.model.service.StorageService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.model.service.trace
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import java.util.stream.Collectors.toList

class PostStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService, private val userData: UserStorageService) :
  PostStorageService {
  override val currentUserData = userData.currentUserData

  @OptIn(ExperimentalCoroutinesApi::class)
  override val allPosts: Flow<List<Post>>
    get() =
      auth.currentUser.flatMapLatest {
        firestore.collection(POST_COLLECTION).dataObjects()
      }
  @OptIn(ExperimentalCoroutinesApi::class)
  override val myPosts: Flow<List<Post>>
    get() =
      auth.currentUser.flatMapLatest { user ->
        firestore.collection(POST_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
      }
  @OptIn(ExperimentalCoroutinesApi::class)
  override val purchasedPosts: Flow<List<Post>>
    get() = userData.currentUserData.flatMapLatest { userData ->
      if(userData.purchaseList.isNotEmpty()) {
        firestore.collection(POST_COLLECTION).whereIn(FieldPath.documentId(), userData.purchaseList).dataObjects()
      } else {
        flowOf(emptyList<Post>()) // return an empty list
      }
    }
  override suspend fun getPost(postId: String): Post? =
    firestore.collection(POST_COLLECTION).document(postId).get().await().toObject()

  override suspend fun save(post: Post): String =
    trace(SAVE_POST_TRACE) {
      val postWithUserId = post.copy(userId = auth.currentUserId)
      firestore.collection(POST_COLLECTION).add(postWithUserId).await().id
    }

  override suspend fun update(post: Post): Unit =
    trace(UPDATE_POST_TRACE) {
      firestore.collection(POST_COLLECTION).document(post.id).set(post).await()
    }

  override suspend fun delete(postId: String) {
    firestore.collection(POST_COLLECTION).document(postId).delete().await()
  }
  companion object {
    private const val USER_ID_FIELD = "userId"
    private const val POST_COLLECTION = "posts"
    private const val SAVE_POST_TRACE = "saveTask"
    private const val UPDATE_POST_TRACE = "updateTask"
  }
}
