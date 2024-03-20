package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

interface NavigationInterface {
    fun navigateToHome()
}

class Navigation(private val navHostController: NavHostController) : NavigationInterface {
    override fun navigateToHome() {
        navigateTo(Route.Home)
    }
    private fun navigateTo(route: String) {
        Log.i("Navigation", "Navigating to $route")
        navHostController.navigate(route)
    }
}

class MockNavigation() : NavigationInterface {
    var number_calls = 0

    fun get_number_calls(): Int {
        return number_calls
    }
    override fun navigateToHome() {
        number_calls++
        Log.i("Navigation", "Navigating to home")
    }
}