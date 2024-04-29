package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DateSelectorTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  fun setContent(onConfirm: (LocalDate) -> Unit = {}, title: String = "Add to Date") {
    composeTestRule.setContent { DateSelector(onConfirm = onConfirm, title = title) }
  }

  @Test
  fun everythingDisplayed() {
    setContent()
    val date = LocalDate.now(ZoneId.systemDefault())
    val day = date.format(DateTimeFormatter.ofPattern("dd"))
    val month = date.format(DateTimeFormatter.ofPattern("MM"))
    val year = date.format(DateTimeFormatter.ofPattern("yyyy"))

    ComposeScreen.onComposeScreen<DateSelectorScreen>(composeTestRule) {
      dateSelectorTitle {
        assertIsDisplayed()
        assertTextEquals("Add to Date")
      }

      changeDateButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextContains(day)
        assertTextContains(month)
        assertTextContains(year)
        assertTextContains("/")
        assertIsEnabled()
      }
    }
  }

  @Test
  // No need to test the dialog, since it's from M3
  fun changeDateButtonClicked() {
    setContent()
    ComposeScreen.onComposeScreen<DateSelectorScreen>(composeTestRule) {
      changeDateButton { performClick() }

      composeTestRule.onNodeWithTag("DatePickerDialog").assertExists()
      composeTestRule.onNodeWithTag("DatePicker").assertExists()
    }
  }

  @Test
  fun noTitleIfEmpty() {
    setContent(title = "")
    ComposeScreen.onComposeScreen<DateSelectorScreen>(composeTestRule) {
      dateSelectorTitle { assertDoesNotExist() }
    }
  }
}
