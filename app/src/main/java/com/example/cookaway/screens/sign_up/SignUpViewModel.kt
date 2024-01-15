

package com.example.cookaway.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.cookaway.R.string as AppText
import com.example.cookaway.SETTINGS_SCREEN
import com.example.cookaway.SIGN_UP_SCREEN
import com.example.cookaway.SPLASH_SCREEN
import com.example.cookaway.common.ext.isValidEmail
import com.example.cookaway.common.ext.isValidPassword
import com.example.cookaway.common.ext.passwordMatches
import com.example.cookaway.common.snackbar.SnackbarManager
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
  private val accountService: AccountService,
  private val userStorageService: UserStorageService,
  logService: LogService
) : MakeItSoViewModel(logService) {
  var uiState = mutableStateOf(SignUpUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onRepeatPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(repeatPassword = newValue)
  }

  fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (!password.isValidPassword()) {
      SnackbarManager.showMessage(AppText.password_error)
      return
    }

    if (!password.passwordMatches(uiState.value.repeatPassword)) {
      SnackbarManager.showMessage(AppText.password_match_error)
      return
    }

    launchCatching {
      accountService.linkAccount(email, password)
      userStorageService.createUser(accountService.currentUserId)
      openAndPopUp(SPLASH_SCREEN, SIGN_UP_SCREEN)
      //openAndPopUp(SETTINGS_SCREEN, SIGN_UP_SCREEN)
    }
  }
}
