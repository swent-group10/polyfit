package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlin.test.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeRecommendationMoreDetailScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  private val mockRecipeRecommendationViewModel = mockk<RecipeRecommendationViewModel>()
  private var showIngredient = MutableLiveData<Boolean>()

  @Before
  fun setUp() {
    showIngredient = MutableLiveData(true)
    every { mockRecipeRecommendationViewModel.selectedRecipe } returns
        MutableLiveData(Recipe.default())
    every { mockRecipeRecommendationViewModel.showIngredient } returns showIngredient
    every { mockRecipeRecommendationViewModel.setShowIngredientFalse() } answers
        {
          showIngredient.postValue(false)
        }

    composeTestRule.setContent {
      RecipeRecommendationMoreDetailScreen(mockRecipeRecommendationViewModel)
    }
  }

  @Test
  fun testEverythingIsDisplayed() {

    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<RecipeRecommendationMoreDetailScreenScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      composeTestRule.onNodeWithTag("RecipeCard").assertExists().assertIsDisplayed()

      composeTestRule.onNodeWithTag("TitleAndToggleCard").assertExists().assertIsDisplayed()
    }
  }

  @Test
  fun testToggleButtons() {

    composeTestRule.waitForIdle()
    ComposeScreen.onComposeScreen<RecipeRecommendationMoreDetailScreenScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      composeTestRule
          .onNodeWithTag("tonalButton2")
          .assertExists()
          .assertIsDisplayed()
          .performClick()

      composeTestRule.onNodeWithTag("IngredientList").assertDoesNotExist()
    }
  }
}
