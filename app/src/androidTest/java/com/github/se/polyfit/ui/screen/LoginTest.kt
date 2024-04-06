package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest : TestCase() {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @get:Rule val intentsTestRule = IntentsTestRule(MainActivity::class.java)

  @Test
  fun titleAndButtonAreCorrectlyDisplayed() {
    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      // Test the UI elements
      loginTitle {
        assertIsDisplayed()
        assertTextEquals("PolyFit")
      }
      loginButton {
        assertIsDisplayed()
        assertHasClickAction()
      }

      loginTerm {
        assertIsDisplayed()
        assertTextContains(
            "By clicking continue, you agree to our", substring = true, ignoreCase = false)
        assertTextContains("Terms of Service", substring = true, ignoreCase = false)
        assertTextContains("Privacy Policy", substring = true, ignoreCase = false)
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
      // TODO check we are on another page
      intended(toPackage("com.google.android.gms"))
    }
  }

  @Test
  fun googleSignInReturnsValidActivityResult() {
    Thread.sleep(2000) // wait for 2 seconds before the test starts

    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      loginButton {
        assertIsDisplayed()
        performClick()
      }

      // assert that an Intent resolving to Google Mobile Services has been sent (for sign-in)
      intended(toPackage("com.google.android.gms"))
    }
  }
}
