package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GradientBoxTest : TestCase() {
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
  fun defaultGradientBox() {
    composeTestRule.setContent { GradientBox(iconOnClick = {}) {} }
    ComposeScreen.onComposeScreen<GradientBox>(composeTestRule) {
      composeTestRule.onNodeWithTag("GradientBox").assertIsDisplayed()
      composeTestRule.onNodeWithTag("GradientBox").onChild().assertDoesNotExist()
    }
  }

  @Test
  fun customGradientBox() {
    composeTestRule.setContent {
      GradientBox(
          round = 10.0,
          borderSize = 5.0,
          gradientColors = listOf(Color(0xFFFFFFFF), Color(0xFF000000)),
          iconOnClick = {},
          icon = EmptyImageVector,
          iconColor = Color(0xFF000000),
          iconDescriptor = "descriptor") {
            Text(text = "Hello World")
          }
    }
    ComposeScreen.onComposeScreen<GradientBox>(composeTestRule) {
      composeTestRule.onNodeWithTag("GradientBox").assertIsDisplayed()
      composeTestRule.onNodeWithText("Hello World").assertIsDisplayed()
    }
  }
}
