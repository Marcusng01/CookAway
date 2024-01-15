package com.example.cookaway.screens.withdraw

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookaway.model.UserData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.cookaway.R
import com.example.cookaway.common.composable.BasicButton
import com.example.cookaway.common.composable.MoneyField
import com.example.cookaway.common.ext.basicButton
import com.example.cookaway.common.ext.fieldModifier

@ExperimentalMaterialApi
@Composable
fun WithdrawScreen(
    viewModel: WithdrawViewModel = hiltViewModel(),
    popUpScreen: () -> Unit
){
    val activity = LocalContext.current as AppCompatActivity
    val userData = viewModel.currentUserData.collectAsStateWithLifecycle(UserData()).value
    var balanceAmount by viewModel.balanceAmount
    balanceAmount = userData.balance
    var withdrawAmount by viewModel.withdrawAmount
    WithdrawScreenContent(
        withdrawAmount =  withdrawAmount,
        onWithdrawChange = viewModel::onWithdrawChange,
        onWithdrawClick = { viewModel.onWithdrawClick(activity,popUpScreen) }
    )}
@Composable
@ExperimentalMaterialApi
fun WithdrawScreenContent(
    modifier: Modifier = Modifier,
    withdrawAmount: String,
    onWithdrawChange: (String) -> Unit,
    onWithdrawClick: () -> Unit,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        MoneyField(R.string.withdraw, withdrawAmount, onWithdrawChange, Modifier.fieldModifier())
        BasicButton(R.string.withdraw, Modifier.basicButton()) {onWithdrawClick()}
    }
}