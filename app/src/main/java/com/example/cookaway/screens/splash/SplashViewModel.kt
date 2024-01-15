

package com.example.cookaway.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.example.cookaway.LOGIN_SCREEN
import com.example.cookaway.SPLASH_SCREEN
import com.example.cookaway.TASKS_SCREEN
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.ConfigurationService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.screens.MakeItSoViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  configurationService: ConfigurationService,
  private val accountService: AccountService,
  logService: LogService
) : MakeItSoViewModel(logService) {
  val showError = mutableStateOf(false)

  init {
    launchCatching { configurationService.fetchConfiguration() }
  }

  fun onAppStart(openAndPopUp: (String, String) -> Unit) {

    showError.value = false
    if (accountService.hasUser) {
      openAndPopUp(TASKS_SCREEN, SPLASH_SCREEN)
    }
    else
    {
      openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
    }
  }

//  private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
//    launchCatching(snackbar = false) {
//      try {
//        accountService.createAnonymousAccount()
//      } catch (ex: FirebaseAuthException) {
//        showError.value = true
//        throw ex
//      }
//      openAndPopUp(TASKS_SCREEN, SPLASH_SCREEN)
//    }
//  }
}
