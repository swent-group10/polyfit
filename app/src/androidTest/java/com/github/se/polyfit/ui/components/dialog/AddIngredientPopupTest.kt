package com.github.se.polyfit.ui.components.dialog

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
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
      ingredientTitle { assertIsDisplayed() }
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

      composeTestRule.onNodeWithTag("EnterIngredientName").performTextInput("apple")

      composeTestRule.onNodeWithTag("NutritionSizeInput Total Weight").performTextInput("1")

      composeTestRule.onNodeWithTag("NutritionSizeInput Calories").performTextInput("1")

      addIngredientButton { assertIsEnabled() }
    }
  }
}
