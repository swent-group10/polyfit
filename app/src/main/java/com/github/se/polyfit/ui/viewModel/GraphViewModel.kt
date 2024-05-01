package com.github.se.polyfit.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.github.se.polyfit.ui.utils.GraphData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

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

enum class SortDirection {
    ASCENDING,
    DESCENDING
}

@HiltViewModel
class GraphViewModel @Inject constructor() : ViewModel() {
    private val _sortedPoints = MutableLiveData(SortPoints.KCAL)

    private val _sortDirection = MutableLiveData(SortDirection.ASCENDING)

    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> = _searchText

    private val _graphData = MutableLiveData(mockData)
    val graphData = updateGraphData()

    fun onSearchTextChanges(text: String) {
        _searchText.value = text
    }

    fun updateSort(attribute: SortPoints) {
        _sortedPoints.value = attribute
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
        val attribute = _sortedPoints.value ?: SortPoints.KCAL
        val direction = _sortDirection.value ?: SortDirection.ASCENDING

        var filteredData =
            if (text.isBlank()) data else data.filter { it.doesMatchSearchQuery(text) }
        filteredData =
            when (attribute) {
                SortPoints.KCAL ->
                    if (direction == SortDirection.ASCENDING) filteredData.sortedBy { it.kCal }
                    else filteredData.sortedByDescending { it.kCal }

                SortPoints.WEIGHT ->
                    if (direction == SortDirection.ASCENDING) filteredData.sortedBy { it.weight }
                    else filteredData.sortedByDescending { it.weight }
            }
        return filteredData
    }
}

// Will be modified further when Data is linked

private val mockData =
    listOf(
        GraphData(kCal = 1000.0, day = 11, month = "Mar.", weight = 45.0),
        GraphData(kCal = 870.2, day = 12, month = "Mar.", weight = 330.0),
        GraphData(kCal = 1689.98, day = 13, month = "Mar.", weight = 78.0),
        GraphData(kCal = 1300.0, day = 14, month = "Mar.", weight = 65.9),
        GraphData(kCal = 1000.0, day = 15, month = "Mar.", weight = 35.0),
        GraphData(kCal = 2399.3, day = 16, month = "Mar.", weight = 78.0),
        GraphData(kCal = 2438.0, day = 17, month = "Mar.", weight = 80.2)
    )
private val mockDates =
    listOf(
        LocalDate.of(2024, 3, 11),
        LocalDate.of(2024, 3, 12),
        LocalDate.of(2024, 3, 13),
        LocalDate.of(2024, 3, 14),
        LocalDate.of(2024, 3, 15),
        LocalDate.of(2024, 3, 16),
        LocalDate.of(2024, 3, 17)
    )

fun DateList(): List<LocalDate> {
    return mockDates
}

fun DataToPoints(): List<Point> {
    val data = mockData
    return data.mapIndexed { index, graphData ->
        Point(x = index.toFloat(), y = graphData.kCal.toFloat())
    }
}