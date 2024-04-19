package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.navigation.globalNavigation
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import io.mockk.mockk
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

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

  @Before
  fun setup() {
    // Create a mock of MealViewModel
    val mealViewModel = mockk<MealViewModel>(relaxed = true)

    mockkStatic(Log::class)
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Overview) {
        globalNavigation(navController, mealViewModel)
      }
    }
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun topBarDisplayed() {
    composeTestRule.onNodeWithTag("MainTopBar").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("topBarOuterBox").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewTitle)
        .assertExists()
        .assertIsDisplayed()
        .assertTextEquals("PolyFit")
  }

  @Test
  fun bottomBarDisplayed() {
    composeTestRule.onNodeWithTag("MainBottomBar").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewHomeBtn)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewMapBtn)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewMapBtn)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithContentDescription(OverviewTags.overviewHomeIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithContentDescription(OverviewTags.overviewMapIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithContentDescription(OverviewTags.overviewSettingsIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()

    composeTestRule
        .onNodeWithTag(OverviewTags.overviewHomeLabel, useUnmergedTree = true)
        .assertExists()

    composeTestRule
        .onNodeWithTag(OverviewTags.overviewMapLabel, useUnmergedTree = true)
        .assertDoesNotExist()

    composeTestRule
        .onNodeWithTag(OverviewTags.overviewSettingsLabel, useUnmergedTree = true)
        .assertDoesNotExist()
  }

  @Test
  fun OverviewScreenContent_Displayed() {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
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

        composeTestRule.onNodeWithTag(OverviewTags.overviewGoal).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithTag(OverviewTags.overviewTrack).assertExists().assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(OverviewTags.overviewPictureBtn)
            .onChild()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithTag(OverviewTags.overviewManualBtn)
            .onChild()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithTag(OverviewTags.overviewDetailsBtn)
            .onChild()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule.onNodeWithContentDescription("photoIcon").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("penIcon").assertExists().assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("historyIcon")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(OverviewTags.overviewCalorie)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("CaloriePerMeal").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithTag("Number").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithTag("Goal").assertExists().assertIsDisplayed()
      }
    }
  }

  @Test
  fun PictureDialog_Displays() {
    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
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
