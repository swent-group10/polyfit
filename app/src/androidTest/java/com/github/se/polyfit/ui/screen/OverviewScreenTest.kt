package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.NavItemTags
import com.github.se.polyfit.ui.navigation.Route
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverviewTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @Before
  fun setup() {
    mockkStatic(Log::class)
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Home) {
        globalNavigation(navController)
      }
    }
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun topBarDisplayed() {
    ComposeScreen.onComposeScreen<OverviewTopBar>(composeTestRule) {
      title {
        assertExists()
        assertIsDisplayed()
        assertTextEquals("Polyfit")
      }
    }
  }

  @Test
  fun bottomBarDisplayed() {
    composeTestRule.onNodeWithTag("MainBottomBar").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(NavItemTags.overviewItem)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(NavItemTags.mapItem)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(NavItemTags.settingsItem)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(NavItemTags.overviewIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(NavItemTags.mapIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(NavItemTags.settingsIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
  }

  @Test
  fun bottomBarItemName_DisplayOnSelect() {
    // Initial state with overview Item selected
    composeTestRule
        .onNodeWithText(NavItemTags.overviewName, useUnmergedTree = true)
        .assertIsDisplayed()
    composeTestRule.onNodeWithText(NavItemTags.mapName, useUnmergedTree = true).assertDoesNotExist()
    composeTestRule
        .onNodeWithText(NavItemTags.settingsName, useUnmergedTree = true)
        .assertDoesNotExist()

    // Selecting Map Item
    composeTestRule
        .onNodeWithTag(NavItemTags.mapItem, useUnmergedTree = true)
        .assertIsDisplayed()
        .assertHasClickAction()
        .performClick()
    composeTestRule.onNodeWithTag("OverviewScreen").assertDoesNotExist()
    composeTestRule.onNodeWithTag(NavItemTags.overviewName).assertDoesNotExist()
    composeTestRule.onNodeWithTag(NavItemTags.settingsName).assertDoesNotExist()
    composeTestRule.onNodeWithText(NavItemTags.mapName).assertIsDisplayed()

    // Selecting Settings Item
    composeTestRule
        .onNodeWithTag(NavItemTags.settingsItem, useUnmergedTree = true)
        .assertIsDisplayed()
        .assertHasClickAction()
        .performClick()
    composeTestRule.onNodeWithTag(NavItemTags.overviewName).assertDoesNotExist()
    composeTestRule.onNodeWithTag(NavItemTags.mapName).assertDoesNotExist()
    composeTestRule.onNodeWithText(NavItemTags.settingsName).assertIsDisplayed()
  }

  @Test
  fun OverviewScreenContent_Displayed() {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
      thirdCard {
        assertExists()
        assertIsDisplayed()
      }

      secondCard {
        assertExists()
        assertIsDisplayed()
      }

      genericImage {
        assertExists()
        assertIsDisplayed()
      }

      calorieCard {
        assertExists()
        assertIsDisplayed()
      }

      welcomeMessage {
        assertExists()
        assertIsDisplayed()
      }
      ComposeScreen.onComposeScreen<CalorieCard>(composeTestRule) {
        assertExists()
        assertIsDisplayed()

        title {
          assertExists()
          assertIsDisplayed()
        }
        mealTracking {
          assertExists()
          assertIsDisplayed()
          assertTextEquals("Track your meals")
        }
        photoButton {
          assertExists()
          assertIsDisplayed()
          assertHasClickAction()
        }

        editButton {
          assertExists()
          assertIsDisplayed()
          assertHasClickAction()
        }

        historyButton {
          assertExists()
          assertIsDisplayed()
          assertHasClickAction()
        }

        composeTestRule.onNodeWithContentDescription("photoIcon").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("penIcon").assertExists().assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("historyIcon")
            .assertExists()
            .assertIsDisplayed()

        calorieAmount {
          assertExists()
          assertIsDisplayed()
        }

        caloriePerMeal {
          assertExists()
          assertIsDisplayed()
        }

        calNumber {
          assertExists()
          assertIsDisplayed()
        }

        calSlash {
          assertExists()
          assertIsDisplayed()
          assertTextEquals("/")
        }

        calGoal {
          assertExists()
          assertIsDisplayed()
        }

        breakfast {
          assertExists()
          assertIsDisplayed()
          assertTextEquals("Breakfast")
        }

        lunch {
          assertExists()
          assertIsDisplayed()
          assertTextEquals("Lunch")
        }

        dinner {
          assertExists()
          assertIsDisplayed()
          assertTextEquals("Dinner")
        }

        snacks {
          assertExists()
          assertIsDisplayed()
          assertTextEquals("Snacks")
        }
      }
    }
  }

  @Test
  fun PictureDialog_Displays() {
    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag("PhotoButton").performClick()
      assertExists()
      assertIsDisplayed()
      firstButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
      }
      secondButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }
}
