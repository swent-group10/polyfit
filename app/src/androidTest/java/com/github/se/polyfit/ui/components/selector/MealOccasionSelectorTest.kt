package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.meal.MealOccasion
import io.github.kakaocup.compose.node.element.ComposeScreen
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealOccasionSelectorTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  fun setContent(
      startOccasion: MealOccasion = MealOccasion.OTHER,
      onConfirm: (MealOccasion) -> Unit = {}
  ) {
    composeTestRule.setContent {
      MealOccasionSelector(startOccasion = startOccasion, onConfirm = onConfirm)
    }
  }

  @Test
  fun everythingDisplayed() {
    setContent()
    ComposeScreen.onComposeScreen<MealOccasionSelectorScreen>(composeTestRule) {
      mealOccasionSelectorTitle {
        assertIsDisplayed()
        assertTextEquals("Meal Occasion")
      }

      breakfastButton {
        assertIsDisplayed()
        assertIsEnabled()
        assertIsNotSelected()
        assertHasClickAction()
      }

      lunchButton {
        assertIsDisplayed()
        assertIsEnabled()
        assertIsNotSelected()
        assertHasClickAction()
      }

      dinnerButton {
        assertIsDisplayed()
        assertIsEnabled()
        assertIsNotSelected()
        assertHasClickAction()
      }

      snackButton {
        assertIsDisplayed()
        assertIsEnabled()
        assertIsNotSelected()
        assertHasClickAction()
      }

      breakfastText {
        assertIsDisplayed()
        assertTextEquals("Breakfast")
      }

      lunchText {
        assertIsDisplayed()
        assertTextEquals("Lunch")
      }

      dinnerText {
        assertIsDisplayed()
        assertTextEquals("Dinner")
      }

      snackText {
        assertIsDisplayed()
        assertTextEquals("Snack")
      }
    }
  }

  @Test
  fun selectingOccasionWorks() {
    setContent()

    ComposeScreen.onComposeScreen<MealOccasionSelectorScreen>(composeTestRule) {
      breakfastButton { assertIsNotSelected() }

      lunchButton { assertIsNotSelected() }

      dinnerButton { assertIsNotSelected() }

      snackButton { assertIsNotSelected() }

      breakfastButton {
        performClick()
        assertIsSelected()
      }

      lunchButton {
        performClick()
        assertIsSelected()
      }

      dinnerButton {
        performClick()
        assertIsSelected()
      }

      snackButton {
        performClick()
        assertIsSelected()
      }
    }
  }

  @Test
  fun initialOccasionSetsProperly() {
    setContent(startOccasion = MealOccasion.LUNCH)

    ComposeScreen.onComposeScreen<MealOccasionSelectorScreen>(composeTestRule) {
      breakfastButton { assertIsNotSelected() }

      lunchButton { assertIsSelected() }

      dinnerButton { assertIsNotSelected() }

      snackButton { assertIsNotSelected() }
    }
  }
}
