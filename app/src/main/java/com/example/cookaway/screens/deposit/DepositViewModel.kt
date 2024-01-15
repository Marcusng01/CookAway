

package com.example.cookaway.screens.deposit

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

  fun onTopUpClick(popUpScreen: () -> Unit) {
    val topUpAmountDouble = if (topUpAmount.value.isEmpty()) 0.0 else topUpAmount.value.toDouble()
    launchCatching {
//      userStorageService.topUp(balanceAmount.value, topUpAmountDouble, "")
      userStorageService.topUp(topUpAmountDouble, accountService.currentUserId)
    }
    popUpScreen()
  }
  fun onTopUpChange(newValue: String) {
    if (newValue.isEmpty() || newValue.matches(pattern)) {
      topUpAmount.value = newValue
    }
  }

}
