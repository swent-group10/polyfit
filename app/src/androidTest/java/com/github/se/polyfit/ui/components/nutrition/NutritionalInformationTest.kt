package com.github.se.polyfit.ui.components.nutrition

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NutritionalInformationTest : TestCase() {
  private val context = ApplicationProvider.getApplicationContext<Context>()

  @get:Rule val composeTestRule = createComposeRule()

  private val onlyCalories =
      Ingredient(
          name = "Test",
          id = 0,
          amount = 1.0,
          unit = MeasurementUnit.UNIT,
          nutritionalInformation =
              NutritionalInformation(
                  mutableListOf(
                      Nutrient("calories", 100.0, MeasurementUnit.CAL),
                  )))

  private val noCaloriesInfo =
      Ingredient(
          name = "Test",
          id = 0,
          amount = 1.0,
          unit = MeasurementUnit.UNIT,
          nutritionalInformation =
              NutritionalInformation(
                  mutableListOf(
                      Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                      Nutrient("fat", 10.0, MeasurementUnit.G),
                      Nutrient("saturatedFat", 3.0, MeasurementUnit.G),
                      Nutrient("carbohydrates", 20.0, MeasurementUnit.G),
                  )))

  private val nutritionalInformation =
      Ingredient(
          name = "Test",
          id = 0,
          amount = 1.0,
          unit = MeasurementUnit.UNIT,
          nutritionalInformation =
              NutritionalInformation(
                  mutableListOf(
                      Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                      Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                      Nutrient("Fat", 10.0, MeasurementUnit.G),
                      Nutrient("Saturated Fat", 3.0, MeasurementUnit.G),
                      Nutrient("Carbohydrates", 20.0, MeasurementUnit.G),
                      Nutrient("Net Carbohydrates", 15.0, MeasurementUnit.G),
                      Nutrient("Sugar", 5.0, MeasurementUnit.G),
                      Nutrient("Cholesterol", 0.0, MeasurementUnit.MG),
                      Nutrient("Sodium", 0.0, MeasurementUnit.MG),
                      Nutrient("Protein", 15.0, MeasurementUnit.G),
                      Nutrient("Vitamin C", 0.0, MeasurementUnit.MG),
                      Nutrient("Manganese", 0.0, MeasurementUnit.UG),
                      Nutrient("Fiber", 2.0, MeasurementUnit.G),
                      Nutrient("Vitamin B6", 0.0, MeasurementUnit.MG),
                      Nutrient("Copper", 0.0, MeasurementUnit.UG),
                      Nutrient("Vitamin B1", 0.0, MeasurementUnit.MG),
                      Nutrient("Folate", 0.0, MeasurementUnit.UG),
                      Nutrient("Potassium", 0.0, MeasurementUnit.MG),
                      Nutrient("Magnesium", 0.0, MeasurementUnit.MG),
                      Nutrient("Vitamin B3", 0.0, MeasurementUnit.MG),
                      Nutrient("Vitamin B5", 0.0, MeasurementUnit.MG),
                      Nutrient("Vitamin B2", 0.0, MeasurementUnit.MG),
                      Nutrient("Iron", 0.0, MeasurementUnit.MG),
                      Nutrient("Calcium", 0.0, MeasurementUnit.MG),
                      Nutrient("Vitamin A", 0.0, MeasurementUnit.IU),
                      Nutrient("Zinc", 0.0, MeasurementUnit.MG),
                      Nutrient("Phosphorus", 0.0, MeasurementUnit.MG),
                      Nutrient("Vitamin K", 0.0, MeasurementUnit.UG),
                      Nutrient("Selenium", 0.0, MeasurementUnit.UG),
                      Nutrient("Vitamin E", 0.0, MeasurementUnit.IU))))

  private fun meal(ingredient: Ingredient) =
      Meal(
          name = "Steak & Veggie",
          ingredients = mutableListOf(ingredient),
          occasion = MealOccasion.DINNER,
          mealID = 20,
          nutritionalInformation = NutritionalInformation(mutableListOf()))

  private fun setup(meal: Meal) {
    composeTestRule.setContent {
      NutritionalInformation(mealViewModel = MealViewModel(mockk()).apply { setMealData(meal) })
    }
  }

  @Test
  fun noCaloriesAvailable() {
    val meal = meal(noCaloriesInfo)
    setup(meal)

    ComposeScreen.onComposeScreen<NutritionalInformationScreen>(composeTestRule) {
      noMealInfo {
        assertIsDisplayed()
        assertTextEquals("No nutritional information available")
      }

      mealName {
        assertIsDisplayed()
        assertTextEquals(meal.name)
      }

      nutrient { assertDoesNotExist() }
      nutrientAmount { assertDoesNotExist() }
      nutrientName { assertDoesNotExist() }
    }
  }

  @Test
  fun regularMeal() {
    val meal = meal(nutritionalInformation)
    setup(meal)

    ComposeScreen.onComposeScreen<NutritionalInformationScreen>(composeTestRule) {
      noMealInfo { assertDoesNotExist() }

      mealName {
        assertIsDisplayed()
        assertTextEquals(meal.name)
      }

      nutrient { assertIsDisplayed() }

      nutrientName {
        assertIsDisplayed()
        assertTextEquals("Calories")
      }

      nutrientAmount {
        assertIsDisplayed()
        assertTextEquals("200 cal")
      }

      val lazyCol = composeTestRule.onNodeWithTag("NutritionalInformation")

      lazyCol.performScrollToIndex(meal.nutritionalInformation.nutrients.size)

      composeTestRule.onNodeWithText("Vitamin E").assertIsDisplayed()
    }
  }

  @Test
  fun onlyCalories() {
    val meal = meal(onlyCalories)
    setup(meal)

    ComposeScreen.onComposeScreen<NutritionalInformationScreen>(composeTestRule) {
      noMealInfo { assertDoesNotExist() }

      mealName {
        assertIsDisplayed()
        assertTextEquals(meal.name)
      }

      nutrient { assertIsDisplayed() }

      nutrientName {
        assertIsDisplayed()
        assertTextEquals("Calories")
      }

      nutrientAmount {
        assertIsDisplayed()
        assertTextEquals("100 cal")
      }

      composeTestRule.onAllNodesWithTag("NutrientInfo").assertCountEquals(1)
    }
  }
}
