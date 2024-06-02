package com.github.se.polyfit.ui.components.ingredients

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
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
class IngredientNutritionEditFieldsTest : TestCase() {
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

  @Test
  fun nutritionFieldIsFunctional() {
    composeTestRule.setContent {
      IngredientNutritionEditFields(
          nutritionFields =
              mutableListOf(
                  Nutrient("Total Weight", 0.0, MeasurementUnit.G),
                  Nutrient("Calories", 0.0, MeasurementUnit.CAL),
                  Nutrient("Carbohydrates", 0.0, MeasurementUnit.G),
                  Nutrient("Fat", 0.0, MeasurementUnit.G),
                  Nutrient("Protein", 0.0, MeasurementUnit.G),
              ))
    }

    ComposeScreen.onComposeScreen<IngredientNutritionEditFieldsScreen>(composeTestRule) {
      servingSizeContainer { assertIsDisplayed() }
      caloriesContainer { assertIsDisplayed() }
      carbsContainer { assertIsDisplayed() }
      fatContainer { assertIsDisplayed() }
      proteinContainer { assertIsDisplayed() }

      servingSizeLabel {
        assertIsDisplayed()
        assertTextContains("Total Weight")
      }
      caloriesLabel {
        assertIsDisplayed()
        assertTextContains("Calories")
      }
      carbsLabel {
        assertIsDisplayed()
        assertTextContains("Carbohydrates")
      }
      fatLabel {
        assertIsDisplayed()
        assertTextContains("Fat")
      }
      proteinLabel {
        assertIsDisplayed()
        assertTextContains("Protein")
      }

      servingSizeInput {
        assertIsDisplayed()
        assertTextContains("0.0")
        performTextClearance()
        performTextInput("1")
        assertTextContains("1.0")
      }
      caloriesInput {
        assertIsDisplayed()
        assertTextContains("0.0")
        performTextClearance()
        performTextInput("1")
        assertTextContains("1.0")
      }
      carbsInput {
        assertIsDisplayed()
        assertTextContains("0.0")
        performTextClearance()
        performTextInput("1")
        assertTextContains("1.0")
      }
      caloriesInput { assertTextContains("1.0") }
      fatInput {
        assertIsDisplayed()
        assertTextContains("0.0")
        performTextClearance()
        performTextInput("1")
        assertTextContains("1.0")
      }
      proteinInput {
        assertIsDisplayed()
        assertTextContains("0.0")
        performTextClearance()
        performTextInput("1")
        assertTextContains("1.0")
      }

      servingSizeUnit {
        assertIsDisplayed()
        assertTextContains("g")
      }
      caloriesUnit {
        assertIsDisplayed()
        assertTextContains("cal")
      }
      carbsUnit {
        assertIsDisplayed()
        assertTextContains("g")
      }
      fatUnit {
        assertIsDisplayed()
        assertTextContains("g")
      }
      proteinUnit {
        assertIsDisplayed()
        assertTextContains("g")
      }
    }
  }
}
