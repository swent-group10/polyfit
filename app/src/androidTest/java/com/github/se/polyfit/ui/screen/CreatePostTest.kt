package com.github.se.polyfit.ui.screen

import android.graphics.Bitmap
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.components.textField.MealInputTextFieldScreen
import com.github.se.polyfit.viewmodel.post.CreatePostViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePostTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  val mockNavForward: () -> Unit = mockk()
  val mockNavBack: () -> Unit = mockk()
  private val mockMealRepository = mockk<MealRepository>(relaxed = true)
  private val mockPostFirebaseRepository = mockk<PostFirebaseRepository>(relaxed = true)
  private lateinit var viewModel: CreatePostViewModel

  fun setup(meals: List<Meal> = listOf()) {
    coEvery { mockMealRepository.getAllMeals() } returns meals
    every { mockNavForward() } just Runs
    every { mockNavBack() } just Runs

    viewModel = CreatePostViewModel(mockMealRepository, mockPostFirebaseRepository)
    viewModel.setBitMap(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
    composeTestRule.setContent { CreatePostScreen(mockNavBack, mockNavForward, viewModel) }
  }

  fun everythingIsDisplayed() {
    setup()
    ComposeScreen.onComposeScreen<CreatePostScreen>(composeTestRule) {
      pictureSelector {
        assertExists()
        assertIsDisplayed()
      }

      postDescription {
        assertExists()
        assertIsDisplayed()
      }

      mealSelector {
        assertExists()
        assertIsDisplayed()
      }
    }

    ComposeScreen.onComposeScreen<CreatePostTopBar>(composeTestRule) {
      title {
        assertExists()
        assertIsDisplayed()
        assertTextEquals("New Post")
      }

      backButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
      }
    }

    ComposeScreen.onComposeScreen<CreatePostBottomBar>(composeTestRule) {
      postButton {
        assertExists()
        assertIsDisplayed()
        assertIsNotEnabled()
      }
    }
  }

  @Test
  fun selectingMealEnablesPostButton() {
    val meal = Meal(MealOccasion.DINNER, "eggs", "1", 102.2)
    meal.addIngredient(
        Ingredient(
            "milk",
            1,
            102.0,
            MeasurementUnit.MG,
            NutritionalInformation(mutableListOf(Nutrient("calories", 1.0, MeasurementUnit.G)))))

    setup(listOf(meal))

    ComposeScreen.onComposeScreen<CreatePostScreen>(composeTestRule) {
      mealSelector {
        assertExists()
        assertIsDisplayed()
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

    ComposeScreen.onComposeScreen<CreatePostBottomBar>(composeTestRule) {
      postButton {
        assertExists()
        assertIsDisplayed()
        assertIsEnabled()
      }
    }
  }
}
