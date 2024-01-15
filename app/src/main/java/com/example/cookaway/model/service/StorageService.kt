

package com.example.cookaway.model.service

import com.example.cookaway.model.Post
import com.example.cookaway.model.Task
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val tasks: Flow<List<Task>>

  suspend fun getTask(taskId: String): Task?
  suspend fun save(post: Post): String
  suspend fun update(post: Post)
  suspend fun delete(taskId: String)
}
