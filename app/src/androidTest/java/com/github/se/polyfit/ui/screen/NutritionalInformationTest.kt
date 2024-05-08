package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
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
import com.google.firebase.firestore.DocumentReference
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NutritionalInfoTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  private val mockMealRepo: MealRepository = mockk(relaxed = true)

  private val mockMealViewModel: MealViewModel = MealViewModel(mockMealRepo)

  private val meal =
      Meal(
          name = "Steak and frites",
          mealID = 1,
          ingredients =
              mutableListOf(
                  Ingredient(
                      "Steak",
                      1,
                      100.0,
                      MeasurementUnit.G,
                      NutritionalInformation(
                          mutableListOf(Nutrient("Calories", 2000.0, MeasurementUnit.CAL))))),
          occasion = MealOccasion.LUNCH)

  @Before
  fun setup() {
    mockkStatic(Log::class)

    val navigateBack = { mockNav.goBack() }
    val navigateForward = { mockNav.navigateToHome() }
    mockMealViewModel.setMealData(meal)

    composeTestRule.setContent { NutritionScreen(mockMealViewModel, navigateBack, navigateForward) }

    val mockkDocRef = mockk<DocumentReference>()
    coEvery { mockMealRepo.storeMeal(any<Meal>()) } returns mockkDocRef
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun topBarDisplayed() = runTest {
    ComposeScreen.onComposeScreen<NutritionalInformationTopBar>(composeTestRule) {
      title {
        assertIsDisplayed()
        assertTextEquals("Nutrition Facts")
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
  fun addRecipeButton() {
    ComposeScreen.onComposeScreen<NutritionalInformationBottomBar>(composeTestRule) {
      addToDiaryButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Add to Diary")
      }

      addRecipeButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Add Recipe")
        performClick()
      }

      // TODO: Update with proper test when functionality is implemented
      verify { Log.v("Add Recipe", "Clicked") }
    }
  }

  @Test
  fun addToDiaryButton() {
    ComposeScreen.onComposeScreen<NutritionalInformationBottomBar>(composeTestRule) {
      addRecipeButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Add Recipe")
      }

      addToDiaryButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Add to Diary")
        performClick()
      }

      verify { mockNav.navigateToHome() }
    }
  }

  @Test
  fun verifyBodyShown() {
    ComposeScreen.onComposeScreen<NutritionalInformationBody>(composeTestRule) {
      mealName {
        assertIsDisplayed()
        assertTextEquals(meal.name)
      }
    }
  }

  @Test
  fun verifySuccessfulNameChange() {
    ComposeScreen.onComposeScreen<NutritionalInformationBody>(composeTestRule) {
      val mealNameText = composeTestRule.onNodeWithTag("MealName")
      val editMealButton = composeTestRule.onNodeWithTag("EditMealButton")
      val mealNameTextInput = composeTestRule.onNodeWithTag("EditMealNameTextField")

      mealNameText.assertIsDisplayed()

      editMealButton.performClick()

      mealNameText.assertDoesNotExist()

      mealNameTextInput.assertIsDisplayed().performTextClearance()

      mealNameTextInput.performTextInput("Apple Pie")

      mealNameTextInput.performImeAction()

      mealNameTextInput.assertDoesNotExist()

      mealNameText.assertIsDisplayed().assertTextEquals("Apple Pie")
    }
  }

  @Test
  fun verifyMealNamePlaceholder() {
    ComposeScreen.onComposeScreen<NutritionalInformationBody>(composeTestRule) {
      val mealNameText = composeTestRule.onNodeWithTag("MealName")
      val editMealButton = composeTestRule.onNodeWithTag("EditMealButton")
      val mealNameTextInput = composeTestRule.onNodeWithTag("EditMealNameTextField")

      mealNameText.assertIsDisplayed()

      editMealButton.performClick()

      mealNameText.assertDoesNotExist()

      mealNameTextInput.assertIsDisplayed().performTextClearance()

      mealNameTextInput.performImeAction()

      mealNameText.assertIsDisplayed().assertTextEquals("Enter name here")
    }
  }

  // TODO: Test that incomplete meal can not be added to diary
}
