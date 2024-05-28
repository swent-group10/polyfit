package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.components.textField.MealInputTextFieldScreen
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealSelectorTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  private val mockSetPostMeal: (Meal) -> Unit = mockk(relaxed = true)
  private val mockSetSelectedMeal: (Meal) -> Unit = mockk(relaxed = true)
  private var selectedMeal: Meal = Meal.default()

  fun setup(meals: List<Meal> = listOf()) {
    composeTestRule.setContent {
      MealSelector(
          selectedMeal = selectedMeal,
          setSelectedMeal = mockSetSelectedMeal,
          meals = meals,
          setPostMeal = mockSetPostMeal)
    }
  }

  @Test
  fun defaultDisplay() {
    setup()

    ComposeScreen.onComposeScreen<MealSelectorScreen>(composeTestRule) {
      mealSelectorRow {
        assertIsDisplayed()
        assertTextContains("Share your recipe")
      }

      searchMealBar { assertDoesNotExist() }
      mealDetails { assertDoesNotExist() }
      carbs { assertDoesNotExist() }
      protein { assertDoesNotExist() }
      fat { assertDoesNotExist() }
      ingredient { assertDoesNotExist() }
    }
  }

  @Test
  fun selectingMealReplacesSelector() {
    val meal = Meal(MealOccasion.DINNER, "eggs", "1", "testUserID", 102.2)
    meal.addIngredient(
        Ingredient(
            "milk",
            1,
            102.0,
            MeasurementUnit.MG,
            NutritionalInformation(mutableListOf(Nutrient("calories", 1.0, MeasurementUnit.G)))))
    setup(listOf(meal))

    ComposeScreen.onComposeScreen<MealSelectorScreen>(composeTestRule) {
      mealSelectorRow {
        assertIsDisplayed()
        assertTextContains("Share your recipe")
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<MealInputTextFieldScreen>(composeTestRule) {
      searchMealBar {
        assertIsDisplayed()
        performClick()
      }
      meal {
        assertIsDisplayed()
        performClick()
      }
    }

    verify { mockSetSelectedMeal(meal) }
  }

  @Test
  fun selectedMealIsDisplayed() {
    val meal = Meal(MealOccasion.DINNER, "eggs", "1", "testUserID", 102.2)
    meal.addIngredient(
        Ingredient(
            "milk",
            1,
            102.0,
            MeasurementUnit.MG,
            NutritionalInformation(mutableListOf(Nutrient("calories", 1.0, MeasurementUnit.G)))))
    selectedMeal = meal
    setup()

    ComposeScreen.onComposeScreen<MealSelectorScreen>(composeTestRule) {
      mealSelectorRow {
        assertIsDisplayed()
        assertTextContains("eggs")
        assertTextContains("1.00 kCal")
      }

      carbs { assertIsDisplayed() }
      protein { assertIsDisplayed() }
      fat { assertIsDisplayed() }
    }
  }
}
