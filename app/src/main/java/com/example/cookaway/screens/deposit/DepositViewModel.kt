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

package com.example.cookaway.screens.deposit

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookaway.model.UserData
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
  logService: LogService,
  private val accountService: AccountService,
  private val userStorageService: UserStorageService,
) : MakeItSoViewModel(logService) {
  val currentUserData = userStorageService.currentUserData
  var balanceAmount = mutableDoubleStateOf(0.0)
  var topUpAmount = mutableStateOf("")
  private val pattern = Regex("^\\d+\$")

  fun onTopUpClick() {
    val topUpAmountDouble = topUpAmount.value.toDouble();
    launchCatching {
      userStorageService.topUp(balanceAmount.value, topUpAmountDouble)
    }
  }
  fun onTopUpChange(newValue: String) {
    if (newValue.isEmpty() || newValue.matches(pattern)) {
      topUpAmount.value = newValue
    }
  }

//  fun onWithdrawClick(openScreen: (String) -> Unit) = openScreen(WITHDRAW_SCREEN)
//  fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)
//
//  fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)
//
//  fun onSignOutClick(restartApp: (String) -> Unit) {
//    launchCatching {
//      accountService.signOut()
//      restartApp(SPLASH_SCREEN)
//    }
//  }
//
//  fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
//    launchCatching {
//      accountService.deleteAccount()
//      restartApp(SPLASH_SCREEN)
//    }
//  }
}
