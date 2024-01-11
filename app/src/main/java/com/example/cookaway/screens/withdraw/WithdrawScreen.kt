package com.example.cookaway.screens.withdraw

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cookaway.screens.settings.SettingsViewModel

@ExperimentalMaterialApi
@Composable
fun WithdrawScreen(
    viewModel: SettingsViewModel = hiltViewModel()
){
    Text("Withdraw")
}