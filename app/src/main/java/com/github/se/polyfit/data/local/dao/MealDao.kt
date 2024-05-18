package com.github.se.polyfit.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.se.polyfit.data.local.entity.MealEntity
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import java.time.LocalDate

@Dao
interface MealDao {
  @Query("SELECT * FROM MealTable") fun getAll(): List<MealEntity>

  fun getAllMeals(): List<Meal> {
    val meals = getAll()
    return meals.map { it.toMeal() }
  }

  fun getAllIngredients(): List<Ingredient> {
    val meals = getAll()

    return meals.flatMap { it.ingredients }
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(meal: MealEntity)

  // Converts the Meal object to a MealEntity and inserts it into the database.
  // Returns the ID of the inserted meal.
  fun insert(meal: Meal): String {
    val mealEntity = MealEntity.toMealEntity(meal)
    insert(mealEntity)
    return mealEntity.id
  }

  @Query("SELECT * FROM MEALTABLE WHERE createdAt >= :date ")
  fun getMealsCreatedOnOrAfterDate(date: LocalDate): List<Meal>

  @Query("SELECT * FROM MEALTABLE WHERE createdAt >= :date ")
  fun getMealsCreatedOnOrAfterDateLiveData(date: LocalDate): LiveData<List<Meal>>

  @Query("SELECT * FROM MEALTABLE WHERE createdAt == :date ")
  fun getMealsCreatedOnDate(date: LocalDate): List<Meal>

  @Query("DELETE FROM MealTable WHERE id = :id") fun deleteById(id: String)

  @Query("DELETE FROM MealTable") fun deleteAll()

  /**
   * Get a meal by its database ID which is the primary key of the MealEntity use to differentiate
   * between meals who do not have yet a firebase ID
   */
  @Query("SELECT * FROM MealTable WHERE id = :id LIMIT 1 ")
  fun getMealEntityById(id: String): MealEntity?

  fun getMealById(id: String): Meal? {
    return getMealEntityById(id)?.toMeal()
  }
}
