package com.github.se.polyfit.data.processor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import java.time.LocalDate
import javax.inject.Inject

data class DailyCalorieSummary(val date: LocalDate, val totalCalories: Double)

data class DailyWeightSummary(val date: LocalDate, val totalWeight: Nutrient)

/**
 * This class is responsible for processing the data from the local database and making it usable
 * for the UI.
 */
class LocalDataProcessor @Inject constructor(private val mealDao: MealDao, private val user: User) {
  /**
   * Calculate the total calories consumed since a specific date. This is calculated by summing the
   * total calories of all meals created on or after the specified date. If an error occurs, an
   * empty list is returned.
   */
  fun calculateCaloriesSince(sinceDate: LocalDate): List<DailyCalorieSummary> {
    return try {
      mealDao
          .getMealsCreatedOnOrAfterDate(sinceDate, user.id)
          .filter { it.isComplete() && it.occasion != MealOccasion.OTHER }
          .groupBy { it.createdAt }
          .map { (date, meals) ->
            DailyCalorieSummary(date, meals.sumOf { it.calculateTotalCalories() })
          }
    } catch (e: Exception) {
      Log.e("LocalDataProcessor", "Error getting calories since $sinceDate", e)
      emptyList()
    }
  }

  fun getCaloriesPerMealOccasionToday(): Map<MealOccasion, Double> {
    return getCaloriesPerMealOnSpecificDay(LocalDate.now())
  }

  /**
   * Get the calories per meal occasion today as a LiveData object. If an error occurs, an empty
   * list is returned.
   */
  fun getCaloriesPerMealOccasionTodayLiveData(): LiveData<List<Pair<MealOccasion, Double>>> {
    return try {
      mealDao.getMealsCreatedOnDateLiveData(LocalDate.now(), user.id).map { meals ->
        val allOccasion =
            MealOccasion.entries.filter { it != MealOccasion.OTHER }.map { Pair(it, 0.0) }

        val mealValues =
            meals
                .toMutableList()
                .filter { it.isComplete() && it.occasion != MealOccasion.OTHER }
                .groupBy { it.occasion }
                .mapValues { (_, meals) -> meals.sumOf { it.calculateTotalCalories() } }
                .toList()

        allOccasion.map { pair ->
          val (occasion, _) = pair
          val value = mealValues.find { it.first == occasion }?.second ?: 0.0
          Pair(occasion, value)
        }
      }
    } catch (e: Exception) {
      Log.e("LocalDataProcessor", "Error getting calories per meal occasion today", e)
      MutableLiveData(emptyList())
    }
  }

  private fun getCaloriesPerMealOnSpecificDay(day: LocalDate): Map<MealOccasion, Double> {
    val mealsOnDay = mealDao.getMealsCreatedOnOrAfterDate(day, user.id)
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

  /**
   * Get the weight since a specific date. This is calculated by summing the weight of all
   * ingredients in all meals created on or after the specified date. If an error occurs, an empty
   * list is returned.
   */
  fun getWeightSince(sinceDate: LocalDate): List<DailyWeightSummary> {
    return try {
      mealDao
          .getMealsCreatedOnOrAfterDate(sinceDate, user.id)
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
    } catch (e: Exception) {
      Log.e("LocalDataProcessor", "Error getting weight since $sinceDate", e)
      emptyList()
    }
  }
}
