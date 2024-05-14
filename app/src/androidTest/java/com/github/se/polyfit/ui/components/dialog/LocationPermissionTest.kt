package com.github.se.polyfit.ui.components.dialog

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.base.Verify.verify
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlin.test.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationPermissionTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  val mockFunction: () -> Unit = mockk()
  val mockActivityResultLauncher = mockk<ActivityResultLauncher<String>>()

  @Before
  fun setup() {
    every { mockFunction() } just Runs
    composeTestRule.setContent {
      LocationPermissionDialog(onApprove = mockFunction, onDeny = mockFunction)
    }
  }

  @Test
  fun everythingIsDisplayed() {
    ComposeScreen.onComposeScreen<LocationPermissionScreen>(composeTestRule) {
      title {
        assertIsDisplayed()
        assertTextEquals("Location Permission Request")
      }

      message {
        assertIsDisplayed()
        assertTextEquals(
            "Your post will be visible to others around you. Allow Polyfit to access your location?")
      }

      approveButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals(" Allow ")
      }

      denyButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals(" Cancel ")
      }
    }
  }

 /* @Test
  fun approveButtonClicked() {
    ComposeScreen.onComposeScreen<LocationPermissionScreen>(composeTestRule) {
      approveButton { performClick() }
      verify { mockActivityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
    }
  }*/

  @Test
  fun denyButtonClicked() {
    ComposeScreen.onComposeScreen<LocationPermissionScreen>(composeTestRule) {
      denyButton { performClick() }
      verify { mockFunction() }
    }
  }
}
