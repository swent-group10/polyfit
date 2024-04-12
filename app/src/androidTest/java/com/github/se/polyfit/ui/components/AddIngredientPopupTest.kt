package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddIngredientPopupTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  @Before
  fun setup() {
    mockkStatic(Log::class)
    composeTestRule.setContent {
      AddIngredientDialog(onAddIngredient = {}, onClickCloseDialog = {})
    }
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun popupBoxIsDisplayed() {
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientDialog { assertIsDisplayed() }
      closePopupIcon {
        assertIsDisplayed()
        assertHasClickAction()
      }
      addIngredientContent { assertIsDisplayed() }
    }
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun searchIngredientIsDisplayed() {
    ComposeScreen.onComposeScreen<AddIngredientSearchBar>(composeTestRule) {
      searchResultContainer { assertDoesNotExist() }

      singleSearchResult { assertDoesNotExist() }

      // Search bar input
      composeTestRule
          .onNodeWithText("Enter an Ingredient...") // find based on placeholder
          .assertExists("Placeholder text is not found.")
          .performTextInput("a")

      composeTestRule
          .onNodeWithTag("SearchIcon")
          .assertExists("Search icon is not found.")
          .performClick()

      searchResultContainer {
        assertIsDisplayed()
        performScrollToIndex(1)
      }

      singleSearchResult { assertIsDisplayed() }
    }
  }

  @Test
  fun searchIngredientIsFunctional() {
    ComposeScreen.onComposeScreen<AddIngredientSearchBar>(composeTestRule) {
      // Search bar input
      composeTestRule
          .onNodeWithText("Enter an Ingredient...") // find based on placeholder
          .performTextInput("apple")

      composeTestRule
          .onNodeWithTag("SearchIngredientBar")
          .performKeyPress(KeyEvent(NativeKeyEvent(0, 66))) // 66 is the keycode for 'Enter' key

      verify { Log.v("Search", "Searching for apple") }

      composeTestRule
          .onNodeWithTag("SearchIcon")
          .assertExists("Search icon is not found.")
          .performClick()

      singleSearchResult {
        assertIsDisplayed()
        assertTextContains("Apple")
        assertHasClickAction()
        performClick()
      }

      composeTestRule.onNodeWithText("Apple").assertIsDisplayed()

      singleSearchResult { assertDoesNotExist() }
    }
  }

  // TODO: after writing data retrieval from database for ingredient, write test for it

  @Test
  fun nutritionInfoIsDisplayed() {
    ComposeScreen.onComposeScreen<AddIngredientEditNutritionInfo>(composeTestRule) {
      servingSizeContainer { assertIsDisplayed() }
      caloriesContainer { assertIsDisplayed() }
      carbsContainer { assertIsDisplayed() }
      fatContainer { assertIsDisplayed() }
      proteinContainer { assertIsDisplayed() }
    }
  }

  @Test
  fun addButtonIsDisplayed() {
    ComposeScreen.onComposeScreen<AddIngredientButton>(composeTestRule) {
      addIngredientButton {
        assertIsDisplayed()
        assertTextContains("Add")
        assertHasClickAction()
      }
    }
  }

  @Test
  fun addButtonIsConditionallyDisabled() {
    ComposeScreen.onComposeScreen<AddIngredientButton>(composeTestRule) {
      addIngredientButton {
        assertIsDisplayed()
        assertTextContains("Add")
        assertIsNotEnabled()
      }

      composeTestRule.onNodeWithText("Enter an Ingredient...").performTextInput("apple")

      composeTestRule.onNodeWithTag("NutritionSizeInput Total Weight").performTextInput("1")

      composeTestRule.onNodeWithTag("NutritionSizeInput Calories").performTextInput("1")

      addIngredientButton { assertIsEnabled() }
    }
  }
}
