

package com.example.cookaway.screens.create_post

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cookaway.R
import com.example.cookaway.R.drawable as AppIcon
import com.example.cookaway.R.string as AppText
import com.example.cookaway.common.composable.*
import com.example.cookaway.common.ext.card
import com.example.cookaway.common.ext.fieldModifier
import com.example.cookaway.common.ext.spacer
import com.example.cookaway.common.ext.toolbarActions
import com.example.cookaway.model.Post
import com.example.cookaway.model.Priority
import com.example.cookaway.model.Task
import com.example.cookaway.theme.MakeItSoTheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

@Composable
@ExperimentalMaterialApi
fun CreatePostScreen(
  popUpScreen: () -> Unit,
  viewModel: CreatePostViewModel = hiltViewModel()
) {
//  val task by viewModel.task
  val activity = LocalContext.current as AppCompatActivity
  var itemPrice by viewModel.price
  val post by viewModel.post

  CreatePostScreenContent(
    post = post,
    onDoneClick = { viewModel.onDoneClick(activity,popUpScreen) },
    onTitleChange = viewModel::onTitleChange,
    onDescriptionChange = viewModel::onDescriptionChange,
    updateSelectedImageUri = viewModel::updateSelectedImageUri,
//    uploadImageToFirebase = viewModel::uploadImageToFirebase,
    selectedImageUri = viewModel.selectedImageUri,
    price = itemPrice,
    onPriceChange = viewModel::onPriceChange,
  )
}

@Composable
@ExperimentalMaterialApi
fun CreatePostScreenContent(
  post: Post,
  modifier: Modifier = Modifier,
  onDoneClick: () -> Unit,
  onTitleChange: (String) -> Unit,
  onDescriptionChange: (String) -> Unit,
  updateSelectedImageUri: (Uri) -> Unit,
//  uploadImageToFirebase: (Context, Uri) -> Unit,
  selectedImageUri: Uri,
  price: String,
  onPriceChange: (String) -> Unit,
  ) {
//  val context = LocalContext.current
  Column(
    modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    ActionToolbar(
      title = AppText.create_post,
      modifier = Modifier.toolbarActions(),
      endActionIcon = AppIcon.ic_check,
      endAction = { onDoneClick() }
    )

    Spacer(modifier = Modifier.spacer())

    selectedImageUri?.let { uri ->
      Image(
        painter = rememberAsyncImagePainter(model = uri),
        contentDescription = "Selected Image",
        modifier = Modifier.size(200.dp)
      )
    }

    val fieldModifier = Modifier.fieldModifier()
    ImagePicker { uri ->
      updateSelectedImageUri(uri)
//      uploadImageToFirebase(context,uri)
    }
    BasicField(AppText.title, post.title, onTitleChange, fieldModifier)
    LargeField(AppText.description, post.description, onDescriptionChange, fieldModifier)
    MoneyField(R.string.price, price, onPriceChange, Modifier.fieldModifier())

    Spacer(modifier = Modifier.spacer())
  }
}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = { uri: Uri? -> uri?.let { onImageSelected(it) } }
  )
  BasicTextButton(text = AppText.upload_image, modifier= Modifier, action = { launcher.launch("image/*") })
}