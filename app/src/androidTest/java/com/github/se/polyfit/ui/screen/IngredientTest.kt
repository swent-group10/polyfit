package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  private fun launchIngredientScreenWithTestData(
      testIngredients: List<Ingredient>,
      testPotentials: List<Ingredient>
  ) {
    composeTestRule.setContent { IngredientScreen(mockNav, testIngredients, testPotentials) }
  }

  private val manyIngredients =
      listOf(
          Ingredient("Olive Oil", 5, "ml"),
          Ingredient("Beef Tenderloin", 50, "g"),
          Ingredient("White Asparagus", 10, "g"),
          Ingredient("Corn", 4, "g"),
          Ingredient("Foie Gras", 100, "g"))
  private val fewPotentialIngredients =
      listOf(
          Ingredient("Carrots", 100, "g"),
      )

  private val manyPotentialIngredients =
      listOf(
          Ingredient("Carrots", 100, "g"),
          Ingredient("Peas", 100, "g"),
          Ingredient("Worcestershire Sauce", 15, "ml"),
          Ingredient("Salt", 5, "g"),
          Ingredient("Pepper", 5, "g"),
          Ingredient("Garlic", 1, "clove"),
      )

  @Test
  fun topBarDisplayed() {
    launchIngredientScreenWithTestData(emptyList(), emptyList())
    ComposeScreen.onComposeScreen<IngredientsTopBar>(composeTestRule) {
      ingredientTitle {
        assertIsDisplayed()
        assertTextEquals("Ingredients")
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
  fun bottomBarDisplayed() {
    launchIngredientScreenWithTestData(emptyList(), emptyList())
    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      addIngredientButton { assertIsDisplayed() }

      doneButton {
        assertIsDisplayed()
        assertTextEquals("Done")
        assertHasClickAction()
      }
    }
  }

  @Test
  fun noIngredientMessageDisplay() {
    launchIngredientScreenWithTestData(emptyList(), emptyList())
    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      noIngredients {
        assertIsDisplayed()
        assertTextEquals("No ingredients added yet")
      }

      ingredientButton { assertDoesNotExist() }
      potentialIngredientButton { assertDoesNotExist() }
      morePotentialIngredients { assertDoesNotExist() }
    }
  }

  @Test
  fun displayOnlyIngredients() {
    launchIngredientScreenWithTestData(manyIngredients, emptyList())

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      morePotentialIngredients { assertDoesNotExist() }
      potentialIngredientButton { assertDoesNotExist() }
      noIngredients { assertDoesNotExist() }

      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5ml")
      }

      composeTestRule.onAllNodesWithTag("Ingredient").assertCountEquals(5)
    }
  }

  @Test
  fun displayOnePotential() {
    launchIngredientScreenWithTestData(emptyList(), fewPotentialIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      potentialIngredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots")
      }

      morePotentialIngredients { assertDoesNotExist() }
      ingredientButton { assertDoesNotExist() }
      noIngredients { assertDoesNotExist() }

      composeTestRule.onAllNodesWithTag("PotentialIngredient").assertCountEquals(1)
    }
  }

  @Test
  fun displayManyPotential() {
    launchIngredientScreenWithTestData(emptyList(), manyPotentialIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      noIngredients { assertDoesNotExist() }
      ingredientButton { assertDoesNotExist() }

      potentialIngredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots")
      }

      morePotentialIngredients { assertIsDisplayed() }

      composeTestRule.onAllNodesWithTag("PotentialIngredient").assertCountEquals(3)
    }
  }

  @Test
  fun displayAll() {
    launchIngredientScreenWithTestData(manyIngredients, manyPotentialIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      noIngredients { assertDoesNotExist() }

      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5ml")
      }

      potentialIngredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots")
      }

      morePotentialIngredients { assertIsDisplayed() }

      composeTestRule.onAllNodesWithTag("Ingredient").assertCountEquals(5)
      composeTestRule.onAllNodesWithTag("PotentialIngredient").assertCountEquals(3)
    }
  }
}
