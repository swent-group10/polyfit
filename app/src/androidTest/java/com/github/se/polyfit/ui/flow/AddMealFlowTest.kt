package com.github.se.polyfit.ui.flow

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.components.selector.MealOccasionSelectorScreen
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.AddIngredientPopupBox
import com.github.se.polyfit.ui.screen.AdditionalMealInfoBottomBar
import com.github.se.polyfit.ui.screen.AdditionalMealInfoTopBar
import com.github.se.polyfit.ui.screen.HomeScreen
import com.github.se.polyfit.ui.screen.IngredientsBottomBar
import com.github.se.polyfit.ui.screen.IngredientsList
import com.github.se.polyfit.ui.screen.IngredientsTopBar
import com.github.se.polyfit.ui.screen.NutritionalInformationTopBar
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddMealFlowTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @Before
  fun setup() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      val navigation = Navigation(navController)
      NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) { HomeScreen() }
        composable(Route.AddMeal) {
          AddMealFlow(
              navigation::goBack,
              navigation::navigateToHome,
              mealId = null,
              MealViewModel(mockk(), mockk()))
        }
      }
      navigation.navigateToAddMeal()
    }
  }

  @Test
  fun ingredientScreenGoesBackAfterConfirm() {
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

      composeTestRule.onNodeWithTag("GoBack").assertExists().performClick()

      ingredientTitle { assertDoesNotExist() }
      backButton { assertDoesNotExist() }
    }

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton { assertDoesNotExist() }
    }
  }

  @Test
  fun ingredientScreenStaysIfDenied() {
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

      composeTestRule.onNodeWithTag("DenyButton").assertExists().performClick()

      ingredientTitle { assertExists() }
    }

    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton { assertDoesNotExist() }
    }
  }

  @Test
  fun additionalMealInfoIsShown() {
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientGradientButton { performClick() }
      composeTestRule.onNodeWithText("Enter an Ingredient...").performTextInput("apple")
      composeTestRule.onNodeWithTag("NutritionSizeInput Calories").performTextInput("1")
      composeTestRule.onNodeWithTag("NutritionSizeInput Total Weight").performTextInput("1")
      finishAddIngredientButton { performClick() }
    }

    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Done")
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<AdditionalMealInfoTopBar>(composeTestRule) {
      title { assertIsDisplayed() }
    }
  }

  @Test
  fun nutritionalInformationIsShown() {
    ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
      addIngredientGradientButton { performClick() }
      composeTestRule.onNodeWithText("Enter an Ingredient...").performTextInput("apple")
      composeTestRule.onNodeWithTag("NutritionSizeInput Calories").performTextInput("1")
      composeTestRule.onNodeWithTag("NutritionSizeInput Total Weight").performTextInput("1")
      finishAddIngredientButton { performClick() }
    }

    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<MealOccasionSelectorScreen>(composeTestRule) {
      dinnerButton {
        assertIsDisplayed()
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<AdditionalMealInfoBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<NutritionalInformationTopBar>(composeTestRule) {
      title {
        assertIsDisplayed()
        assertTextEquals("Nutrition Facts")
      }
    }
  }
}
