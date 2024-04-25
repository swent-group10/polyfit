package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Route
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GraphScreenTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule val mockkRule = MockKRule(this)

  @Before
  fun setup() {
    System.setProperty("isTestEnvironment", "true")
    mockkStatic(Log::class)
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen() }
      }
    }
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun graphSpotReserved() {
    composeTestRule.onNodeWithTag("GraphScreenColumn").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LineChart").assertDoesNotExist()
    composeTestRule.onNodeWithTag("LineChartSpacer").assertExists().assertIsDisplayed()
  }

  @Test
  fun searchBarShownAndWriteable() {
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("1000")
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").assertTextEquals("1000")
  }

  @Test
  fun searchBarFiltersCorrectly() {
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("1000")
    // Assert only the matching GraphData elements are displayed
    composeTestRule
        .onAllNodesWithText("1000.0 kCal", ignoreCase = true)
        .assertCountEquals(2) // Assuming two entries match this
  }

  @Test
  fun dropdownMenuInteraction() {
    composeTestRule.onNodeWithTag("GraphDataSortingMenu").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("DropdownMenuTextField")
        .assertExists()
        .assertIsDisplayed()
        .assertTextEquals("KCAL")
    composeTestRule.onNodeWithTag("DropdownTab").assertIsNotDisplayed()
    composeTestRule.onNodeWithTag("GraphDataSortingMenu").performClick()
    composeTestRule.onNodeWithTag("DropdownTab").assertIsDisplayed()
    composeTestRule.onNodeWithText("WEIGHT").performClick()
    composeTestRule.onNodeWithTag("DropdownTab").assertIsNotDisplayed()
    composeTestRule.onNodeWithTag("DropdownMenuTextField").assertTextEquals("WEIGHT")
  }
}
