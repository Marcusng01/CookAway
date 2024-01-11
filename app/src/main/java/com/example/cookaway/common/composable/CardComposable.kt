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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookaway.R
import com.example.cookaway.common.ext.basicButton
import com.example.cookaway.common.ext.dropdownSelector
import com.example.cookaway.common.ext.horizontalSpacer

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
      modifier = Modifier.fillMaxWidth().padding(16.dp)
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
      modifier = Modifier.fillMaxWidth().padding(16.dp)
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
    Post(
      "Wai Kit",
      R.drawable.ic_user_circle,
      "Food",
      "Ingredients and Recipe",
      false)
}

@Composable
fun Post(
  username: String,
  image: Int,
  title: String,
  description: String,
  bought: Boolean,
){
  Column {
    PostUser(username)
    PostImage(image)
    PostTitle(title)
    PostDescription(description,bought)
  }
}

@Composable
fun PostUser(username: String) {
  Row(modifier = Modifier.padding(16.dp)) {
    Image(
      painter = painterResource(R.drawable.ic_user_circle),
      contentDescription = null,
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
    )
    Text(
      text = username,
      modifier = Modifier.padding(horizontal = 8.dp)
    )
  }
}


@Composable
fun PostImage(image: Int) {
  Image(
    painter = painterResource(image),
    contentDescription = null,
    modifier = Modifier.fillMaxWidth()
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
fun PostDescription(description: String, bought: Boolean) {
  Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (!bought) {
      BasicButton(R.string.buy, Modifier.basicButton(), { })
    } else {
      Text(text = description)
    }
  }
}
