package com.github.se.polyfit.data.processor

import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.meal.MealOccasion
import java.time.LocalDate
import javax.inject.Inject

data class DailyCalorieSummary(val date: LocalDate, val totalCalories: Double)

data class MealOccasionCalorieSummary(val occasion: MealOccasion, val totalCalories: Double)

class LocalDataProcessor @Inject constructor(private val mealDao: MealDao) {

  private fun calculateCalories(sinceDate: LocalDate): List<DailyCalorieSummary> {
    return mealDao
        .getMealsCreatedOnOrAfterDate(sinceDate)
        .groupBy { it.createdAt }
        .map { (date, meals) ->
          val totalCalories = meals.sumOf { it.calculateTotalCalories() }
          DailyCalorieSummary(date, totalCalories)
        }
  }

  fun getCaloriesPerMealOccasionToday(): List<MealOccasionCalorieSummary> {
    return mealDao
        .getMealsCreatedOnOrAfterDate(LocalDate.now())
        .groupBy { it.occasion }
        .map { (occasion, meals) ->
          val totalCalories = meals.sumOf { it.calculateTotalCalories() }
          MealOccasionCalorieSummary(occasion, totalCalories)
        }
  }

  fun getCaloriesLastMonth(): List<DailyCalorieSummary> =
      calculateCalories(LocalDate.now().minusMonths(1))

  fun getCaloriesLastWeek(): List<DailyCalorieSummary> =
      calculateCalories(LocalDate.now().minusWeeks(1))
}
