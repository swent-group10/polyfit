package com.github.se.polyfit.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.github.se.polyfit.viewmodel.graph.DisplayScreen
import java.time.LocalDate
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun lineChartData(
    pointList: List<Point>,
    dateList: List<LocalDate>,
    screen: DisplayScreen
): LineChartData {
  val steps = 10
  val xAxisData =
      AxisData.Builder()
          .axisLabelAngle(15f)
          .axisLabelDescription { "Date" }
          .axisStepSize(120.dp)
          .shouldDrawAxisLineTillEnd(false)
          .backgroundColor(MaterialTheme.colorScheme.background)
          .steps(pointList.size - 1)
          .labelData { i ->
            if (screen == DisplayScreen.GRAPH_SCREEN) {
              dateList[i].toString()
            } else {
              ""
            }
          }
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
            if (screen == DisplayScreen.GRAPH_SCREEN) {
              val yMin = pointList.minOf { it.y }.let { (floor(it / 10) * 10).toInt() }
              val yMax = pointList.maxOf { it.y }.let { (ceil(it / 10) * 10).toInt() }
              val yScale = (yMax - yMin) / steps
              ((i * yScale) + yMin).toFloat().formatToSinglePrecision()
            } else {
              ""
            }
          }
          .build()

  return LineChartData(
      linePlotData =
          LinePlotData(
              lines =
                  listOf(
                      Line(
                          dataPoints = pointList,
                          lineStyle =
                              LineStyle(
                                  color = MaterialTheme.colorScheme.onBackground,
                                  lineType = LineType.SmoothCurve(isDotted = false)),
                          intersectionPoint =
                              IntersectionPoint(color = MaterialTheme.colorScheme.primary),
                          selectionHighlightPoint = null,
                          shadowUnderLine =
                              ShadowUnderLine(
                                  alpha = 0.5f,
                                  brush =
                                      Brush.verticalGradient(
                                          colors =
                                              listOf(
                                                  MaterialTheme.colorScheme.inversePrimary,
                                                  MaterialTheme.colorScheme.background))),
                          selectionHighlightPopUp = null))),
      backgroundColor = MaterialTheme.colorScheme.surface,
      xAxisData = xAxisData,
      yAxisData = yAxisData,
      gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant))
}
