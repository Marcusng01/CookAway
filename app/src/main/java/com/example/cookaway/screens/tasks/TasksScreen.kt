

package com.example.cookaway.screens.tasks

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookaway.R.drawable as AppIcon
import com.example.cookaway.R.string as AppText
import com.example.cookaway.common.composable.ActionToolbar
import com.example.cookaway.common.composable.CookAwayTabRow
import com.example.cookaway.common.composable.PostCard
import com.example.cookaway.common.ext.smallSpacer
import com.example.cookaway.common.ext.toolbarActions
import com.example.cookaway.model.Post
import com.example.cookaway.model.UserData

@Composable
@ExperimentalMaterialApi
fun TasksScreen(
  openScreen: (String) -> Unit,
  viewModel: TasksViewModel = hiltViewModel()
) {
  val userData = viewModel.currentUserData.collectAsStateWithLifecycle(UserData())
  val posts = viewModel
    .posts
    .value
    .collectAsStateWithLifecycle(emptyList())

  TasksScreenContent(
    onSettingsClick = viewModel::onSettingsClick,
    onViewClick = viewModel::onViewClick,
    onCreateClick = viewModel::onCreateClick,
    openScreen = openScreen,
    posts = posts.value,
    onBuyClick = viewModel::onBuyClick,
    currentUserData = userData.value,
    tabHeaders = viewModel.tabHeaders,
    onTabSelected = viewModel::onTabSelected,
    currentTab = viewModel.currentTab.value,
    iconList =  viewModel.iconList
  )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TasksScreenContent(
  openScreen: (String) -> Unit,
  onSettingsClick: ((String) -> Unit) -> Unit,
  onCreateClick: ((String) -> Unit) -> Unit,
  onViewClick: ((String) -> Unit, Post) -> Unit,
  onBuyClick: (Post,UserData) -> Unit,
  posts: List<Post>,
  currentUserData: UserData,
  tabHeaders: List<String>,
  onTabSelected: (String) -> Unit,
  currentTab: String,
  @DrawableRes iconList: List<Int>
) {
  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onCreateClick(openScreen)},
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = Modifier.padding(16.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ){
          Text(stringResource(AppText.create))
          Icon(Icons.Filled.Add, "Add")
        }
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
      CookAwayTabRow(
        tabHeaders = tabHeaders,
        onTabSelected = onTabSelected,
        currentTab = currentTab,
        iconList = iconList)
      LazyColumn {
        items(posts, key = { it.id }) { postItem ->
          val isInPurchaseList = postItem.id in currentUserData.purchaseList
          PostCard(
            post = postItem,
            bought = isInPurchaseList,
            onBuyClick = onBuyClick,
            currentUserData = currentUserData,
            onViewClick = onViewClick,
            openScreen = openScreen
            )
          Spacer(modifier = Modifier.smallSpacer())
        }
      }
    }
  }
}
