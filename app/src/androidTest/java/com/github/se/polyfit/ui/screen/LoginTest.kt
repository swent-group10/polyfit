package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)
  @RelaxedMockK lateinit var mockNav: Navigation

  @Before
  fun setup() {
    Intents.init()
    mockkStatic(Log::class)
    composeTestRule.setContent { LoginScreen(mockNav::navigateToHome) }
    composeTestRule.waitForIdle()
  }

  @After
  fun tearDown() {
    Intents.release()
    unmockkStatic(Log::class)
  }

  @Test
  fun navigatesToHomeWhenAuthenticated() {

    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      // Assuming there's a button that triggers authentication in LoginScreen
      loginButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    verify { mockNav.navigateToHome() }
    confirmVerified(mockNav)
  }

  @Test
  fun loginScreenDisplaysCorrectly() {

    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      loginColumn {
        assertExists()
        assertIsDisplayed()
      }

      loginTitle.assertExists()
      loginTitle {
        assertExists()
        assertIsDisplayed()
        // We ignore case because title may change to PolyFit.
        assertTextContains("Polyfit", substring = true, ignoreCase = true)
      }

      loginButton {
        assertIsDisplayed()
        assertHasClickAction()
      }

      loginTerm {
        assertIsDisplayed()
        assertTextContains("By clicking continue, you agree to our", substring = true)
        assertTextContains("Terms of Service", substring = true)
        assertTextContains("and", substring = true)
        assertTextContains("Privacy Policy", substring = true)
      }
    }
  }
}
