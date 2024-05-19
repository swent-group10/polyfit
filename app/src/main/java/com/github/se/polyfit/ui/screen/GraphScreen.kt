package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.common.extensions.roundTwoDecimal
import co.yml.charts.ui.linechart.LineChart
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.DropDownMenu
import com.github.se.polyfit.ui.components.button.SortingArrows
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.utils.GraphData
import com.github.se.polyfit.ui.viewModel.DisplayScreen
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.github.se.polyfit.ui.viewModel.SortDirection
import com.github.se.polyfit.ui.viewModel.SortPoints

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullGraphScreen(
    viewModel: GraphViewModel = hiltViewModel<GraphViewModel>(),
    goBack: () -> Unit
) {
  val context = LocalContext.current

  val isTestEnvironment = System.getProperty("isTestEnvironment") == "true"

  val sortedBy = remember { mutableStateOf("KCAL") }

  val sortDirection by viewModel.sortDirection.observeAsState(SortDirection.ASCENDING)

  val searchText by viewModel.searchText.observeAsState()
  val graphData by viewModel.graphData.observeAsState(viewModel.initGraphData())
  val filteredGraphData by viewModel.filteredGraphData.observeAsState(emptyList())
  val dataPoints = viewModel.DataPoints()
  val dates = viewModel.DateList()

  Scaffold(topBar = { SimpleTopBar(context.getString(R.string.graphScreenTitle), goBack) }) {
      padding ->
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().testTag("GraphScreenColumn").padding(padding)) {
          Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround) {
                  if (!isTestEnvironment && dataPoints.isNotEmpty()) {
                    LineChart(
                        modifier = Modifier.fillMaxSize().testTag("LineChart"),
                        lineChartData =
                            lineChartData(dataPoints, dates, DisplayScreen.GRAPH_SCREEN))
                  } else {
                    Spacer(modifier = Modifier.fillMaxSize().testTag("LineChartSpacer"))
                  }
                }
          }
          Spacer(modifier = Modifier.height(5.dp))
          Row(
              modifier = Modifier.fillMaxWidth().padding(5.dp),
              horizontalArrangement = Arrangement.SpaceBetween) {
                searchText?.let {
                  OutlinedTextField(
                      value = it,
                      singleLine = true,
                      onValueChange = viewModel::onSearchTextChanges,
                      modifier = Modifier.fillMaxWidth(0.35f).testTag("GraphScreenSearchBar"),
                      placeholder = { Text(text = "Search") },
                      shape = MaterialTheme.shapes.large)
                }
                Row() {
                  SortingArrows(
                      modifier =
                          Modifier.align(Alignment.CenterVertically)
                              .size(30.dp)
                              .testTag("SortingArrow"),
                      direction = sortDirection,
                      onClick = {
                        viewModel.updateSort(
                            SortPoints.valueOf(sortedBy.value),
                            viewModel.inverseDirection(sortDirection))
                      })
                  Spacer(Modifier.width(5.dp))
                  DropDownMenu(
                      SortPoints.toList(),
                      sortedBy,
                      onItemSelect = { sortAttributeName ->
                        val sortAttribute = SortPoints.valueOf(sortAttributeName)
                        viewModel.updateSort(sortAttribute)
                      })
                }
              }
          Spacer(modifier = Modifier.height(5.dp))
          HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)

          ListOfElements(
              filteredGraphData = filteredGraphData,
              modifier = Modifier.fillMaxWidth().weight(1f).testTag("ElementsList"))
        }
  }
}

@Composable
private fun ListOfElements(filteredGraphData: List<GraphData>, modifier: Modifier) {
  val context = LocalContext.current
  LazyColumn(modifier = modifier) {
    items(filteredGraphData) { data ->
      Spacer(modifier = Modifier.height(5.dp))
      Row(
          modifier = Modifier.fillMaxWidth().testTag("ElementsRow"),
          horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 4.dp)) {
                  Text(
                      text = context.getString(R.string.kcalvalue, data.kCal.roundTwoDecimal()),
                      modifier = Modifier.testTag("kcal"))
                  Text(
                      text =
                          context
                              .getString(R.string.weightvalue, data.weight.roundTwoDecimal())
                              .takeIf { data.weight > 0.0 } ?: "",
                      modifier = Modifier.testTag("weight"))
                }
            Text(
                text = context.getString(R.string.datevalue, data.date.toString()),
                modifier = Modifier.padding(end = 4.dp).testTag("Date"))
          }
      HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)
    }
  }
}
