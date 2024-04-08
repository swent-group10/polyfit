package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
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
class PrimaryPurpleButtonTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  @Before
  fun setup() {
    mockkStatic(Log::class)
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun defaultButton() {
    composeTestRule.setContent { PrimaryPurpleButton(onClick = {}) }
    ComposeScreen.onComposeScreen<GradientBox>(composeTestRule) {
      composeTestRule.onNodeWithTag("PrimaryPurpleButton").assertIsDisplayed()
      composeTestRule.onNodeWithTag("PrimaryPurpleButton").onChild().assertDoesNotExist()
    }
  }

  @Test
  fun customButton() {
    composeTestRule.setContent {
      PrimaryPurpleButton(
          onClick = { Log.v("Button", "Clicked") },
          text = "Hello World",
      )
    }
    ComposeScreen.onComposeScreen<GradientBox>(composeTestRule) {
      composeTestRule.onNodeWithTag("PrimaryPurpleButton").assertIsDisplayed()
      composeTestRule.onNodeWithTag("PrimaryPurpleButton").assertHasClickAction()
      composeTestRule.onNodeWithTag("PrimaryPurpleButton").performClick()
      verify { Log.v("Button", "Clicked") }
      composeTestRule.onNodeWithText("Hello World").assertIsDisplayed()
    }
  }
}
