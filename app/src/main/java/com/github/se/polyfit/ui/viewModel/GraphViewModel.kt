package com.github.se.polyfit.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.ui.utils.GraphData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class SortPoints {
  KCAL,
  WEIGHT;

  override fun toString(): String {
    return this.name
  }

  companion object {
    fun toList(): List<String> {
      return values().map { it.toString() }
    }
  }
}

enum class DisplayScreen {
  OVERVIEW,
  GRAPH_SCREEN
}

enum class SortDirection {
  ASCENDING,
  DESCENDING
}

@HiltViewModel
class GraphViewModel @Inject constructor(private val dataProcessor: LocalDataProcessor) :
    ViewModel() {
  private val _sortedPoints = MutableLiveData(SortPoints.KCAL)

  private val _sortDirection = MutableLiveData(SortDirection.ASCENDING)

  private val _searchText = MutableLiveData("")
  val searchText: LiveData<String> = _searchText

  private val _graphData = MediatorLiveData<List<GraphData>>()
  val graphData = _graphData

  init {
    fetchCaloriesData()
  }

  private fun fetchCaloriesData(numDays: Int = 7) {
    viewModelScope.launch(Dispatchers.IO) {
      val today = LocalDate.now()
      // Create a list of dates for the last 7 days including today
      val dates = (0 ..< numDays).map { today.minusDays(it.toLong()) }
      // Create a baseline list of GraphData with calories set to 0
      val baselineGraphData =
          dates.map { GraphData(kCal = 0.0, date = it, weight = 0.0) }.toMutableList()

      Log.d("ViewModelTest", "Fetching Data...")
      // Fetch data from the processor
      val actualData =
          dataProcessor.calculateCaloriesSince(LocalDate.now().minusDays(numDays.toLong())).map {
            GraphData(
                kCal = it.totalCalories,
                date = it.date,
                weight = 0.0) // Assuming no weight data available
          }

      Log.d("ViewModelTest", "Fetched Data : $actualData")

      // Replace baseline data with actual data where available
      actualData.forEach { data ->
        val index = baselineGraphData.indexOfFirst { it.date == data.date }
        if (index != -1) {
          baselineGraphData[index] = data
        }
      }
      Log.d("ViewModelTest", "Data output : $baselineGraphData")

      // Post the combined data to LiveData
      _graphData.postValue(baselineGraphData)
    }
  }

  fun initGraphData(): MutableList<GraphData> {
    val today = LocalDate.now()
    // Create a list of dates for the last 7 days including today
    val dates = (0..6).map { today.minusDays(it.toLong()) }
    // Create a baseline list of GraphData with calories set to 0
    val baselineGraphData =
        dates.map { GraphData(kCal = 0.0, date = it, weight = 0.0) }.toMutableList()

    return baselineGraphData
  }

  fun onSearchTextChanges(text: String) {
    _searchText.value = text
    _graphData.value = filterAndSortData()
  }

  fun updateSort(attribute: SortPoints) {
    _sortedPoints.value = attribute
    _graphData.value = filterAndSortData()
  }

  private fun updateGraphData(): LiveData<List<GraphData>> {
    val result = MediatorLiveData<List<GraphData>>()
    result.addSource(_searchText) { result.value = filterAndSortData() }
    result.addSource(_graphData) { result.value = filterAndSortData() }
    result.addSource(_sortedPoints) { result.value = filterAndSortData() }
    result.addSource(_sortDirection) { result.value = filterAndSortData() }
    return result
  }

  private fun filterAndSortData(): List<GraphData> {
    val text = _searchText.value ?: ""
    val data = _graphData.value ?: emptyList()
    if (data.isEmpty()) return emptyList()
    val attribute = _sortedPoints.value ?: SortPoints.KCAL
    val direction = _sortDirection.value ?: SortDirection.ASCENDING

    var filteredData =
        if (text.isBlank()) data
        else
            data.filter {
              Log.d("FilterTest", "Filtering data : ${it.kCal} contains text : $text")
              it.doesMatchSearchQuery(text)
            }
    filteredData =
        when (attribute) {
          SortPoints.KCAL ->
              if (direction == SortDirection.ASCENDING) filteredData.sortedBy { it.kCal }
              else filteredData.sortedByDescending { it.kCal }
          SortPoints.WEIGHT ->
              if (direction == SortDirection.ASCENDING) filteredData.sortedBy { it.weight }
              else filteredData.sortedByDescending { it.weight }
        }
    Log.d("FilterTest", "Filtered Data: $filteredData")
    return filteredData
  }

  fun DataPoints(): List<Point> {
    val data = _graphData.value ?: initGraphData()
    return data.mapIndexed { index, graphData ->
      Point(x = index.toFloat(), y = graphData.kCal.toFloat())
    }
  }

  fun DateList(): List<LocalDate> {
    val dateList: MutableList<LocalDate> = mutableListOf()
    val data = _graphData.value ?: initGraphData()
    data.forEach { d -> dateList.add(d.date) }
    return dateList
  }
}
