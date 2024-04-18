package com.github.se.polyfit.viewmodel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GraphViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _graphData = MutableStateFlow(mockData)
    val graphData = searchText
        .combine(_graphData){ text, data ->
            if(text.isBlank()){
                data
            }else{
                data.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _graphData.value
        )
    fun onSearchTextChanges(text : String){
        _searchText.value = text
    }
}

data class GraphData(
    val kCal : Double,
    val day : Int,
    val month : String,
    val weight : Double
){
    fun doesMatchSearchQuery(query : String) : Boolean{
        val matchingCombinations = listOf(
            "$kCal",
            "$day",
            month,
            "$weight",
            "$day$month",
            "$day $month",
            "$kCal $day",
            "$kCal $month",
            "$kCal $day $month",
            "$weight $day",
            "$weight $month",
            "$weight $day $month"
        )

        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }
}

val mockData = listOf(
    GraphData(
        kCal = 1000.0,
        day = 11,
        month = "Mar.",
        weight = 45.0
    ),
    GraphData(
        kCal = 870.2,
        day = 12,
        month = "Mar.",
        weight = 33.0
    ),
    GraphData(
        kCal = 1689.98,
        day = 13,
        month = "Mar.",
        weight = 78.0
    ),
    GraphData(
        kCal = 1300.0,
        day = 14,
        month = "Mar.",
        weight = 65.9
    ),
    GraphData(
        kCal = 1000.0,
        day = 15,
        month = "Mar.",
        weight = 35.0
    ),
    GraphData(
        kCal = 2399.3,
        day = 16,
        month = "Mar.",
        weight = 78.0
    ),
    GraphData(
      kCal = 2438.0,
      day = 17,
      month = "Mar.",
      weight = 80.2
    )
)
fun DataToPoints() : List<Point>{
    val data = mockData
    return data.mapIndexed{index, graphData ->
        Point(x = index.toFloat(), y = graphData.kCal.toFloat())
    }
}