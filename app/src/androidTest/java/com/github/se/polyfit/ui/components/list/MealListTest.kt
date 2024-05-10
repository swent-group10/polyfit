package com.github.se.polyfit.ui.components.list

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.meal.MealTagColor
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MealListTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  fun setContent(
      meals: List<Meal> = emptyList(),
      occasion: MealOccasion = MealOccasion.BREAKFAST,
      navigateTo: (String?) -> Unit = {}
  ) {
    composeTestRule.setContent { MealList(meals, occasion, navigateTo) }
  }

  @Test
  fun everythingIsDisplayed() {
    setContent()

    ComposeScreen.onComposeScreen<MealListScreen>(composeTestRule) {
      occasionTitle {
        assertIsDisplayed()
        assertTextEquals("Breakfast")
      }

      totalCalories {
        assertIsDisplayed()
        assertTextEquals("0.0")
      }
    }
  }

  private val ingredient =
      Ingredient(
          name = "Tomato",
          id = 1,
          amount = 100.0,
          unit = MeasurementUnit.G,
          nutritionalInformation =
              NutritionalInformation(
                  mutableListOf(
                      Nutrient("calories", 10.0, MeasurementUnit.CAL),
                      Nutrient("totalWeight", 10.0, MeasurementUnit.G),
                      Nutrient("carbohydrates", 10.0, MeasurementUnit.G),
                      Nutrient("fat", 10.0, MeasurementUnit.G),
                      Nutrient("protein", 10.0, MeasurementUnit.G))))
  private val tags = mutableListOf(MealTag("Tag 1", MealTagColor.BRIGHTORANGE))
  private val meal1 =
      Meal(
          name = "Meal 1",
          ingredients = mutableListOf(ingredient),
          tags = tags,
          occasion = MealOccasion.BREAKFAST)

  private val meal2 =
      Meal(
          name = "Meal 2",
          ingredients = mutableListOf(ingredient),
          tags = tags,
          occasion = MealOccasion.BREAKFAST)

  @Test
  fun oneMealDisplays() {
    setContent(meals = listOf(meal1))

    ComposeScreen.onComposeScreen<MealListScreen>(composeTestRule) {
      mealCard { assertIsDisplayed() }

      mealName {
        assertIsDisplayed()
        assertTextEquals("Meal 1")
      }

      mealCalories {
        assertIsDisplayed()
        assertTextEquals("10.0 kcal")
      }

      totalCalories {
        assertIsDisplayed()
        assertTextEquals("10.0")
      }

      composeTestRule.onNodeWithTag("MealTagList", true).assertIsDisplayed()
    }
  }

  @Test
  fun manyMeals() {
    setContent(meals = listOf(meal1, meal2))

    ComposeScreen.onComposeScreen<MealListScreen>(composeTestRule) {
      totalCalories {
        assertIsDisplayed()
        assertTextEquals("20.0")
      }

      composeTestRule.onAllNodesWithTag("MealCard").assertCountEquals(2)
    }
  }

  // TODO: Test editing once implemented
}
