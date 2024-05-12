package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.test.ExperimentalTestApi
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
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.GraphData
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.ui.viewModel.DisplayScreen
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.github.se.polyfit.viewmodel.meal.OverviewViewModel
import com.github.se.polyfit.viewmodel.post.CreatePostViewModel
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
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
    val dataProcessor = mockk<LocalDataProcessor>(relaxed = true)
    val mockPostViewModel: CreatePostViewModel = mockk(relaxed = true)

    every { mockPostViewModel.meals.value } returns listOf()
    val mockkOverviewModule: OverviewViewModel = mockk {}

    every { mockkOverviewModule.getUserName() } returns "It's me Mario"

    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) {
          GenericScreen(
              navController = navController,
              content = { paddingValues ->
                OverviewScreen(
                    paddingValues,
                    navController,
                    mockkOverviewModule,
                )
              })
        }

        composable(Route.AddMeal) {
          AddMealFlow(goBack = {}, navigateToHome = {}, mealId = null, mockk(relaxed = true))
        }
        composable(Route.Graph) {
          FullGraphScreen(goBack = {}, viewModel = GraphViewModel(dataProcessor))
        }
        composable(Route.CreatePost) { CreatePostScreen(postViewModel = mockPostViewModel) }
      }
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

    composeTestRule
        .onNodeWithTag("Graph Box", useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("LineChartSpacer", useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun graphScreenAccessed() {
    setup()
    composeTestRule
        .onNodeWithTag("OverviewScreenLazyColumn")
        .performScrollToNode(hasTestTag("Graph Card"))
    composeTestRule.onNodeWithTag("Graph Card").assertHasClickAction().performClick()
    composeTestRule.onNodeWithTag("GraphScreenColumn").assertExists().assertIsDisplayed()
  }
}
