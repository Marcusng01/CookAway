

package com.example.cookaway.model.service

import com.example.cookaway.model.Post
import com.example.cookaway.model.UserData
import kotlinx.coroutines.flow.Flow

interface PostStorageService {
  val allPosts: Flow<List<Post>>
  val myPosts: Flow<List<Post>>
  val purchasedPosts: Flow<List<Post>>
  val currentUserData: Flow<UserData>

  suspend fun getPost(postId: String): Post?
  suspend fun save(post: Post): String
  suspend fun update(post: Post)
  suspend fun delete(postId: String)
}
