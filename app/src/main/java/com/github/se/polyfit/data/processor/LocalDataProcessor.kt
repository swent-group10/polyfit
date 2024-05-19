package com.github.se.polyfit.data.processor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import java.time.LocalDate
import javax.inject.Inject

data class DailyCalorieSummary(val date: LocalDate, val totalCalories: Double)

data class DailyWeightSummary(val date: LocalDate, val totalWeight: Nutrient)

class LocalDataProcessor @Inject constructor(private val mealDao: MealDao) {
  fun calculateCaloriesSince(sinceDate: LocalDate): List<DailyCalorieSummary> {
    return mealDao
        .getMealsCreatedOnOrAfterDate(sinceDate)
        .filter { it.isComplete() && it.occasion != MealOccasion.OTHER }
        .groupBy { it.createdAt }
        .map { (date, meals) ->
          DailyCalorieSummary(date, meals.sumOf { it.calculateTotalCalories() })
        }
  }

  fun getCaloriesPerMealOccasionToday(): Map<MealOccasion, Double> {
    return getCaloriesPerMealOnSpecificDay(LocalDate.now())
  }

  fun getCaloriesPerMealOccasionTodayLiveData(): LiveData<List<Pair<MealOccasion, Double>>> {
    Log.d("LocalDataProcessor", "${LocalDate.now()}")
    return mealDao.getMealsCreatedOnDateLiveData(LocalDate.now()).map { meals ->
      val allOccasion =
          MealOccasion.values().toList().filter { it != MealOccasion.OTHER }.map { Pair(it, 0.0) }

      val mealValues =
          meals
              .toMutableList()
              .filter { it.isComplete() && it.occasion != MealOccasion.OTHER }
              .groupBy { it.occasion }
              .mapValues { (_, meals) -> meals.sumOf { it.calculateTotalCalories() } }
              .toList()

      Log.d("LocalDataProcessor", "mealValues: $mealValues")

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

  fun getWeightSince(sinceDate: LocalDate): List<DailyWeightSummary> {
    return mealDao
        .getMealsCreatedOnOrAfterDate(sinceDate)
        .groupBy { it.createdAt }
        .map { (date, meals) ->
          DailyWeightSummary(
              date,
              meals.fold(Nutrient("", 0.0, MeasurementUnit.G)) { acc, meal ->
                Nutrient.plus(
                    meal.ingredients
                        .map { Nutrient("", it.amount, it.unit) }
                        .reduce(Nutrient::plus),
                    acc)
              })
        }
  }
}
