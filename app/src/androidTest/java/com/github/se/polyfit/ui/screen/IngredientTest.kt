package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  private val mockMealRepo: MealRepository = mockk()
  private val mockMealViewModel: MealViewModel = MealViewModel(mockMealRepo, mockk())

  @Before fun setup() {}

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  private fun launchIngredientScreenWithTestData(
      testIngredients: MutableList<Ingredient>,
  ) {
    val navigateBack = { mockNav.goBack() }
    val navigateForward = { mockNav.navigateToNutrition() }
    val testMeal =
        Meal(occasion = MealOccasion.OTHER, name = "Test Meal", ingredients = testIngredients)

    mockMealViewModel.setMealData(testMeal)

    composeTestRule.setContent {
      IngredientScreen(mockMealViewModel, navigateBack, navigateForward)
    }
  }

  private val manyIngredients =
      mutableListOf(
          Ingredient(
              "Olive Oil",
              5,
              5.0,
              MeasurementUnit.ML,
              NutritionalInformation(
                  mutableListOf(
                      Nutrient("Calories", 100.0, MeasurementUnit.CAL),
                      Nutrient("Fat", 10.0, MeasurementUnit.G),
                      Nutrient("Protein", 0.0, MeasurementUnit.G),
                      Nutrient("Carbs", 0.0, MeasurementUnit.G),
                  ))),
          Ingredient("Beef Tenderloin", 50, 50.0, MeasurementUnit.G),
          Ingredient("White Asparagus", 10, 10.0, MeasurementUnit.G),
          Ingredient("Corn", 4, 4.0, MeasurementUnit.G),
          Ingredient("Foie Gras", 100, 100.0, MeasurementUnit.G),
          Ingredient("Asperge", 27, 1.0, MeasurementUnit.OTHER),
      )

  @Test
  fun topBarDisplayed() {
    launchIngredientScreenWithTestData(mutableListOf())
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

      composeTestRule.onNodeWithTag("GoBack", useUnmergedTree = true).assertExists().performClick()

      verify { mockNav.goBack() }
      confirmVerified(mockNav)
    }
  }

  @Test
  fun bottomBarDisplayed() {
    launchIngredientScreenWithTestData(mutableListOf())
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
    launchIngredientScreenWithTestData(mutableListOf())
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientDialog { assertDoesNotExist() }

      addIngredientGradientButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

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
    launchIngredientScreenWithTestData(mutableListOf())
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientDialog { assertDoesNotExist() }

      ingredientButton { assertDoesNotExist() }

      addIngredientGradientButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      addIngredientDialog { assertIsDisplayed() }

      composeTestRule.onNodeWithText("Enter an Ingredient...").performTextInput("apple")

      composeTestRule.onNodeWithTag("NutritionSizeInput Calories").performTextInput("1")

      composeTestRule.onNodeWithTag("NutritionSizeInput Total Weight").performTextInput("1")

      finishAddIngredientButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      addIngredientDialog { assertDoesNotExist() }
    }
  }

  @Test
  fun doneButton() {
    launchIngredientScreenWithTestData(manyIngredients)
    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Done")
        performClick()
      }

      verify { mockNav.navigateToNutrition() }
    }
  }

  @Test
  fun noIngredientMessageDisplay() {
    launchIngredientScreenWithTestData(mutableListOf())
    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      noIngredients {
        assertIsDisplayed()
        assertTextEquals("No ingredients added.")
      }

      ingredientButton { assertDoesNotExist() }
    }
  }

  @Test
  fun displayOnlyIngredients() {
    launchIngredientScreenWithTestData(manyIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      noIngredients { assertDoesNotExist() }

      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5 ml")
        assertHasClickAction()
      }

      composeTestRule.onAllNodesWithTag("Ingredient").assertCountEquals(6)
    }
  }

  @Test
  fun expandIngredientExpandAndCollapse() {
    launchIngredientScreenWithTestData(manyIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5 ml")
        assertHasClickAction()
        performClick()
      }

      composeTestRule.onNodeWithTag("ExpandedIngredient").assertIsDisplayed()

      composeTestRule.onNodeWithTag("ExpandedIngredientName").assertTextContains("Olive Oil")

      composeTestRule.onNodeWithTag("NutritionInfoContainer").assertIsDisplayed()

      composeTestRule.onNodeWithTag("TopRightIconInGradientBox").assertIsDisplayed().performClick()

      composeTestRule.onNodeWithTag("ExpandedIngredient").assertDoesNotExist()

      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5 ml")
      }
    }
  }

  @Test
  fun deleteIngredient() = runTest {
    launchIngredientScreenWithTestData(manyIngredients)

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton {
        assertIsDisplayed()
        assertTextContains("Olive Oil 5 ml")
        assertHasClickAction()
        performClick()
      }

      composeTestRule.onNodeWithTag("ExpandedIngredient").assertIsDisplayed()

      composeTestRule.onNodeWithTag("ExpandedIngredientName").assertTextContains("Olive Oil")

      composeTestRule.onNodeWithTag("NutritionInfoContainer").assertIsDisplayed()

      composeTestRule.onNodeWithTag("DeleteIngredientButton").assertIsDisplayed().performClick()

      composeTestRule.onAllNodesWithTag("Ingredient").assertCountEquals(5)
    }
  }

  @Test
  fun doneButtonDisabledWithoutIngredients() {
    launchIngredientScreenWithTestData(mutableListOf())

    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertTextEquals("Done")
        assertIsNotEnabled()
      }
    }
  }
}
