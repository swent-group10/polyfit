package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    composeTestRule.setContent {
      LoginScreen(mockNav::navigateToHome)
    }
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

    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      loginColumn.assertExists()

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

  @Test
  fun loginButtonNavigatesToNewScreen() {
    // Launch the LoginScreen
    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      // Test the UI elements
      loginButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }

      runBlocking {
        delay(5000) // waits for 5 seconds
      }
      // TODO check we are on another page
      intended(toPackage("com.google.android.gms"))
    }
  }
}
