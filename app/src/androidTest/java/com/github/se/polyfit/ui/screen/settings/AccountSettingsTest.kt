package com.github.se.polyfit.ui.screen.settings

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.viewmodel.settings.AccountSettingsViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountSettingsTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  private val mockNav: Navigation = mockk(relaxed = true)
  private val mockViewModel: AccountSettingsViewModel = mockk(relaxed = true)

  private val partialUser = User(id = "1", displayName = "John Doe", email = "")

  private val baseUser =
      User(
          id = "1",
          displayName = "John Doe",
          givenName = "John",
          familyName = "Doe",
          email = "john@doe.com",
          calorieGoal = 0)

  val filledUser =
      User(
          id = "1",
          displayName = "John Doe",
          givenName = "John",
          familyName = "Doe",
          email = "john@doe.com",
          heightCm = 180,
          weightKg = 80.0,
          calorieGoal = 2000,
          dob = LocalDate.of(1990, 1, 2))

  fun setup(user: User = baseUser) {
    every { mockViewModel.user.value } returns user

    composeTestRule.setContent { AccountSettingsScreen(mockNav::goBack, mockViewModel) }
  }

  @Test
  fun fieldsPopulateIfExist() {
    setup()

    ComposeScreen.onComposeScreen<AccountSettingsScreen>(composeTestRule) {
      displayName { assertTextContains("John Doe") }
      firstName { assertTextContains("John") }
      lastName { assertTextContains("Doe") }
      height { assertTextContains("Height (cm)") }
      weight { assertTextContains("Weight (kg)") }
      calorieGoal { assertTextContains("Daily Calorie Goal (kCal)") }
    }

    ComposeScreen.onComposeScreen<AccountSettingsBottomBar>(composeTestRule) {
      cancel {
        assertIsDisplayed()
        assertHasClickAction()
      }
      save {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun canNotSaveUnlessNameComplete() {
    setup(partialUser)

    ComposeScreen.onComposeScreen<AccountSettingsBottomBar>(composeTestRule) {
      cancel {
        assertIsDisplayed()
        assertHasClickAction()
      }
      save {
        assertIsDisplayed()
        assertIsNotEnabled()
      }
    }
  }

  @Test
  fun allFieldsContainProperData() {
    setup(filledUser)

    ComposeScreen.onComposeScreen<AccountSettingsScreen>(composeTestRule) {
      displayName { assertTextContains("John Doe") }
      firstName { assertTextContains("John") }
      lastName { assertTextContains("Doe") }
      height { assertTextContains("180") }
      weight { assertTextContains("80.0") }
      calorieGoal {
        assertTextContains("2000")

        composeTestRule.onNodeWithTag("ChangeDateButton").assertTextContains("1990")
        composeTestRule.onNodeWithTag("ChangeDateButton").assertTextContains("01")
        composeTestRule.onNodeWithTag("ChangeDateButton").assertTextContains("01")
      }
    }
  }

  @Test
  fun testLargeValues() {
    setup(filledUser)
    ComposeScreen.onComposeScreen<AccountSettingsScreen>(composeTestRule) {
      height { assertTextContains("180") }
      weight { assertTextContains("80.0") }
      calorieGoal { assertTextContains("2000") }
    }

    ComposeScreen.onComposeScreen<AccountSettingsScreen>(composeTestRule) {
      composeTestRule.onNodeWithTag("Height")
      composeTestRule.onNodeWithTag("Height").performTextInput("100000")
    }
  }

  @Test
  fun savingMakesCallToViewModel() {
    setup()

    ComposeScreen.onComposeScreen<AccountSettingsBottomBar>(composeTestRule) {
      save { performClick() }
    }

    verify { mockViewModel.updateUser() }
  }
}
