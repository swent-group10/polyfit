package com.github.se.polyfit.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.github.se.polyfit.viewmodel.meal.DataToPoints

@Composable
@Preview
fun lineChartData(): LineChartData {
    val steps = 10
    val points = DataToPoints()
    val xAxisData = AxisData.Builder()
        .axisStepSize(60.dp)
        .shouldDrawAxisLineTillEnd(false)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .steps(points.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .labelAndAxisLinePadding(30.dp)
        .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
        .labelData { i ->
            val yScale = 2500 / steps
            (i * yScale).toString()
        }
        .build()

    return LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    lineStyle = LineStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    intersectionPoint = IntersectionPoint(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    selectionHighlightPoint = SelectionHighlightPoint(color = MaterialTheme.colorScheme.secondary),
                    shadowUnderLine = ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    ),
                    selectionHighlightPopUp = SelectionHighlightPopUp()
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant)
    )

}