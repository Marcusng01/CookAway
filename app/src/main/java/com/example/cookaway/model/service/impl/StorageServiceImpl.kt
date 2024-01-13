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

import com.example.cookaway.model.Post
import com.example.cookaway.model.Task
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.StorageService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService, private val userStorageService: UserStorageService) :
  StorageService {

  @OptIn(ExperimentalCoroutinesApi::class)
  override val tasks: Flow<List<Task>>
    get() =
      auth.currentUser.flatMapLatest { user ->
        firestore.collection(TASK_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
      }

  override suspend fun getTask(taskId: String): Task? =
    firestore.collection(TASK_COLLECTION).document(taskId).get().await().toObject()

  override suspend fun save(post: Post): String =
    trace(SAVE_TASK_TRACE) {
      var userId = auth.currentUserId
      val taskWithUserId = post.copy(userId = userId)
      var postId = firestore.collection(POST_COLLECTION).add(taskWithUserId).await().id
      userStorageService.updatePurchaseList(postId, userId)
      userId
    }

  override suspend fun update(post: Post): Unit =
    trace(UPDATE_TASK_TRACE) {
      firestore.collection(POST_COLLECTION).document(post.id).set(post).await()
    }

//  override suspend fun save(task: Task): String =
//    trace(SAVE_TASK_TRACE) {
//      val taskWithUserId = task.copy(userId = auth.currentUserId)
//      firestore.collection(TASK_COLLECTION).add(taskWithUserId).await().id
//    }
//
//  override suspend fun update(task: Task): Unit =
//    trace(UPDATE_TASK_TRACE) {
//      firestore.collection(TASK_COLLECTION).document(task.id).set(task).await()
//    }

  override suspend fun delete(taskId: String) {
    firestore.collection(TASK_COLLECTION).document(taskId).delete().await()
  }


  companion object {
    private const val USER_ID_FIELD = "userId"
    private const val TASK_COLLECTION = "tasks"
    private const val POST_COLLECTION = "posts"
    private const val SAVE_TASK_TRACE = "saveTask"
    private const val UPDATE_TASK_TRACE = "updateTask"
  }
}
