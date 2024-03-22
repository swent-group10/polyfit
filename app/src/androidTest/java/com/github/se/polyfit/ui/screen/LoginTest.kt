package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest : TestCase() {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun titleAndButtonAreCorrectlyDisplayed() {
    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      // Test the UI elements
      loginTitle {
        assertIsDisplayed()
        assertTextEquals("Welcome")
      }
      loginButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Ignore("This test is not working, It need a mock authentication service to work.")
  @Test
  fun googleSignInReturnsValidActivityResult() {
    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      // composeTestRule.setContent { LoginScreen(nav) }
      loginButton {
        assertIsDisplayed()
        performClick()
      }
      // assert that an Intent resolving to Google Mobile Services has been sent (for sign-in)
      // intended(toPackage("com.google.android.gms"))
    }

    ComposeScreen.onComposeScreen<HomeScreen>(composeTestRule) {
      // Test the UI elements
      homeScreen { assertIsDisplayed() }
    }
  }
}
