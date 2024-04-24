package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.ui.linechart.LineChart
import com.github.se.polyfit.ui.components.DropDownMenu
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.github.se.polyfit.ui.viewModel.SortPoints

@Composable
fun FullGraphScreen() {
  var isExpanded = remember { mutableStateOf(false) }

  var sortedBy = remember { mutableStateOf("KCAL") }

  val viewModel = viewModel<GraphViewModel>()
  val searchText by viewModel.searchText.collectAsState()
  val graphData by viewModel.graphData.collectAsState()
  //val isSearching by viewModel.isSearching.collectAsState()
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)) {
      LineChart(modifier = Modifier.fillMaxSize(), lineChartData = lineChartData())
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
          OutlinedTextField(
              value = searchText,
              onValueChange = viewModel::onSearchTextChanges,
              modifier = Modifier.fillMaxWidth(0.4f).testTag("GraphScreenSearchBar"),
              placeholder = { Text(text = "Search") },
              shape = MaterialTheme.shapes.large)
          DropDownMenu(
              isExpanded,
              SortPoints.toList(),
              sortedBy,
              onItemSelect = { sortAttributeName ->
                val sortAttribute = SortPoints.valueOf(sortAttributeName)
                viewModel.updateSort(sortAttribute)
              })
        }
    Spacer(modifier = Modifier.height(5.dp))
    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)
    LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
      items(graphData) { data ->
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 4.dp)) {
            Text(text = "${data.kCal} kCal")
            Text(text = "${data.weight} lbs")
          }
          Text(modifier = Modifier.padding(end = 4.dp), text = "${data.day} ${data.month}")
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)
      }
    }
  }
}
