package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.test.Test
import org.junit.Before

class NavigationTest {

  private val navHostController = mockk<NavHostController>(relaxed = true)
  private lateinit var navigation: Navigation

  @Before
  fun setUp() {
    mockkStatic(Log::class)
    every { Log.i(any(), any()) } returns 0

    navigation = Navigation(navHostController)
  }

  @Test
  fun navigateToAddMeal_withMealDatabaseId_navigatesToCorrectRoute() {
    val mealDatabaseId = 1L
    val expectedRoute = Route.AddMeal + "/$mealDatabaseId"
    every { navHostController.navigate(expectedRoute) } just Runs

    navigation.navigateToAddMeal(mealDatabaseId)

    verify { navHostController.navigate(expectedRoute) }
  }

  @Test
  fun navigateToAddMeal_withoutMealDatabaseId_navigatesToCorrectRoute() {
    val expectedRoute = Route.AddMeal
    every { navHostController.navigate(expectedRoute) } just Runs

    navigation.navigateToAddMeal()

    verify { navHostController.navigate(expectedRoute) }
  }

  @Test
  fun navigateToAdditionalMealInfo_navigatesToCorrectRoute() {
    val expectedRoute = Route.AdditionalMealInfo
    every { navHostController.navigate(expectedRoute) } just Runs

    navigation.navigateToAdditionalMealInfo()

    verify { navHostController.navigate(expectedRoute) }
  }

  @Test
  fun navigateToDailyRecap_navigatesToCorrectRoute() {
    val expectedRoute = Route.DailyRecap
    every { navHostController.navigate(expectedRoute) } just Runs

    navigation.navigateToDailyRecap()

    verify { navHostController.navigate(expectedRoute) }
  }

  @Test
  fun navigateTONutrition_navigatesToCorrectRoute() {
    val expectedRoute = Route.Nutrition
    every { navHostController.navigate(expectedRoute) } just Runs

    navigation.navigateToNutrition()

    verify { navHostController.navigate(expectedRoute) }
  }

  @Test
  fun testGoBack() {
    every { navHostController.popBackStack() } returns true

    navigation.goBack()

    verify { navHostController.popBackStack() }
  }
}
