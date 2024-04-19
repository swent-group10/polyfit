package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.MainActivity
import com.github.se.polyfit.ui.components.AddIngredientDialog
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import okhttp3.internal.wait
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest : TestCase() {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setup() {
    Intents.init()
    composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun titleAndButtonAreCorrectlyDisplayed() {
    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      // Test the UI elements
      loginTitle {
        assertExists()
        assertIsDisplayed()
        assertTextContains("PolyFit", substring = true, ignoreCase = false)
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
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
      loginButton{
        assertDoesNotExist()
      }
    }
  }
}
