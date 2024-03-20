package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController
import javax.inject.Inject

interface NavigationInterface {
    fun navigateToHome()
    fun getController(): NavHostController?
}

class Navigation @Inject constructor(private val navHostController: NavHostController) : NavigationInterface {
    override fun navigateToHome() {
        navigateTo(Route.Home)
    }

    override fun getController(): NavHostController {
        return navHostController
    }

    private fun navigateTo(route: String) {
        Log.i("Navigation", "Navigating to $route")
        navHostController.navigate(route)
    }
}

class MockNavigation  @Inject constructor() : NavigationInterface {
    var number_calls = 0

    fun get_number_calls(): Int {
        return number_calls
    }
    override fun navigateToHome() {
        number_calls++
        Log.i("Navigation", "Mock Navigating to home")
    }

    override fun getController(): NavHostController? {
        return null
    }
}