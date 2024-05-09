package com.github.se.polyfit.ui.components.button

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LikeButtonTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @Test
  fun testEverythingIsDisplayed() {
    composeTestRule.setContent { LikeButton(likeCount = 14) }
    composeTestRule.waitForIdle()

    ComposeScreen.onComposeScreen<LikeButtonScreen>(composeTestRule) {
      assertExists()
      assertHasClickAction()
      assertIsNotEnabled()
      composeTestRule.onNodeWithText("14").assertExists()
      composeTestRule.onNodeWithTag("LikeIcon", useUnmergedTree = true).assertExists()
    }
  }
}
