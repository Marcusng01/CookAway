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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookaway.R
import com.example.cookaway.R.drawable as AppIcon
import com.example.cookaway.R.string as AppText
import com.example.cookaway.common.composable.ActionToolbar
import com.example.cookaway.common.composable.PostCard
import com.example.cookaway.common.composable.PostScreenContents
import com.example.cookaway.common.ext.smallSpacer
import com.example.cookaway.common.ext.toolbarActions
import com.example.cookaway.model.Post
import com.example.cookaway.model.UserData
import com.example.cookaway.theme.MakeItSoTheme

@Composable
@ExperimentalMaterialApi
fun PostScreen(
  openScreen: (String) -> Unit,
  viewModel: TasksViewModel = hiltViewModel()
) {
//  val userData = viewModel.currentUserData.collectAsStateWithLifecycle(UserData())
  val selectedPost = listOf<Post>(viewModel.selectedPost.value)
  PostScreenContent(
    onSettingsClick = viewModel::onSettingsClick,
    openScreen = openScreen,
    post = selectedPost
  )

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun PostScreenContent(
  onSettingsClick: ((String) -> Unit) -> Unit,
  openScreen: (String) -> Unit,
  post: List<Post>,
) {
  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = Modifier.padding(16.dp)
      ) {
        Icon(Icons.Filled.Add, "Add")
      }
    }
  ) {
    Column(modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()) {
      ActionToolbar(
        title = AppText.tasks,
        modifier = Modifier.toolbarActions(),
        endActionIcon = AppIcon.ic_settings,
        endAction = { onSettingsClick(openScreen) }
      )

      LazyColumn {
        items(post, key = { it.id }) { postItem ->
          PostScreenContents(post = postItem)
        }
      }
    }
  }
}
