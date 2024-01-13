package com.example.cookaway.screens.withdraw

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.cookaway.model.service.AccountService
import com.example.cookaway.model.service.LogService
import com.example.cookaway.model.service.UserStorageService
import com.example.cookaway.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val userStorageService: UserStorageService,
) : MakeItSoViewModel(logService) {
    val currentUserData = userStorageService.currentUserData
    var balanceAmount = mutableDoubleStateOf(0.0)
    var withdrawAmount = mutableStateOf("")
    private val pattern = Regex("^\\d+\$")

    fun onWithdrawClick() {
        val withdrawAmountDouble = withdrawAmount.value.toDouble();
        launchCatching {
            userStorageService.withdraw(withdrawAmountDouble, accountService.currentUserId)
        }
    }

    fun onWithdrawChange(newValue: String) {
        if (newValue.isEmpty() || newValue.matches(pattern)) {
            withdrawAmount.value = newValue
        }
    }
}