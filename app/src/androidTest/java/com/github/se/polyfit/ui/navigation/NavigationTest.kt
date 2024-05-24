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
  fun navigateToCreatePost() {
    route = Route.CreatePost
    every { navHostController.navigate(route) } returns Unit
    navigation.navigateToCreatePost()

    verify { navHostController.navigate(Route.CreatePost) }
  }

  @Test
  fun navigateToSettingsHome() {
    route = Route.SettingsHome
    every { navHostController.navigate(route) } returns Unit
    navigation.navigateToSettingsHome()

    verify { navHostController.navigate(Route.SettingsHome) }
  }

  @Test
  fun goBackTo() {
    route = Route.Home
    every { navHostController.popBackStack(route, any()) } returns true
    navigation.goBackTo(Route.Home)

    verify { navHostController.popBackStack(Route.Home, false) }
  }

  @Test
  fun goBackToLogin() {
    route = Route.Register
    every { navHostController.popBackStack(route, any()) } returns true
    navigation.goBackToLogin()

    verify { navHostController.popBackStack(Route.Register, false) }
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
  fun navigateToEditMeal() {
    val mealDatabaseId = "1"
    route = Route.EditMeal + "/$mealDatabaseId"
    every { navHostController.navigate(route) } returns Unit

    navigation.navigateToEditMeal(mealDatabaseId)

    verify { navHostController.navigate(route) }
  }

  @Test
  fun navigateToAddMealWithIdNull() {
    route = Route.AddMeal
    every { navHostController.navigate(any<String>()) } returns Unit

    navigation.navigateToAddMeal()

    verify { navHostController.navigate(Route.AddMeal) }
  }
}
