package com.github.se.polyfit.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.settings.SettingOption
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()
  val mockFunction: () -> Unit = mockk(relaxed = true)

  @Before
  fun setup() {
    val settings: List<SettingOption> =
        listOf(
            SettingOption("Account", Icons.Outlined.AccountCircle, mockFunction),
            SettingOption("Terms & Conditions", Icons.Outlined.Build, mockFunction),
            SettingOption("Privacy Policy", Icons.Outlined.Lock, mockFunction),
            SettingOption("About", Icons.Outlined.Info, mockFunction),
            SettingOption("Logout", Icons.AutoMirrored.Outlined.ExitToApp, mockFunction),
        )

    composeTestRule.setContent { SettingsScreen(settings) }
  }

  @Test
  fun everythingIsDisplayed() {
    ComposeScreen.onComposeScreen<SettingsScreen>(composeTestRule) {
      title {
        assertExists()
        assertIsDisplayed()
        assertTextEquals("Settings")
      }

      settingItem {
        assertExists()
        assertIsDisplayed()
        assertTextEquals("Account")
        assertContentDescriptionContains("Outlined.AccountCircle")
        assertContentDescriptionContains("ArrowRight")
        performClick()
      }

      verify { mockFunction() }

      divider {
        assertExists()
        assertIsDisplayed()
      }

      composeTestRule.onAllNodesWithTag("SettingItem").assertCountEquals(5)
    }
  }
}
