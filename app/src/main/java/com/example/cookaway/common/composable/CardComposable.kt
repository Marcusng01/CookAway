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

package com.example.cookaway.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cookaway.R
import com.example.cookaway.common.ext.card
import com.example.cookaway.common.ext.dropdownSelector
import com.example.cookaway.common.ext.horizontalSpacer
import com.example.cookaway.model.Post
import com.example.cookaway.model.UserData

@ExperimentalMaterialApi
@Composable
fun DangerousCardEditor(
  @StringRes title: Int,
  @DrawableRes icon: Int,
  content: String,
  modifier: Modifier,
  onEditClick: () -> Unit
) {
  CardEditor(title, icon, content, onEditClick, MaterialTheme.colors.primary, modifier)
}

@ExperimentalMaterialApi
@Composable
fun RegularCardEditor(
  @StringRes title: Int,
  @DrawableRes icon: Int,
  content: String,
  modifier: Modifier,
  onEditClick: () -> Unit
) {
  CardEditor(title, icon, content, onEditClick, MaterialTheme.colors.onSurface, modifier)
}

@ExperimentalMaterialApi
@Composable
private fun CardEditor(
  @StringRes title: Int,
  @DrawableRes icon: Int,
  content: String,
  onEditClick: () -> Unit,
  highlightColor: Color,
  modifier: Modifier
) {
  Card(
    backgroundColor = MaterialTheme.colors.onPrimary,
    modifier = modifier,
    onClick = onEditClick
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.weight(1f)) { Text(stringResource(title), color = highlightColor) }

      if (content.isNotBlank()) {
        Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
      }

      Icon(painter = painterResource(icon), contentDescription = "Icon", tint = highlightColor)
    }
  }
}

@ExperimentalMaterialApi
@Composable
fun RegularCardDescriptor(
  @StringRes title: Int,
  @DrawableRes icon: Int,
  content: String,
  highlightColor: Color = MaterialTheme.colors.onSurface,
  modifier: Modifier
) {
  Card(
    backgroundColor = MaterialTheme.colors.onPrimary,
    modifier = modifier,
    elevation = 0.dp
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Absolute.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Icon(painter = painterResource(icon), contentDescription = "Icon", tint = highlightColor)

      Spacer(modifier = Modifier.horizontalSpacer())

      Column(modifier = Modifier.weight(1f)) { Text(stringResource(title), color = highlightColor) }

      if (content.isNotBlank()) {
        Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
      }
    }
  }
}

@Composable
@ExperimentalMaterialApi
fun CardSelector(
  @StringRes label: Int,
  options: List<String>,
  selection: String,
  modifier: Modifier,
  onNewValue: (String) -> Unit
) {
  Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
    DropdownSelector(label, options, selection, Modifier.dropdownSelector(), onNewValue)
  }
}


@Preview(showBackground =true)
@Composable
fun PostPreview(){
  PostScreenContents(
    Post(
      id ="",
      userId = "Wai Kit",
      imageUrl = "https://firebasestorage.googleapis.com/v0/b/cookaway-59718.appspot.com/o/burger.jpeg?alt=media&token=5004b3c8-cfbb-41c9-a2bd-7345ec6afb7f",
      title = "Food",
      description = "Ingredients and Recipe",
    ),
  )
}
//@Composable
//fun PostCard(
//  post: Post,
//  bought: Boolean = true,
//  onBuyClick: (String)-> Unit,
//  onViewClick: (String)-> Unit
//){
//  Column {
//    PostUser(post.userId)
//    PostImage(post.imageUrl)
//    PostTitle(post.title)
//    PostPurchaseOrView(
//      onBuyClick = onBuyClick,
//      onViewClick = onViewClick,
//      bought = bought,
//    )
//  }
//}



@Composable
fun PostScreenContents(
  post: Post,
){
  Column {
//    PostUser(post.userId)
    PostImage(post.imageUrl)
    PostTitle(post.title)
    PostDescription(post.description)
  }
}

@Composable
fun PostCard(
  currentUserData: UserData,
  post: Post,
  bought: Boolean = true,
  onBuyClick: (Post, UserData) -> Unit,
  openScreen: (String)->Unit,
  onViewClick: ((String)->Unit, Post) -> Unit,
){
  Column {
//    PostUser(post.userId)
    PostImage(post.imageUrl)
    PostTitle(post.title)
    PostPurchaseOrView(
      onBuyClick = onBuyClick,
      bought = bought,
      currentUserData = currentUserData,
      post = post,
      openScreen = openScreen,
      onViewClick = onViewClick,
    )
  }
}
@Composable
fun PostImage(imageURL: String) {
  AsyncImage(
    model = imageURL,
    contentDescription = "Food",
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f),
    contentScale = ContentScale.Crop
  )
}

@Composable
fun PostTitle(title: String) {
  Text(
    text = title,
    fontSize = 32.sp, // Adjust the font size as needed
    fontWeight = FontWeight.Bold,
    color = Color.Black, // Set the text color to black
    textAlign = TextAlign.Center, // Center the text
    modifier = Modifier
  )
}

@Composable
fun PostDescription(description: String) {
  Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = description)
  }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostPurchaseOrView(
  bought: Boolean,
  openScreen: (String)->Unit,
  onViewClick: ((String)->Unit, Post) -> Unit,
  onBuyClick: (Post, UserData) -> Unit,
  currentUserData: UserData,
  post: Post,
) {
  Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (!bought) {
      BuyCard(
        currentUserData = currentUserData,
        post = post,
        onBuyClick = onBuyClick,
      )
    } else {
      RegularCardEditor(
        title = R.string.view,
        icon = R.drawable.ic_visibility_on,
        content = "",
        modifier = Modifier.card()
      ) {
        onViewClick(openScreen, post)
      }
    }
  }
}
@ExperimentalMaterialApi
@Composable
private fun BuyCard(
  currentUserData: UserData,
  post: Post,
  onBuyClick: (Post, UserData) -> Unit
)
{
  var price = post.price
  var balanceAmount = currentUserData.balance
  var showWarningDialog by remember { mutableStateOf(false) }

  RegularCardEditor(R.string.buy, R.drawable.ic_exit, "RM $price", Modifier.card()) {
    showWarningDialog = true
  }

  if (showWarningDialog) {
    if(balanceAmount > price){
      AlertDialog(
        title = { Text(stringResource(R.string.confirm_purchase)) },
        text = { Text(stringResource(R.string.balance_after_purchase) + (balanceAmount-price).toString()) },
        dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
        confirmButton = {
          DialogConfirmButton(R.string.yes) {
            onBuyClick(post,currentUserData)
            showWarningDialog = false
          }
        },
        onDismissRequest = { showWarningDialog = false }
      )
    }
    else{
      AlertDialog(
        title = { Text(stringResource(R.string.confirm_purchase)) },
        text = { Text(stringResource(R.string.balance_after_purchase) + (balanceAmount-price).toString()) },
        dismissButton = { DialogCancelButton(R.string.okay) { showWarningDialog = false } },
        confirmButton = {},
        onDismissRequest = { showWarningDialog = false }
      )
    }
  }
}