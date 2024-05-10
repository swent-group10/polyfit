package com.github.se.polyfit.ui.recipe

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.recipe.RecipeCard
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import org.junit.Rule

class RecipeCardTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @Test
  fun testEverythingIsDisplayed() {
    composeTestRule.setContent { RecipeCard(Recipe.default()) }
    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<RecipeCardScreen>(composeTestRule) {
      assertIsDisplayed()

      composeTestRule
          .onNodeWithText(Recipe.default().title)
          .assertIsDisplayed()
          .assertTextContains(Recipe.default().title)

      composeTestRule.onNodeWithTag("RecipeImage", useUnmergedTree = true).assertIsDisplayed()
      composeTestRule.onNodeWithTag("LikeButton", useUnmergedTree = true).assertIsDisplayed()
      composeTestRule.onNodeWithTag("BookmarkButton", useUnmergedTree = true).assertIsDisplayed()
    }
  }

  @Test
  fun assertClickingActionWorks() {
    val mockFunction: (Recipe) -> Unit = mockk(relaxed = true)
    val recipe = Recipe.default()
    composeTestRule.setContent { RecipeCard(recipe, mockFunction) }
    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<RecipeCardScreen>(composeTestRule) {
      composeTestRule
          .onNodeWithTag("RecipeImage", useUnmergedTree = true)
          .assertHasClickAction()
          .performClick()

      // wait a bit for the animation to finish
      Thread.sleep(1000)
    }

    // Verify that mockFunction was called with the correct argument
    verify { mockFunction(recipe) }
  }

  @Test
  fun testBookmarkButton() {
    val onBookmarkClick: (Recipe) -> Unit = mockk(relaxed = true)
    val onBookmarkDeselect: (Recipe) -> Unit = mockk(relaxed = true)
    val recipe = Recipe.default()
    composeTestRule.setContent {
      RecipeCard(recipe, onBookmarkClick = onBookmarkClick, onBookmarkRemove = onBookmarkDeselect)
    }
    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<RecipeCardScreen>(composeTestRule) {
      composeTestRule
          .onNodeWithTag("BookmarkButton", useUnmergedTree = true)
          .assertHasClickAction()
          .performClick()

      composeTestRule
          .onNodeWithTag("BookmarkButton", useUnmergedTree = true)
          .assertHasClickAction()
          .performClick()
    }

    // Verify that mockFunction was called with the correct argument
    verify { onBookmarkClick(recipe) }
    verify { onBookmarkDeselect(recipe) }
  }
}
