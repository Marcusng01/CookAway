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

package com.example.cookaway.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookaway.R.drawable as AppIcon
import com.example.cookaway.R.string as AppText
import com.example.cookaway.common.composable.*
import com.example.cookaway.common.ext.card
import com.example.cookaway.common.ext.spacer
import com.example.cookaway.model.UserData
import com.example.cookaway.theme.MakeItSoTheme
import kotlinx.coroutines.flow.first

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
  restartApp: (String) -> Unit,
  openScreen: (String) -> Unit,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val uiState = viewModel.uiState
  val userData = viewModel.currentUserData.collectAsStateWithLifecycle(UserData())
//  val uiState by viewModel.uiState.collectAsState(
//    initial = SettingsUiState(false)
//  )
  SettingsScreenContent(
    onSignOutClick = { viewModel.onSignOutClick(restartApp) },
    onDepositClick = { viewModel.onDepositClick(openScreen) },
    onWithdrawClick = { viewModel.onWithdrawClick(openScreen) },
    email = viewModel.currentUserEmail,
    userData = userData.value
  )
}

@ExperimentalMaterialApi
@Composable
fun SettingsScreenContent(
  modifier: Modifier = Modifier,
  onSignOutClick: () -> Unit,
  onDepositClick: () -> Unit,
  onWithdrawClick: () -> Unit,
  email: String,
  userData: UserData
) {

  Column(
    modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    BasicToolbar(AppText.settings)
    Spacer(modifier = Modifier.spacer())
    EmailCard(email)
    Spacer(modifier = Modifier.spacer())
    BalanceCard(userData.balance)
    DepositCard { onDepositClick()  }
    WithdrawCard { onWithdrawClick() }
    Spacer(modifier = Modifier.spacer())
    Spacer(modifier = Modifier.spacer())
    Spacer(modifier = Modifier.spacer())
    Spacer(modifier = Modifier.spacer())
    SignOutCard { onSignOutClick() }
  }
}

@ExperimentalMaterialApi
@Composable
private fun EmailCard(
  email: String
){
  RegularCardDescriptor(
    AppText.email,
    AppIcon.ic_user_circle, email,modifier = Modifier.card())
}

@ExperimentalMaterialApi
@Composable
private fun BalanceCard(
  balance: Double
){
  var balanceString = "RM ${balance.toString()}"
  RegularCardDescriptor(
    AppText.balance,
    AppIcon.ic_balance, balanceString,modifier = Modifier.card())
}
@ExperimentalMaterialApi
@Composable
private fun DepositCard(
  onDepositClick: () -> Unit,
) {
  RegularCardEditor(AppText.deposit, AppIcon.ic_deposit, "", Modifier.card()){
    onDepositClick()
  }
}

@ExperimentalMaterialApi
@Composable
private fun WithdrawCard(
  onWithdrawClick: () -> Unit
) {
  RegularCardEditor(AppText.withdraw, AppIcon.ic_withdraw, "", Modifier.card()){
    onWithdrawClick()
  }
}


@ExperimentalMaterialApi
@Composable
private fun SignOutCard(signOut: () -> Unit) {
  var showWarningDialog by remember { mutableStateOf(false) }

  RegularCardEditor(AppText.sign_out, AppIcon.ic_exit, "", Modifier.card()) {
    showWarningDialog = true
  }

  if (showWarningDialog) {
    AlertDialog(
      title = { Text(stringResource(AppText.sign_out_title)) },
      text = { Text(stringResource(AppText.sign_out_description)) },
      dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
      confirmButton = {
        DialogConfirmButton(AppText.sign_out) {
          signOut()
          showWarningDialog = false
        }
      },
      onDismissRequest = { showWarningDialog = false }
    )
  }
}

//@ExperimentalMaterialApi
//@Composable
//private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
//  var showWarningDialog by remember { mutableStateOf(false) }
//
//  DangerousCardEditor(
//    AppText.delete_my_account,
//    AppIcon.ic_delete_my_account,
//    "",
//    Modifier.card()
//  ) {
//    showWarningDialog = true
//  }
//
//  if (showWarningDialog) {
//    AlertDialog(
//      title = { Text(stringResource(AppText.delete_account_title)) },
//      text = { Text(stringResource(AppText.delete_account_description)) },
//      dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
//      confirmButton = {
//        DialogConfirmButton(AppText.delete_my_account) {
//          deleteMyAccount()
//          showWarningDialog = false
//        }
//      },
//      onDismissRequest = { showWarningDialog = false }
//    )
//  }
//}

@Preview(showBackground = true)
@ExperimentalMaterialApi
@Composable
fun SettingsScreenPreview() {
  val uiState = SettingsUiState(hasUser = false)

  MakeItSoTheme {
    SettingsScreenContent(
      onSignOutClick = { },
      onDepositClick = {  },
      onWithdrawClick = {  },
      email = "ai210417@gmail.com",
      userData = UserData()
    )
  }
}
