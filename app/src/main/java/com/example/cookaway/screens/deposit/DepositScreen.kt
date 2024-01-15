package com.example.cookaway.screens.deposit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookaway.R
import com.example.cookaway.common.composable.BasicButton
import com.example.cookaway.common.composable.BasicField
import com.example.cookaway.common.composable.MoneyField
import com.example.cookaway.common.ext.basicButton
import com.example.cookaway.common.ext.fieldModifier
import com.example.cookaway.model.UserData
import kotlin.reflect.KFunction1

@ExperimentalMaterialApi
@Composable
fun DepositScreen(
    viewModel: DepositViewModel = hiltViewModel(),
    popUpScreen: () -> Unit,
){
    val userData = viewModel.currentUserData.collectAsStateWithLifecycle(UserData()).value
    var balanceAmount by viewModel.balanceAmount
    balanceAmount = userData.balance
    var topUpAmount by viewModel.topUpAmount
    DepositScreenContent(
        topUpAmount = topUpAmount,
        onTopUpChange = viewModel::onTopUpChange,
        onTopUpClick = { viewModel.onTopUpClick(popUpScreen) }
    )}

@Composable
@ExperimentalMaterialApi
fun DepositScreenContent(
    modifier: Modifier = Modifier,
    topUpAmount: String,
    onTopUpChange: (String) -> Unit,
    onTopUpClick: () -> Unit,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        MoneyField(R.string.deposit, topUpAmount, onTopUpChange, Modifier.fieldModifier())
        BasicButton(R.string.deposit, Modifier.basicButton()) {onTopUpClick()}
    }
}