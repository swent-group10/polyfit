package com.github.se.polyfit.ui.viewModel

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
  val sortDirection: LiveData<SortDirection> = _sortDirection

  private val _searchText = MutableLiveData("")
  val searchText: LiveData<String> = _searchText

  private val _graphData = MediatorLiveData<List<GraphData>>()
  val graphData: LiveData<List<GraphData>> = _graphData

  private val _filteredGraphData = MediatorLiveData<List<GraphData>>()
  val filteredGraphData: LiveData<List<GraphData>> = _filteredGraphData

  init {
    fetchCaloriesData()
    _filteredGraphData.addSource(_graphData) { filterAndSortData() }
    _filteredGraphData.addSource(_searchText) { filterAndSortData() }
    _filteredGraphData.addSource(_sortedPoints) { filterAndSortData() }
    _filteredGraphData.addSource(_sortDirection) { filterAndSortData() }
    updateSort(SortPoints.KCAL, SortDirection.ASCENDING)
  }

  fun fetchCaloriesData(numDays: Int = 7) {
    viewModelScope.launch(Dispatchers.IO) {
      val baselineGraphData = initGraphData()

      val actualData =
          dataProcessor.calculateCaloriesSince(LocalDate.now().minusDays(numDays.toLong())).map {
            GraphData(kCal = it.totalCalories, date = it.date, weight = 0.0)
          }

      actualData.forEach { data ->
        val index = baselineGraphData.indexOfFirst { it.date == data.date }
        if (index != -1) {
          baselineGraphData[index] = data
        }
      }
      updateGraphData(baselineGraphData)
    }
  }

  fun updateGraphData(graphDataList: List<GraphData>) {
    _graphData.postValue(graphDataList)
    filterAndSortData()
  }

  fun initGraphData(): MutableList<GraphData> {
    val today = LocalDate.now()
    val dates = (0..6).map { today.minusDays(it.toLong()) }
    val baselineGraphData =
        dates.map { GraphData(kCal = 0.0, date = it, weight = 0.0) }.toMutableList()

    return baselineGraphData
  }

  fun onSearchTextChanges(text: String) {
    _searchText.postValue(text)
    filterAndSortData()
  }

  fun updateSort(attribute: SortPoints, direction: SortDirection = SortDirection.ASCENDING) {
    _sortDirection.postValue(direction)
    _sortedPoints.postValue(attribute)
    filterAndSortData()
  }

  fun inverseDirection(direction: SortDirection): SortDirection {
    return when (direction) {
      SortDirection.ASCENDING -> SortDirection.DESCENDING
      SortDirection.DESCENDING -> SortDirection.ASCENDING
    }
  }

  private fun filterAndSortData() {
    val text = _searchText.value ?: ""
    val data = _graphData.value ?: emptyList()
    if (data.isEmpty()) {
      _filteredGraphData.postValue(emptyList())
      return
    }

    val attribute = _sortedPoints.value ?: SortPoints.KCAL
    val direction = _sortDirection.value ?: SortDirection.ASCENDING

    var filteredData =
        if (text.isBlank()) {
          data
        } else {
          data.filter { it.doesMatchSearchQuery(text) }
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
    val finalData = filteredData.filter { it.kCal > 0 }
    _filteredGraphData.postValue(finalData)
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
