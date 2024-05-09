package com.github.se.polyfit.viewmodel.dailyRecap

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DailyRecapViewModel @Inject constructor(private val mealRepository: MealRepository) :
    ViewModel() {
  private val _meals: MutableStateFlow<List<Meal>> = MutableStateFlow(mutableListOf())
  val meals: StateFlow<List<Meal>> = _meals

  private val _isFetching: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isFetching: StateFlow<Boolean> = _isFetching

  init {
    getMealsOnDate(LocalDate.now())
  }

  fun getMealsOnDate(date: LocalDate) {
    viewModelScope.launch {
      _isFetching.value = true

      val newMeals = mealRepository.getMealsOnDate(date).filter { it.isComplete() }
      withContext(Dispatchers.Main) {
        Log.v("new meals", newMeals.toString())
        _meals.value = newMeals
      } // FYI: UI updates only on Main Thread

      _isFetching.value = false
    }
  }
}
