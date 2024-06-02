package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.meal.MealTagColor
import com.github.se.polyfit.ui.components.list.MAX_TAGS
import io.github.kakaocup.compose.node.element.ComposeScreen
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealTagSelectorTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  fun setContent(
      mealTags: MutableList<MealTag> = mutableListOf(),
      addMealTag: (MealTag) -> Unit = { mealTags.add(it) },
      removeMealTag: (MealTag) -> Unit = { mealTags.remove(it) }
  ) {
    composeTestRule.setContent { MealTagSelector(mealTags, addMealTag, removeMealTag) }
  }

  @Test
  fun everythingDisplayed() {
    setContent()
    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      mealTagSelectorTitle {
        assertIsDisplayed()
        assertTextEquals("Special Tags")
      }

      maxTagsSubTitle {
        assertIsDisplayed()
        assertTextEquals("(max $MAX_TAGS)")
      }

      mealTagItem { assertDoesNotExist() }

      addTag {
        assertIsDisplayed()
        assertHasClickAction()
      }

      addMealTagDialog.assertDoesNotExist()
    }
  }

  @Test
  fun tagIsDisplayed() {
    val mealTags = mutableListOf(MealTag("Dairy Free", MealTagColor.BLUE))
    setContent(mealTags)
    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      mealTagItem {
        assertIsDisplayed()
        assertTextEquals("Dairy Free")
      }
    }
  }

  @Test
  fun addTagDialogIsDisplayed() {
    setContent()
    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      addTag {
        assertIsDisplayed()
        performClick()
      }

      addMealTagDialog { assertIsDisplayed() }

      addMealTagTitle {
        assertIsDisplayed()
        assertTextEquals("Add a Tag")
      }

      removeMealTagButton { assertDoesNotExist() }

      labelSubtitle {
        assertIsDisplayed()
        assertTextEquals("Label")
      }

      labelInput { assertIsDisplayed() }

      colorSubtitle {
        assertIsDisplayed()
        assertTextEquals("Color")
      }

      saveButton {
        assertIsDisplayed()
        assertIsNotEnabled()
      }

      composeTestRule
          .onAllNodesWithTag("ColorButton")
          .assertCountEquals(MealTagColor.entries.size - 1)
      composeTestRule.onNodeWithTag("Selected").assertDoesNotExist()
    }
  }

  @Test
  fun editTagDialogIsDisplayed() {
    val mealTags = mutableListOf(MealTag("Dairy Free", MealTagColor.BLUE))
    setContent(mealTags)
    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      mealTagItem {
        assertIsDisplayed()
        performClick()
      }

      composeTestRule.waitForIdle()

      addMealTagDialog { assertIsDisplayed() }

      addMealTagTitle {
        assertIsDisplayed()
        assertTextEquals("Edit Tag")
      }

      removeMealTagButton { assertIsDisplayed() }

      labelSubtitle {
        assertIsDisplayed()
        assertTextEquals("Label")
      }

      labelInput {
        assertIsDisplayed()
        assertTextEquals("Dairy Free")
      }

      colorSubtitle {
        assertIsDisplayed()
        assertTextEquals("Color")
      }

      saveButton {
        assertIsDisplayed()
        assertIsEnabled()
      }

      composeTestRule
          .onAllNodesWithTag("ColorButton")
          .assertCountEquals(MealTagColor.entries.size - 1)
      composeTestRule.onNodeWithTag("Selected", useUnmergedTree = true).assertExists()
    }
  }

  @Test
  fun addTag() {
    setContent()
    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      addTag { performClick() }

      labelInput { performTextInput("Dairy Free") }

      colorButton { performClick() }

      saveButton { performClick() }

      mealTagItem {
        assertIsDisplayed()
        assertTextEquals("Dairy Free")
      }
    }
  }

  @Test
  fun addTagLimitsTo50() {
    setContent()
    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      addTag { performClick() }

      labelInput {
        performTextInput("A".repeat(50))
        performTextInput("A")
        assertTextEquals("A".repeat(50))
      }

      colorButton { performClick() }

      saveButton { performClick() }

      mealTagItem {
        assertIsDisplayed()
        assertTextEquals("A".repeat(50))
      }
    }
  }

  @Test
  fun editTag() {
    val mealTags = mutableListOf(MealTag("Dairy Free", MealTagColor.BLUE))
    setContent(mealTags)

    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      mealTagItem { performClick() }

      labelInput {
        performTextClearance()
        performTextInput("Gluten Free")
      }

      colorButton { performClick() }

      saveButton { performClick() }

      mealTagItem {
        assertIsDisplayed()
        assertTextEquals("Gluten Free")
      }
    }
  }

  @Test
  fun removeTag() {
    val mealTags = mutableListOf(MealTag("Dairy Free", MealTagColor.BLUE))
    setContent(mealTags)

    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      mealTagItem { performClick() }

      removeMealTagButton { performClick() }

      mealTagItem { assertDoesNotExist() }
    }
  }

  @Test
  fun removeAddTagAfterMaxTags() {
    val mealTags =
        mutableListOf(
            MealTag("Dairy Free", MealTagColor.BLUE),
            MealTag("Gluten Free", MealTagColor.GREEN),
            MealTag("Vegan", MealTagColor.PURPLE),
            MealTag("Vegetarian", MealTagColor.ORANGE),
            MealTag("Keto", MealTagColor.RED))
    setContent(mealTags)

    ComposeScreen.onComposeScreen<MealTagSelectorScreen>(composeTestRule) {
      addTag { assertDoesNotExist() }
    }
  }
}
