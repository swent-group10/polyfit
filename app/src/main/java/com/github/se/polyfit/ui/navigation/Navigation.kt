package com.github.se.polyfit.ui.navigation

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import javax.inject.Inject

interface NavigationInterface {
    fun navigateToHome()
    fun getController(): NavHostController?
}

class Navigation (private val navHostController: NavHostController) : NavigationInterface {
    var navigationCall = 0
    override fun navigateToHome() {
        navigateTo(Route.Home)
    }

    override fun getController(): NavHostController {
        return navHostController
    }

    private fun navigateTo(route: String) {
        Log.i("Navigation", "Navigating to $route")
        navigationCall++
        navHostController.navigate(route)
    }
}

class MockNavigation (val context: Context) : NavigationInterface {
    var number_calls = 0

    fun get_number_calls(): Int {
        return number_calls
    }
    override fun navigateToHome() {
        number_calls++
        Log.i("Navigation", "Mock Navigating to home")
    }

    override fun getController(): NavHostController {
        return NavHostController(context)
    }
}