package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.MainActivity
import com.github.se.polyfit.ui.navigation.MockNavigation
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest :  TestCase() {


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

    /*@Test
    fun googleSignInReturnsValidActivityResult() {
        ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
            //composeTestRule.setContent { LoginScreen(nav) }
            loginButton {
                assertIsDisplayed()
                performClick()
            }
            // assert that an Intent resolving to Google Mobile Services has been sent (for sign-in)
            intended(toPackage("com.google.android.gms"))
        }
    }*/
}