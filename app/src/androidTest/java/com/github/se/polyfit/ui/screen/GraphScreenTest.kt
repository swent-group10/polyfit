package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.github.se.polyfit.data.processor.DailyCalorieSummary
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.utils.GraphData
import com.github.se.polyfit.ui.viewModel.DisplayScreen
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import java.time.LocalDate
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GraphScreenTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  fun set() {
    val dataProcessor = mockk<LocalDataProcessor>(relaxed = true)
    val today = LocalDate.now()
    // Create a list of dates for the last 7 days including today
    val dates = (0..6).map { today.minusDays(it.toLong()) }
    // Create a baseline list of GraphData with calories set to 0
    every { dataProcessor.calculateCaloriesSince(any()) } returns
        listOf(
            DailyCalorieSummary(dates[0], 1000.0),
            DailyCalorieSummary(dates[1], 870.2),
            DailyCalorieSummary(dates[2], 1689.9),
            DailyCalorieSummary(dates[3], 1300.0),
            DailyCalorieSummary(dates[4], 1000.0),
            DailyCalorieSummary(dates[5], 2399.3),
            DailyCalorieSummary(dates[6], 2438.0))
    composeTestRule.setContent {
      FullGraphScreen(viewModel = GraphViewModel(dataProcessor), goBack = mockNav::goBack)
    }
  }

  private val mockData =
      listOf(
          GraphData(kCal = 1000.0, LocalDate.of(2024, 3, 11), weight = 45.0),
          GraphData(kCal = 870.2, LocalDate.of(2024, 3, 12), weight = 330.0),
          GraphData(kCal = 1689.98, LocalDate.of(2024, 3, 13), weight = 78.0),
          GraphData(kCal = 1300.0, LocalDate.of(2024, 3, 14), weight = 65.9),
          GraphData(kCal = 1000.0, LocalDate.of(2024, 3, 15), weight = 35.0),
          GraphData(kCal = 2399.3, LocalDate.of(2024, 3, 16), weight = 78.0),
          GraphData(kCal = 2438.0, LocalDate.of(2024, 3, 17), weight = 80.2))

  fun DateList(): List<LocalDate> {
    val dateList: MutableList<LocalDate> = mutableListOf()
    mockData.forEach { data -> dateList.add(data.date) }
    return dateList.toList()
  }

  fun DataToPoints(): List<Point> {
    val data = mockData
    return data.mapIndexed { index, graphData ->
      Point(x = index.toFloat(), y = graphData.kCal.toFloat())
    }
  }

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
  fun testSortingByKcalAscending() {
    set()
    composeTestRule
        .onNodeWithTag("SortingArrow")
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule.waitForIdle()
    val nodes = composeTestRule.onAllNodesWithTag("kcal")
    val sortedData =
        listOf(
            "870.2 kCal",
            "1000.0 kCal",
            "1000.0 kCal",
            "1300.0 kCal",
            "1689.9 kCal",
            "2399.3 kCal",
            "2438.0 kCal")
    nodes.assertCountEquals(sortedData.size)
    sortedData.forEachIndexed { index, kcal ->
      nodes[index].assertTextContains(kcal, ignoreCase = true)
    }
    composeTestRule.onNodeWithContentDescription("ArrowUp").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("ArrowDown").assertIsNotDisplayed()
  }

  @Test
  fun testSortingByKcalDescending() {
    set()
    composeTestRule.onNodeWithTag("SortingArrow").performClick() // Change to ascending
    composeTestRule.waitForIdle()

    val nodes = composeTestRule.onAllNodesWithTag("kcal")
    val sortedData =
        listOf(
            "2438.0 kCal",
            "2399.3 kCal",
            "1689.9 kCal",
            "1300.0 kCal",
            "1000.0 kCal",
            "1000.0 kCal",
            "870.2 kCal")
    nodes.assertCountEquals(sortedData.size)
    sortedData.forEachIndexed { index, kcal ->
      nodes[index].assertTextContains(kcal, ignoreCase = true)
    }
    composeTestRule.onNodeWithContentDescription("ArrowUp").assertDoesNotExist()
    composeTestRule.onNodeWithContentDescription("ArrowDown").assertExists().assertIsDisplayed()
  }

  @Test
  fun graphParameters() {
    composeTestRule.setContent {
      // Define a mock ViewModel or required data setup here
      val pointList = DataToPoints() // Assuming this can be accessed here
      val dateList = DateList() // Assuming this can be accessed here
      val screen = MutableStateFlow(DisplayScreen.GRAPH_SCREEN)
      val steps = 10
      // Invoke the composable to render the chart with test data
      var xAxisData =
          AxisData.Builder()
              .axisLabelAngle(15f)
              .axisLabelDescription { "Date" }
              .axisStepSize(120.dp)
              .shouldDrawAxisLineTillEnd(false)
              .backgroundColor(MaterialTheme.colorScheme.background)
              .steps(pointList.size - 1)
              .labelData { i ->
                if (screen.value == DisplayScreen.GRAPH_SCREEN) {
                  dateList[i].toString()
                } else {
                  ""
                }
              }
              .labelAndAxisLinePadding(15.dp)
              .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
              .build()

      var yAxisData =
          AxisData.Builder()
              .steps(steps)
              .backgroundColor(MaterialTheme.colorScheme.background)
              .labelAndAxisLinePadding(30.dp)
              .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
              .labelData { i ->
                if (screen.value == DisplayScreen.GRAPH_SCREEN) {
                  val yMin = pointList.minOf { it.y }.let { (floor(it / 10) * 10).toInt() }
                  val yMax = pointList.maxOf { it.y }.let { (ceil(it / 10) * 10).toInt() }
                  val yScale = (yMax - yMin) / steps
                  ((i * yScale) + yMin).toFloat().formatToSinglePrecision()
                } else {
                  ""
                }
              }
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
  fun lazyListAndItemsDisplayed() {
    set()
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
    set()

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
    set()

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
    set()

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
    set()

    composeTestRule.onNodeWithTag("Calories Per Day Title").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("BackButton")
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
  }

  @Test
  fun topBarBackFuntionning() {
    set()
    composeTestRule.onNodeWithTag("BackButton").performClick()
    verify { mockNav.goBack() }
    confirmVerified(mockNav)
  }

  @Test
  fun graphSpotReserved() {
    set()

    composeTestRule.onNodeWithTag("GraphScreenColumn").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LineChart").assertDoesNotExist()
    composeTestRule.onNodeWithTag("LineChartSpacer").assertExists().assertIsDisplayed()
  }

  @Test
  fun searchBarShownAndWriteable() {
    set()

    composeTestRule.onNodeWithTag("GraphScreenSearchBar").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("1000")
    composeTestRule.onNodeWithTag("GraphScreenSearchBar").assertTextEquals("1000")
  }

  @Test
  fun searchBarFiltersCorrectly() {
    set()

    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("1000")
    composeTestRule.waitForIdle()
    // Assert only the matching GraphData elements are displayed
    composeTestRule
        .onAllNodesWithText("1000.0 kCal", ignoreCase = true)
        .assertCountEquals(2) // Assuming two entries match this

    composeTestRule.onNodeWithTag("GraphScreenSearchBar").performTextInput("5000")
    composeTestRule.onAllNodesWithTag("kcal").assertCountEquals(0)
  }

  @Test
  fun dropdownMenuInteraction() {
    set()

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
