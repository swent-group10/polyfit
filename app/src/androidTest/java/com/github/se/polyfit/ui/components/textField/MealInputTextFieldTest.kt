package com.github.se.polyfit.ui.components.textField

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealInputTextFieldTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  val mockCollapse: () -> Unit = mockk()
  val mockMealChange: (Meal) -> Unit = mockk()

  fun setup(meals: List<Meal> = listOf()) {
    every { mockCollapse() } just Runs
    every { mockMealChange(any()) } just Runs

    composeTestRule.setContent {
      MealInputTextField(
          meals = meals, onCollapseSearchBar = mockCollapse, onMealChange = mockMealChange)
    }
  }

  @Test
  fun everythingIsDisplayed() {
    setup()
    ComposeScreen.onComposeScreen<MealInputTextFieldScreen>(composeTestRule) {
      searchMealBar {
        assertExists()
        assertIsDisplayed()
      }

      placeholder {
        assertExists()
        assertIsDisplayed()
      }

      meal { assertDoesNotExist() }

      mealName { assertDoesNotExist() }

      mealCalories { assertDoesNotExist() }

      scrollableList { assertDoesNotExist() }
    }
  }

  @Test
  fun mealsDisplayWhenSearched() {
    val meal = Meal(MealOccasion.DINNER, "eggs", 1, 102.2, NutritionalInformation(mutableListOf()))
    meal.addIngredient(
        Ingredient(
            "milk",
            1,
            102.0,
            MeasurementUnit.MG,
            NutritionalInformation(mutableListOf(Nutrient("calories", 1.0, MeasurementUnit.G)))))
    setup(listOf(meal))

    ComposeScreen.onComposeScreen<MealInputTextFieldScreen>(composeTestRule) {
      searchMealBar {
        assertExists()
        assertIsDisplayed()
        performClick()
      }

      placeholder {
        assertExists()
        assertIsDisplayed()
      }

      meal {
        assertExists()
        assertIsDisplayed()
        assertTextContains("eggs")
        assertTextContains("1.0 kcal")
        performClick()
      }

      verify(exactly = 1) { mockMealChange(meal) }
      verify(exactly = 1) { mockCollapse() }

      scrollableList { assertExists() }
    }
  }
}
