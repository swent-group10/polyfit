package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.viewModel.DataToPoints
import com.github.se.polyfit.ui.viewModel.DateList
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
    /*

    */
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun graphParameters() {
    composeTestRule.setContent {
      // Define a mock ViewModel or required data setup here
      val pointList = DataToPoints() // Assuming this can be accessed here
      val dateList = DateList() // Assuming this can be accessed here
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
              .labelData { i -> dateList[i].toString() }
              .labelAndAxisLinePadding(15.dp)
              .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
              .build()

      val yAxisData =
          AxisData.Builder()
              .steps(steps)
              .backgroundColor(MaterialTheme.colorScheme.background)
              .labelAndAxisLinePadding(30.dp)
              .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
              .labelData { i ->
                val yMin = pointList.minOf { it.y }
                val yMax = pointList.maxOf { it.y }
                val yScale = (yMax - yMin) / steps
                ((i * yScale) + yMin).formatToSinglePrecision()
              }
              .build()
              .toString()

      // Now compare the generated xAxisData with expected one
      val data = lineChartData(pointList = pointList, dateList = dateList)
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
      assertEquals(
          SelectionHighlightPoint(color = MaterialTheme.colorScheme.secondary).toString(),
          line.selectionHighlightPoint.toString())
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
      assertEquals(SelectionHighlightPopUp().toString(), line.selectionHighlightPopUp.toString())
      assertEquals(MaterialTheme.colorScheme.surface, data.backgroundColor)
      assertEquals(
          GridLines(color = MaterialTheme.colorScheme.outlineVariant).toString(),
          data.gridLines.toString())
    }
  }

  @Test
  fun lazyListAndItemsDisplayed() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    composeTestRule.onNodeWithTag("ElementsList").assertExists().assertIsDisplayed()
    val nodes = composeTestRule.onAllNodesWithTag("ElementsRow")
    assertFalse(nodes.fetchSemanticsNodes().isEmpty(), "No nodes with tag 'ElemetsRow' found")
    val size = nodes.fetchSemanticsNodes().size
    for (i in 0 until size) {
      nodes[i].assertExists().assertIsDisplayed()
      if (i == 3) {
        composeTestRule.onNodeWithTag("ElementsList").performScrollToIndex(size - 1)
      }
    }
  }

  @Test
  fun itemCaloriesDisplayed() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    val nodes = composeTestRule.onAllNodesWithTag("kcal")
    assertFalse(nodes.fetchSemanticsNodes().isEmpty(), "No nodes with tag 'kcal' found")
    val size = nodes.fetchSemanticsNodes().size
    for (i in 0 until size) {
      nodes[i].assertExists().assertIsDisplayed()
      if (i == 3) {
        composeTestRule.onNodeWithTag("ElementsList").performScrollToIndex(size - 1)
      }
    }
  }

  @Test
  fun itemWeightDisplayed() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    val nodes = composeTestRule.onAllNodesWithTag("weight")
    assertFalse(nodes.fetchSemanticsNodes().isEmpty(), "No nodes with tag 'weight' found")
    val size = nodes.fetchSemanticsNodes().size
    for (i in 0 until size) {
      nodes[i].assertExists().assertIsDisplayed()
      if (i == 3) {
        composeTestRule.onNodeWithTag("ElementsList").performScrollToIndex(size - 1)
      }
    }
  }

  @Test
  fun itemDateDisplayed() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    val nodes = composeTestRule.onAllNodesWithTag("Date")
    assertFalse(nodes.fetchSemanticsNodes().isEmpty(), "No nodes with tag 'Date' found")
    val size = nodes.fetchSemanticsNodes().size
    for (i in 0 until size) {
      nodes[i].assertExists().assertIsDisplayed()
      if (i == 3) {
        composeTestRule.onNodeWithTag("ElementsList").performScrollToIndex(size - 1)
      }
    }
  }

  @Test
  fun topBarDisplayed() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    composeTestRule.onNodeWithTag("Calories Per Day Title").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("BackButton")
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
  }

  @Test
  fun graphSpotReserved() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    composeTestRule.onNodeWithTag("GraphScreenColumn").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LineChart").assertDoesNotExist()
    composeTestRule.onNodeWithTag("LineChartSpacer").assertExists().assertIsDisplayed()
  }

  @Test
  fun searchBarShownAndWriteable() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    composeTestRule.onNodeWithTag("GraphScreenSearchBar").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("1000")
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").assertTextEquals("1000")
  }

  @Test
  fun searchBarFiltersCorrectly() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("1000")
    // Assert only the matching GraphData elements are displayed
    composeTestRule
        .onAllNodesWithText("1000.0 kCal", ignoreCase = true)
        .assertCountEquals(2) // Assuming two entries match this

    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("5000")
    composeTestRule.onAllNodesWithTag("kcal").assertCountEquals(0)
  }

  @Test
  fun dropdownMenuInteraction() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Graph) {
        composable(Route.Graph) { FullGraphScreen(GraphViewModel()) }
      }
    }

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
