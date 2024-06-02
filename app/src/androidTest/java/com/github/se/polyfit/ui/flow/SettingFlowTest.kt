package com.github.se.polyfit.ui.flow

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.screen.SettingScreen
import com.github.se.polyfit.ui.screen.settings.AccountSettingsScreen
import com.github.se.polyfit.viewmodel.settings.AccountSettingsViewModel
import com.github.se.polyfit.viewmodel.settings.SettingFlowViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SettingFlowTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  private val mockAccountSettingsViewModel: AccountSettingsViewModel = mockk(relaxed = true)
  private val mockSettingFlowViewModel: SettingFlowViewModel = mockk(relaxed = true)
  private val mockNav = mockk<Navigation>(relaxed = true)

  @Before
  fun setup() {
    composeTestRule.setContent {
      every { mockAccountSettingsViewModel.user.value } returns User(id = "test", email = "test")

      SettingFlow(
          toLogin = mockNav::restartToLogin,
          settingFlowViewModel = mockSettingFlowViewModel,
          accountSettingsViewModel = mockAccountSettingsViewModel)
    }
  }

  @Test
  fun settingScreenIsShown() {
    ComposeScreen.onComposeScreen<SettingScreen>(composeTestRule) { title { assertIsDisplayed() } }
  }

  @Test
  fun accountSettingsCanShow() {
    ComposeScreen.onComposeScreen<SettingScreen>(composeTestRule) { settingItem { performClick() } }

    ComposeScreen.onComposeScreen<AccountSettingsScreen>(composeTestRule) {
      displayName { assertIsDisplayed() }
    }
  }

  @Test
  fun logout() {
    ComposeScreen.onComposeScreen<SettingScreen>(composeTestRule) {
      composeTestRule.onNodeWithText("Sign Out").assertExists().assertIsDisplayed().performClick()
      composeTestRule.onNodeWithText("Are you sure you want to sign out?").assertIsDisplayed()
      composeTestRule.onNodeWithTag("DenyButton").assertIsDisplayed().performClick()
      composeTestRule.onNodeWithText("Sign Out").assertExists().assertIsDisplayed().performClick()
      composeTestRule.onNodeWithTag("SignoutButton").assertIsDisplayed().performClick()
    }

    verify { mockSettingFlowViewModel.signOut() }
  }
}
