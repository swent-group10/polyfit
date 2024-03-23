package com.github.se.polyfit

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.ui.screen.HomeScreen
import com.github.se.polyfit.ui.screen.LoginScreen
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
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
  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.github.se.polyfit", appContext.packageName)
  }
}
