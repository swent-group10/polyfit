// package com.github.se.polyfit.ui.components
//
// import androidx.compose.ui.test.assertCountEquals
// import androidx.compose.ui.test.assertIsDisplayed
// import androidx.compose.ui.test.junit4.createComposeRule
// import androidx.compose.ui.test.onAllNodesWithTag
// import androidx.compose.ui.test.onNodeWithTag
// import androidx.compose.ui.test.onNodeWithText
// import androidx.compose.ui.test.performScrollToIndex
// import androidx.test.ext.junit.runners.AndroidJUnit4
// import com.github.se.polyfit.model.meal.Meal
// import com.github.se.polyfit.model.meal.MealOccasion
// import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
// import com.github.se.polyfit.model.nutritionalInformation.Nutrient
// import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
// import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
// import io.github.kakaocup.compose.node.element.ComposeScreen
// import org.junit.Rule
// import org.junit.Test
// import org.junit.runner.RunWith
//
// @RunWith(AndroidJUnit4::class)
// class NutritionalInformationTest : TestCase() {
//  @get:Rule val composeTestRule = createComposeRule()
//
//  private val onlyCalories =
//      NutritionalInformation(
//          mutableListOf(
//              Nutrient("calories", 100.0, MeasurementUnit.CAL),
//          ))
//
//  private val noCaloriesInfo =
//      NutritionalInformation(
//          mutableListOf(
//              Nutrient("totalWeight", 100.0, MeasurementUnit.G),
//              Nutrient("fat", 10.0, MeasurementUnit.G),
//              Nutrient("saturatedFat", 3.0, MeasurementUnit.G),
//              Nutrient("carbohydrates", 20.0, MeasurementUnit.G),
//          ))
//
//  private val nutritionalInformation =
//      NutritionalInformation(
//          mutableListOf(
//              Nutrient("totalWeight", 100.0, MeasurementUnit.G),
//              Nutrient("calories", 200.0, MeasurementUnit.CAL),
//              Nutrient("fat", 10.0, MeasurementUnit.G),
//              Nutrient("saturatedFat", 3.0, MeasurementUnit.G),
//              Nutrient("carbohydrates", 20.0, MeasurementUnit.G),
//              Nutrient("netCarbohydrates", 15.0, MeasurementUnit.G),
//              Nutrient("sugar", 5.0, MeasurementUnit.G),
//              Nutrient("cholesterol", 0.0, MeasurementUnit.MG),
//              Nutrient("sodium", 0.0, MeasurementUnit.MG),
//              Nutrient("protein", 15.0, MeasurementUnit.G),
//              Nutrient("vitaminC", 0.0, MeasurementUnit.MG),
//              Nutrient("manganese", 0.0, MeasurementUnit.UG),
//              Nutrient("fiber", 2.0, MeasurementUnit.G),
//              Nutrient("vitaminB6", 0.0, MeasurementUnit.MG),
//              Nutrient("copper", 0.0, MeasurementUnit.UG),
//              Nutrient("vitaminB1", 0.0, MeasurementUnit.MG),
//              Nutrient("folate", 0.0, MeasurementUnit.UG),
//              Nutrient("potassium", 0.0, MeasurementUnit.MG),
//              Nutrient("magnesium", 0.0, MeasurementUnit.MG),
//              Nutrient("vitaminB3", 0.0, MeasurementUnit.MG),
//              Nutrient("vitaminB5", 0.0, MeasurementUnit.MG),
//              Nutrient("vitaminB2", 0.0, MeasurementUnit.MG),
//              Nutrient("iron", 0.0, MeasurementUnit.MG),
//              Nutrient("calcium", 0.0, MeasurementUnit.MG),
//              Nutrient("vitaminA", 0.0, MeasurementUnit.IU),
//              Nutrient("zinc", 0.0, MeasurementUnit.MG),
//              Nutrient("phosphorus", 0.0, MeasurementUnit.MG),
//              Nutrient("vitaminK", 0.0, MeasurementUnit.UG),
//              Nutrient("selenium", 0.0, MeasurementUnit.UG),
//              Nutrient("vitaminE", 0.0, MeasurementUnit.IU)))
//
//  private fun meal(nutritionalInformation: NutritionalInformation) =
//      Meal(
//          name = "Steak & Veggie",
//          ingredients = mutableListOf(),
//          occasion = MealOccasion.DINNER,
//          mealID = 20,
//          nutritionalInformation = nutritionalInformation)
//
//  private fun setup(meal: Meal) {
//    composeTestRule.setContent { NutritionalInformation(meal = meal) }
//  }
//
//  @Test
//  fun noCaloriesAvailable() {
//    val meal = meal(noCaloriesInfo)
//    setup(meal)
//
//    ComposeScreen.onComposeScreen<NutritionalInformationScreen>(composeTestRule) {
//      noMealInfo {
//        assertIsDisplayed()
//        assertTextEquals("No nutritional information available")
//      }
//
//      mealName {
//        assertIsDisplayed()
//        assertTextEquals(meal.name)
//      }
//
//      nutrient { assertDoesNotExist() }
//      nutrientAmount { assertDoesNotExist() }
//      nutrientName { assertDoesNotExist() }
//    }
//  }
//
//  @Test
//  fun regularMeal() {
//    val meal = meal(nutritionalInformation)
//    setup(meal)
//
//    ComposeScreen.onComposeScreen<NutritionalInformationScreen>(composeTestRule) {
//      noMealInfo { assertDoesNotExist() }
//
//      mealName {
//        assertIsDisplayed()
//        assertTextEquals(meal.name)
//      }
//
//      nutrient { assertIsDisplayed() }
//
//      nutrientName {
//        assertIsDisplayed()
//        assertTextEquals("Calories")
//      }
//
//      nutrientAmount {
//        assertIsDisplayed()
//        assertTextEquals("200 cal")
//      }
//
//      val lazyCol = composeTestRule.onNodeWithTag("NutritionalInformation")
//
//      lazyCol.performScrollToIndex(meal.nutritionalInformation.nutrients.size)
//
//      composeTestRule.onNodeWithText("Vitamin E").assertIsDisplayed()
//    }
//  }
//
//  @Test
//  fun onlyCalories() {
//    val meal = meal(onlyCalories)
//    setup(meal)
//
//    ComposeScreen.onComposeScreen<NutritionalInformationScreen>(composeTestRule) {
//      noMealInfo { assertDoesNotExist() }
//
//      mealName {
//        assertIsDisplayed()
//        assertTextEquals(meal.name)
//      }
//
//      nutrient { assertIsDisplayed() }
//
//      nutrientName {
//        assertIsDisplayed()
//        assertTextEquals("Calories")
//      }
//
//      nutrientAmount {
//        assertIsDisplayed()
//        assertTextEquals("100 cal")
//      }
//
//      composeTestRule.onAllNodesWithTag("NutrientInfo").assertCountEquals(1)
//    }
//  }
// }
