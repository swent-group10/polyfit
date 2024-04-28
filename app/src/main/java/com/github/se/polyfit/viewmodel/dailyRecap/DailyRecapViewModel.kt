package com.github.se.polyfit.viewmodel.dailyRecap

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

  private suspend fun getMealsOnDate(date: LocalDate) {
    _isFetching.value = true

    val newMeals = mealRepository.getMealsOnDate(date).filter { it.isComplete() }
    withContext(Dispatchers.Main) { _meals.value = newMeals } // FYI: UI updates only on Main Thread

    _isFetching.value = false
  }

  init {
    setDate(LocalDate.now())
  }

  fun setDate(date: LocalDate) {
    viewModelScope.launch { getMealsOnDate(date) }
  }
}
