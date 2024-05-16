package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.button.LikeButtonScreen
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeRecommendationScreenTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @Test
  fun testEverythingIsDisplayed() {
    val mockkNavigation = mockk<NavHostController>(relaxed = true)
    val mockkRecommendationViewModel = mockk<RecipeRecommendationViewModel>()

    val recipeList = mutableListOf<Recipe>()
    for (i in 1..10) {
      recipeList.add(Recipe.default())
    }
    coEvery { mockkRecommendationViewModel.ingredientList() } returns listOf("apple", "banana")
    coEvery { mockkRecommendationViewModel.recipeFromIngredients(any()) } returns recipeList

    composeTestRule.setContent {
      RecipeRecommendationScreen(mockkNavigation, mockkRecommendationViewModel)
    }
    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<LikeButtonScreen>(composeTestRule) {
      composeTestRule.onNodeWithText("Recommended Recipes").assertIsDisplayed()
      composeTestRule.onNodeWithTag("RecipeList").assertExists().assertIsDisplayed()
    }
  }
}
