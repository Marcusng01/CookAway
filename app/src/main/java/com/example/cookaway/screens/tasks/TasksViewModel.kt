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

package com.example.cookaway.screens.tasks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.cookaway.CREATE_POST_SCREEN
import com.example.cookaway.POST_ID
import com.example.cookaway.POST_SCREEN
import com.example.cookaway.SETTINGS_SCREEN
import com.example.cookaway.common.ext.idFromParameter
import com.example.cookaway.model.Post
import com.example.cookaway.model.UserData
import com.example.cookaway.model.service.ConfigurationService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.PostStorageService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.cookaway.R.drawable as AppIcon

@HiltViewModel
class TasksViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  logService: LogService,
  private val postStorageService: PostStorageService,
  private val userStorageService: UserStorageService,
) : MakeItSoViewModel(logService) {
  var posts = mutableStateOf(postStorageService.allPosts)
  val currentUserData = postStorageService.currentUserData
  val selectedPost = mutableStateOf(Post())

  val tabHeaders = listOf("All","Owned","My Recipes")
  var iconList = listOf(AppIcon.ic_all_recipes,AppIcon.ic_owned,AppIcon.ic_my_recipes)
  var currentTab = mutableStateOf("All")

  init {
    val selectedPostId = savedStateHandle.get<String>(POST_ID)
    if (selectedPostId != null) {
      launchCatching {
        selectedPost.value = postStorageService.getPost(selectedPostId.idFromParameter()) ?: Post()
      }
    }
  }

  fun onTabSelected(selectedTab: String){
    if (selectedTab == "All"){
      posts.value = postStorageService.allPosts
      currentTab.value = selectedTab
    }
    else if (selectedTab == "Owned"){
      posts.value = postStorageService.purchasedPosts
      currentTab.value = selectedTab
    }
    else if (selectedTab == "My Recipes"){
      posts.value = postStorageService.myPosts
      currentTab.value = selectedTab
    }

  }

  fun onViewClick(openScreen: (String) -> Unit, post: Post){
    openScreen("$POST_SCREEN?$POST_ID={${post.id}}")
  }
  fun onCreateClick(openScreen: (String) -> Unit) = openScreen(CREATE_POST_SCREEN)
  fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)
  fun onBuyClick(post: Post, currentUserData: UserData) {
    var price = post.price
    var postId= post.id
    var buyerId = currentUserData.id
    var sellerId = post.userId
    launchCatching {
      userStorageService.topUp(price, sellerId)
      userStorageService.withdraw(price, buyerId)
      userStorageService.updatePurchaseList(postId, buyerId)
    }
  }

  companion object {
    private const val PURCHASE_TRACE = "purchase"
  }
}
