package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    // Launch the Ingredients screen
    composeTestRule.setContent {
      val navController = rememberNavController()
      IngredientScreen(Navigation(navController))
    }
  }

  @Test
  fun topBarDisplayed() {
    ComposeScreen.onComposeScreen<IngredientsTopBar>(composeTestRule) {
      // Test the UI elements
      ingredientTitle {
        assertIsDisplayed()
        assertTextEquals("Ingredients")
      }

      backButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun bottomBarDisplayed() {
    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      // Test the UI elements
      addIngredientButton { assertIsDisplayed() }

      doneButton {
        assertIsDisplayed()
        assertTextEquals("Done")
        assertHasClickAction()
      }
    }
  }
}
