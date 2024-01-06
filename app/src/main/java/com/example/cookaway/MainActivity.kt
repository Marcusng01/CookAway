package com.example.cookaway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cookaway.ui.login.LoginScreen
import com.example.cookaway.ui.theme.CookAwayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CookAwayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CookAwayApp()
                }
            }
        }
    }
}

@Composable
fun CookAwayApp(){
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
//    val currentScreen = rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
    CookAwayNavHost(
        navController,
        modifier = Modifier.padding()
    )
}

@Composable
fun CookAwayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        startDestination = Login.route,
        modifier = modifier
    ){
        composable(route = Login.route){
            LoginScreen()
        }
        composable(route = Register.route){
//            DepositScreen()
        }
        composable(route = Home.route){
//            HomeScreen()
        }
        composable(route = Create.route){
//            CreateScreen()
        }
        composable(route = Profile.route){
//            ProfileScreen()
        }
        composable(route = Deposit.route){
//            DepositScreen()
        }
        composable(route = Withdraw.route){
//            WithdrawScreen()
        }
    }
}