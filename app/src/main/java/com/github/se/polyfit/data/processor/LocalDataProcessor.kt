package com.github.se.polyfit.data.processor

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.meal.MealOccasion
import java.time.LocalDate
import javax.inject.Inject

data class DailyCalorieSummary(val date: LocalDate, val totalCalories: Double)

class LocalDataProcessor @Inject constructor(private val mealDao: MealDao) {
  fun calculateCaloriesSince(sinceDate: LocalDate): List<DailyCalorieSummary> {
    return mealDao
        .getMealsCreatedOnOrAfterDate(sinceDate)
        .groupBy { it.createdAt }
        .map { (date, meals) ->
          DailyCalorieSummary(date, meals.sumOf { it.calculateTotalCalories() })
        }
  }

  fun getCaloriesPerMealOccasionToday(): Map<MealOccasion, Double> {
    return getCaloriesPerMealOnSpecificDay(LocalDate.now())
  }

  fun getCaloriesPerMealOccasionTodayLiveData(): LiveData<List<Pair<MealOccasion, Double>>> {
    return mealDao.getMealsCreatedOnOrAfterDateLiveData(LocalDate.now()).map { meals ->
      val allOccasion =
          MealOccasion.values().toList().filter { it != MealOccasion.OTHER }.map { Pair(it, 0.0) }

      val mealValues =
          meals
              .toMutableList()
              .groupBy { it.occasion }
              .mapValues { (_, meals) -> meals.sumOf { it.calculateTotalCalories() } }
              .toList()

      allOccasion.map {
        val (occasion, _) = it
        val value = mealValues.find { it.first == occasion }?.second ?: 0.0
        Pair(occasion, value)
      }
    }
  }

  fun getCaloriesPerMealOnSpecificDay(day: LocalDate): Map<MealOccasion, Double> {
    val mealsOnDay = mealDao.getMealsCreatedOnOrAfterDate(day)
    return MealOccasion.entries
        .toList()
        .associateWith { 0.0 }
        .toMutableMap()
        .mapValues { (occasion, _) ->
          mealsOnDay.filter { it.occasion == occasion }.sumOf { it.calculateTotalCalories() }
        }
  }

  fun getCaloriesLastMonth(): List<DailyCalorieSummary> =
      calculateCaloriesSince(LocalDate.now().minusMonths(1))

  fun getCaloriesLastWeek(): List<DailyCalorieSummary> =
      calculateCaloriesSince(LocalDate.now().minusWeeks(1))
}
