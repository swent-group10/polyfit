package com.github.se.polyfit.ui.components.card

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlin.test.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TitleAndToggleCardTest {

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  private val mockRecipeRecommendationViewModel = mockk<RecipeRecommendationViewModel>()

  @Before
  fun setUp() {
    // Given
    every { mockRecipeRecommendationViewModel.showIngredient } returns MutableLiveData(true)
    every { mockRecipeRecommendationViewModel.setShowIngredientTrue() } just runs
    every { mockRecipeRecommendationViewModel.setShowIngredientFalse() } just runs
  }

  @Test
  fun testEverythingIsDisplayed() {

    composeTestRule.waitForIdle()

    composeTestRule.setContent {
      TitleAndToggleCard("Title", "Toggle", "Toggle2", mockRecipeRecommendationViewModel)
    }

    ComposeScreen.onComposeScreen<TitleAndToggleCardScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      composeTestRule
          .onNodeWithTag("tonalButton1")
          .assertExists()
          .assertHasClickAction()
          .performClick()

      verify { mockRecipeRecommendationViewModel.setShowIngredientTrue() }

      composeTestRule
          .onNodeWithTag("tonalButton2")
          .assertExists()
          .assertHasClickAction()
          .performClick()

      verify { mockRecipeRecommendationViewModel.setShowIngredientFalse() }

      composeTestRule.onNodeWithTag("RecipeRecTitle").assertExists().assertIsDisplayed()
    }
  }
}
