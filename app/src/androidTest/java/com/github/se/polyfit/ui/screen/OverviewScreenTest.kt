package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.ui.viewModel.DataToPoints
import com.github.se.polyfit.ui.viewModel.DateList
import com.github.se.polyfit.ui.viewModel.DisplayScreen
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class OverviewTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  fun setup() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) {
          GenericScreen(
              navController = navController,
              content = { paddingValues -> OverviewScreen(paddingValues, navController, mockk()) })
        }

        composable(Route.AddMeal) {
          AddMealFlow(goBack = {}, navigateToHome = {}, mockk(relaxed = true))
        }
      }
    }
  }

  @Before
  fun SettingUp() {
    mockkStatic(Log::class)
    System.setProperty("isTestEnvironment", "true")
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun graphCardDataTest() {
    composeTestRule.setContent {
      // Define a mock ViewModel or required data setup here
      val pointList = DataToPoints() // Assuming this can be accessed here
      val dateList = DateList() // Assuming this can be accessed here
      val screen = DisplayScreen.GRAPH_SCREEN
      val steps = 10
      // Invoke the composable to render the chart with test data
      val xAxisData =
          AxisData.Builder()
              .axisLabelAngle(15f)
              .axisLabelDescription { "Date" }
              .axisStepSize(120.dp)
              .shouldDrawAxisLineTillEnd(false)
              .backgroundColor(MaterialTheme.colorScheme.background)
              .steps(pointList.size - 1)
              .labelData { i -> "" }
              .labelAndAxisLinePadding(15.dp)
              .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
              .build()

      val yAxisData =
          AxisData.Builder()
              .steps(steps)
              .backgroundColor(MaterialTheme.colorScheme.background)
              .labelAndAxisLinePadding(30.dp)
              .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
              .labelData { i -> "" }
              .build()
              .toString()

      // Now compare the generated xAxisData with expected one
      val data =
          lineChartData(pointList = pointList, dateList = dateList, DisplayScreen.GRAPH_SCREEN)
      assertEquals(xAxisData.toString(), data.xAxisData.toString())
      assertEquals(yAxisData, data.yAxisData.toString())
      val line = data.linePlotData.lines[0]

      assertEquals(
          LineStyle(
                  color = MaterialTheme.colorScheme.onBackground,
                  lineType = LineType.SmoothCurve(isDotted = false))
              .toString(),
          line.lineStyle.toString())

      assertEquals(
          IntersectionPoint(color = MaterialTheme.colorScheme.primary).toString(),
          line.intersectionPoint.toString())
      assertEquals(null, line.selectionHighlightPoint)
      assertEquals(
          ShadowUnderLine(
                  alpha = 0.5f,
                  brush =
                      Brush.verticalGradient(
                          colors =
                              listOf(
                                  MaterialTheme.colorScheme.inversePrimary,
                                  MaterialTheme.colorScheme.background)))
              .toString(),
          line.shadowUnderLine.toString())
      assertEquals(null, line.selectionHighlightPopUp)
      assertEquals(MaterialTheme.colorScheme.surface, data.backgroundColor)
      assertEquals(
          GridLines(color = MaterialTheme.colorScheme.outlineVariant).toString(),
          data.gridLines.toString())
    }
  }

  @Test
  fun topBarDisplayed() {
    setup()
    composeTestRule.onNodeWithTag("MainTopBar").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("topBarOuterBox").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewTitle)
        .assertExists()
        .assertIsDisplayed()
        .assertTextEquals("Polyfit")
  }

  @Test
  fun bottomBarDisplayed() {
    setup()
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
    setup()
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
    setup()
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

  @Test
  fun editButtonClicked() {
    setup()
    ComposeScreen.onComposeScreen<CalorieCard>(composeTestRule) {
      composeTestRule
          .onNodeWithTag(OverviewTags.overviewManualBtn)
          .onChild()
          .assertExists()
          .assertIsDisplayed()
          .assertHasClickAction()
          .performClick()
    }
    ComposeScreen.onComposeScreen<IngredientsTopBar>(composeTestRule) {
      ingredientTitle {
        assertExists()
        assertIsDisplayed()
      }
    }
  }

  @Test
  fun graphCardTest() {
    setup()
    composeTestRule
        .onNodeWithTag("OverviewScreenLazyColumn")
        .performScrollToNode(hasTestTag("Graph Card"))
    composeTestRule
        .onNodeWithTag("Graph Card")
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag("Graph Card Column", useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("Graph Card Title", useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
        .assertTextEquals("Calories Graph")
    composeTestRule
        .onNodeWithTag("Overview Line Chart", useUnmergedTree = true)
        .assertDoesNotExist()
  }
}
