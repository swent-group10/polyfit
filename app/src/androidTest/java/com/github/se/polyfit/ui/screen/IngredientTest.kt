package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
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
class IngredientTests : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  @Before
  fun setup() {
    mockkStatic(Log::class)
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  private fun launchIngredientScreenWithTestData(
      testIngredients: List<Ingredient>,
      testPotentials: List<Ingredient>
  ) {
    composeTestRule.setContent { IngredientScreen(mockNav, testIngredients, testPotentials) }
  }

  private val manyIngredients =
      listOf(
          Ingredient("Olive Oil", 5, 5.0, MeasurementUnit.ML),
          Ingredient("Beef Tenderloin", 50, 50.0, MeasurementUnit.G),
          Ingredient("White Asparagus", 10, 10.0, MeasurementUnit.G),
          Ingredient("Corn", 4, 4.0, MeasurementUnit.G),
          Ingredient("Foie Gras", 100, 100.0, MeasurementUnit.G),
      )
  private val fewPotentialIngredients =
      listOf(
          Ingredient("Carrots", 100, 100.0, MeasurementUnit.G),
      )

  private val manyPotentialIngredients =
      listOf(
          Ingredient("Carrots", 100, 100.0, MeasurementUnit.G),
          Ingredient("Peas", 100, 100.0, MeasurementUnit.G),
          Ingredient("Worcestershire Sauce", 15, 15.0, MeasurementUnit.ML),
          Ingredient("Salt", 5, 5.0, MeasurementUnit.G),
          Ingredient("Pepper", 5, 5.0, MeasurementUnit.G),
          Ingredient("Garlic", 1, 1.0, MeasurementUnit.UNIT),
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
        assertContentDescriptionEquals("Back")
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
  fun openCloseAddIngredientPopup() {
    launchIngredientScreenWithTestData(emptyList(), emptyList())
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientDialog { assertDoesNotExist() }

      addIngredientGradientButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      verify { Log.v("Add Ingredient", "Clicked") }

      // make sure cross icon button closes popup properly
      addIngredientDialog { assertIsDisplayed() }

      closePopupIcon {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      addIngredientDialog { assertDoesNotExist() }
    }
  }

  @Test
  fun addNewIngredientToList() {
    launchIngredientScreenWithTestData(emptyList(), emptyList())
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientDialog { assertDoesNotExist() }

      addIngredientGradientButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      addIngredientDialog { assertIsDisplayed() }

      finishAddIngredientButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      // TODO: Check that ingredient is properly added to the ingredient list

      addIngredientDialog { assertDoesNotExist() }
    }
  }

  @Test
  fun doneButton() {
    launchIngredientScreenWithTestData(emptyList(), emptyList())
    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Done")
        performClick()
      }

      // TODO: @April Update with proper test
      verify { Log.v("Finished", "Clicked") }
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
      morePotentialIngredientsButton { assertDoesNotExist() }
    }
  }

  @Test
  fun displayOnlyIngredients() {
    launchIngredientScreenWithTestData(manyIngredients, emptyList())

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      morePotentialIngredientsButton { assertDoesNotExist() }
      potentialIngredientButton { assertDoesNotExist() }
      noIngredients { assertDoesNotExist() }

      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5.0ML")
        assertHasClickAction()
      }

      composeTestRule.onAllNodesWithTag("Ingredient").assertCountEquals(5)
    }
  }

  @Test
  fun expandIngredient() {
    launchIngredientScreenWithTestData(manyIngredients, emptyList())

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5.0ML")
        assertHasClickAction()
        performClick()
      }

      // TODO: @April Update with proper test
      verify { Log.v("Expand Ingredients", "Clicked") }
    }
  }

  @Test
  fun displayOnePotential() {
    launchIngredientScreenWithTestData(emptyList(), fewPotentialIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      potentialIngredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots")
        assertContentDescriptionEquals("Add Carrots")
        assertHasClickAction()
      }

      morePotentialIngredientsButton { assertDoesNotExist() }
      ingredientButton { assertDoesNotExist() }
      noIngredients { assertDoesNotExist() }

      composeTestRule
          .onAllNodesWithTag("PotentialIngredient")
          .assertCountEquals(fewPotentialIngredients.size)
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
        assertHasClickAction()
      }

      morePotentialIngredientsButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertContentDescriptionEquals("More Options")
      }

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
        assertTextContains("Olive Oil 5.0ML")
        assertHasClickAction()
      }

      potentialIngredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots")
        assertHasClickAction()
      }

      morePotentialIngredientsButton {
        assertIsDisplayed()
        assertHasClickAction()
      }

      composeTestRule.onAllNodesWithTag("Ingredient").assertCountEquals(manyIngredients.size)
      composeTestRule.onAllNodesWithTag("PotentialIngredient").assertCountEquals(3)
    }
  }

  @Test
  fun addPotentialIngredient() {
    launchIngredientScreenWithTestData(emptyList(), fewPotentialIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton { assertDoesNotExist() }

      potentialIngredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots")
        assertHasClickAction()
        performClick()
      }

      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Carrots 100.0G")
      }

      potentialIngredientButton { assertDoesNotExist() }
    }
  }

  @Test
  fun expandPotentialIngredients() {
    launchIngredientScreenWithTestData(emptyList(), manyPotentialIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      composeTestRule.onAllNodesWithTag("PotentialIngredient").assertCountEquals(3)

      morePotentialIngredientsButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
        assertDoesNotExist()
      }

      composeTestRule
          .onAllNodesWithTag("PotentialIngredient")
          .assertCountEquals(manyPotentialIngredients.size)
    }
  }
}
