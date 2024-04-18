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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.ui.linechart.LineChart
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.viewmodel.meal.GraphViewModel

@Composable
fun FullGraphScreen() {
  val viewModel = viewModel<GraphViewModel>()
  val searchText by viewModel.searchText.collectAsState()
  val graphData by viewModel.graphData.collectAsState()
  val isSearching by viewModel.isSearching.collectAsState()
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)) {
      LineChart(modifier = Modifier.fillMaxSize(), lineChartData = lineChartData())
    }
    Spacer(modifier = Modifier.height(5.dp))
    TextField(
        value = searchText,
        onValueChange = viewModel::onSearchTextChanges,
        modifier = Modifier.fillMaxWidth(0.4f).align(Alignment.Start).padding(start = 5.dp),
        placeholder = { Text(text = "Search") })
    Spacer(modifier = Modifier.height(5.dp))
    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)
    LazyColumn(
      modifier = Modifier.fillMaxWidth().weight(1f)
    ){
      items(graphData){data ->
        Spacer(modifier = Modifier.height(5.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ){
          Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 4.dp)
          ){
            Text(
              text = "${data.kCal} kCal"
            )
            Text(
              text = "${data.weight} lbs"
            )
          }
          Text(
            modifier = Modifier.padding(end = 4.dp),
            text = "${data.day} ${data.month}"
          )
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)
      }
    }
  }
}
