package com.example.cookaway

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

interface Destinations {
    val route: String
}

object Login : Destinations {
    override val route = "login"
}

object Register : Destinations {
    override val route = "register"
}

object Home : Destinations {
    override val route = "home"
}

object Create : Destinations {
    override val route = "create"
}

object Profile : Destinations {
    override val route = "profile"
}

object Deposit : Destinations {
    override val route = "deposit"
}

object Withdraw : Destinations {
    override val route = "withdraw"
}