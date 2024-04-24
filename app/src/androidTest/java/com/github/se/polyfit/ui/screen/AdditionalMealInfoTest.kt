package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdditionalMealInfoTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  @RelaxedMockK lateinit var mockMealViewModel: MealViewModel

  fun setup(meal: Meal = Meal.default()) {
    mockkStatic(Log::class)
    every { mockMealViewModel.meal } returns MutableStateFlow(meal)
    composeTestRule.setContent {
      AdditionalMealInfoScreen(mockMealViewModel, mockNav::goBack, mockNav::navigateToNutrition)
    }
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun topBar() {
    setup()
    ComposeScreen.onComposeScreen<AdditionalMealInfoTopBar>(composeTestRule) {
      title {
        assertIsDisplayed()
        assertTextContains("Additional Meal Info")
      }

      backButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      verify { mockNav.goBack() }
      confirmVerified(mockNav)
    }
  }

  @Test
  fun bottomBarDisabled() {
    setup()
    ComposeScreen.onComposeScreen<AdditionalMealInfoBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertIsNotEnabled()
      }
    }
  }

  @Test
  fun bottomBarEnabled() {
    val meal =
        Meal.default()
            .copy(
                occasion = MealOccasion.BREAKFAST,
            )
    setup(meal)

    ComposeScreen.onComposeScreen<AdditionalMealInfoBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertIsEnabled()
        assertHasClickAction()
        performClick()
      }

      verify { mockNav.navigateToNutrition() }
      confirmVerified(mockNav)
    }
  }

  @Test
  fun everythingIsDisplayed() {
    setup()
    ComposeScreen.onComposeScreen<AdditionalMealInfoScreen>(composeTestRule) {
      dateSelector { assertIsDisplayed() }
      mealOccasionSelector { assertIsDisplayed() }
      mealTagSelector { assertIsDisplayed() }
    }
  }
}
