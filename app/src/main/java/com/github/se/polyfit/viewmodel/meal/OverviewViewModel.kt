package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.meal.MealOccasion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OverviewViewModel
@Inject
constructor(
    private val mealDao: MealDao,
    private val spoonacularApiCaller: SpoonacularApiCaller,
    private val user: User,
    private val localDataProcessor: LocalDataProcessor
) : ViewModel() {
  private val liveDataStats =
      MutableStateFlow(
          MealOccasion.values().filter { it != MealOccasion.OTHER }.map { Pair(it, 0.0) })

  fun storeMeal(imageBitmap: Bitmap?): Long? {
    if (imageBitmap == null) {
      Log.e("OverviewViewModel", "Image is null")
      return null
    } else {
      val meal = spoonacularApiCaller.getMealsFromImage(imageBitmap)

      val mealId = mealDao.insert(meal)

      if (mealId == null) {
        Log.e("OverviewViewModel", "Meal could not be inserted into the database")
        return null
      } else {
        return mealId
      }
    }
  }

  fun getUserInfo(): String {
    if (user.givenName.isNullOrEmpty()) {
      return user.email
    } else {
      return user.givenName!!
    }
  }

  fun getUserDailyStats(): StateFlow<List<Pair<MealOccasion, Double>>> {
    viewModelScope.launch(Dispatchers.Default) {
      val stats =
          localDataProcessor.getCaloriesPerMealOccasionToday().toList().filter {
            it.first != MealOccasion.OTHER
          }
      if (stats.toList() != liveDataStats.value) {
        liveDataStats.emit(stats)
      }
    }
    return liveDataStats
  }
}
