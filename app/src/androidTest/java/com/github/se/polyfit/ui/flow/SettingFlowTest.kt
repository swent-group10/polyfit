package com.github.se.polyfit.ui.flow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.settings.SettingOption
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.SettingScreen
import com.github.se.polyfit.ui.screen.SettingsScreen
import com.github.se.polyfit.ui.screen.settings.AccountSettingsScreen
import com.github.se.polyfit.viewmodel.settings.AccountSettingsViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
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

  @Before
  fun setup() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      val navigation = Navigation(navController)
      val options =
          listOf(
              SettingOption("Account", Icons.Default.Person, navigation::navigateToAccountSettings),
          )
      every { mockAccountSettingsViewModel.user.value } returns User(id = "test", email = "test")

      NavHost(navController = navController, startDestination = Route.SettingsHome) {
        composable(Route.SettingsHome) { SettingsScreen(settings = options) }

        composable(Route.AccountSettings) {
          AccountSettingsScreen(navigation::navigateToSettingsHome, mockAccountSettingsViewModel)
        }
      }
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
}
