

package com.example.cookaway.screens.create_post

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.cookaway.TASK_ID
import com.example.cookaway.common.ext.idFromParameter
import com.example.cookaway.model.Post
import com.example.cookaway.model.Task
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.StorageService
import com.example.cookaway.screens.MakeItSoViewModel
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  logService: LogService,
  private val storageService: StorageService,
  private val accountService: AccountService,
) : MakeItSoViewModel(logService) {
  private val imageStorageService = FirebaseStorage.getInstance()
  private val pattern = Regex("^\\d+(\\.\\d{0,2})?$")
  var post = mutableStateOf(Post())
  var price = mutableStateOf("")
  var selectedImageUri by mutableStateOf<Uri>(Uri.EMPTY)

  fun onPriceChange(newValue: String) {
    if (newValue.isEmpty() || newValue.matches(pattern)) {
      price.value = newValue
      post.value = post.value.copy(price = price.value.ifEmpty { "0" }.toDouble())
    }
  }
  private fun uploadImageToFirebase(uri: Uri,popUpScreen: () -> Unit){
    val storageRef = imageStorageService.reference
    val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
    val uploadTask = imageRef.putFile(uri)
    val urlTask = uploadTask.continueWithTask { task ->
      if (!task.isSuccessful) {
        task.exception?.let {
          throw it
        }
      }
      imageRef.downloadUrl
    }.addOnCompleteListener { task ->
      if (task.isSuccessful) {
        val downloadUri = task.result
        post.value = post.value.copy(imageUrl = downloadUri.toString())

        var createdPost = post.value
        launchCatching {
          if (createdPost.id.isBlank()) {
            storageService.save(createdPost)
          } else {
            storageService.update(createdPost)
          }
        }
        popUpScreen()


      } else {
        // Handle failures
        // ...
      }
    }

    urlTask.addOnFailureListener {
      // Handle unsuccessful uploads
    }.addOnSuccessListener {
      // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
      // ...
    }
  }
  fun updateSelectedImageUri(uri: Uri){
    selectedImageUri = uri
  }

  fun onTitleChange(newValue: String) {
    post.value = post.value.copy(title = newValue)
  }

  fun onDescriptionChange(newValue: String) {
    post.value = post.value.copy(description = newValue)
  }

  fun onDoneClick(context:Context, popUpScreen: () -> Unit) {
    try {
      if (Uri.EMPTY.equals(selectedImageUri) || post.value.title=="" || post.value.description=="" || post.value.price==0.toDouble() || price.value==""){
        throw Exception()
      }
      uploadImageToFirebase(selectedImageUri,popUpScreen)
    }catch (e:Exception){
      val builder = AlertDialog.Builder(context)
      builder.setTitle("Error")
      builder.setMessage("Incomplete Information")
      builder.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
      }
      builder.show()
      Log.v("Incomplete Info","oof")
    }
  }

  private fun Int.toClockPattern(): String {
    return if (this < 10) "0$this" else "$this"
  }

  companion object {
    private const val UTC = "UTC"
    private const val DATE_FORMAT = "EEE, d MMM yyyy"
  }
}
