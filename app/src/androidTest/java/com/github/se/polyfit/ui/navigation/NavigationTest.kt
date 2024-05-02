package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
  private val navHostController: NavHostController = mockk()
  private val navigation = Navigation(navHostController)
  lateinit var route: String

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { navHostController.popBackStack() } returns true
  }

  @After
  fun teardown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun goBack() {
    navigation.goBack()
    verify { navHostController.popBackStack() }
  }

  @Test
  fun navigateToHome() {
    route = Route.Home
    every { navHostController.navigate(route) } returns Unit
    navigation.navigateToHome()

    verify { navHostController.navigate(Route.Home) }
  }

  @Test
  fun navigateToAddMeal() {
    route = Route.AddMeal
    every { navHostController.navigate(route) } returns Unit
    navigation.navigateToAddMeal()

    verify { navHostController.navigate(Route.AddMeal) }
  }

  @Test
  fun navigateToNutrition() {
    route = Route.Nutrition
    every { navHostController.navigate(route) } returns Unit
    navigation.navigateToNutrition()

    verify { navHostController.navigate(Route.Nutrition) }
  }

  @Test
  fun navigateToAddMealWithIdNull() {
    route = Route.AddMeal
    every { navHostController.navigate(any<String>()) } returns Unit

    navigation.navigateToAddMeal()

    verify { navHostController.navigate(Route.AddMeal) }
  }
}
