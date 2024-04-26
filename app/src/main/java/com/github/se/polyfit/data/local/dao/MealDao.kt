package com.github.se.polyfit.data.local.dao

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

  fun insert(meal: Meal) {
    insert(MealEntity.toMealEntity(meal))
  }

  @Query("SELECT * FROM MealTable WHERE firebaseId = :id LIMIT 1 ")
  fun getMealEntityByFirebaseID(id: String): MealEntity?

  fun getMealByFirebaseID(id: String): Meal? {
    val meal = getMealEntityByFirebaseID(id)
    return meal?.toMeal()
  }

  @Query("SELECT * FROM MEALTABLE WHERE createdAt >= :date ")
  fun getMealsCreatedOnOrAfterDate(date: LocalDate): List<Meal>

  @Query("SELECT * FROM MEALTABLE WHERE createdAt <= :date ")
  fun getMealsCreatedOnDate(date: LocalDate): List<Meal>

  @Query("DELETE FROM MealTable WHERE firebaseId = :id") fun deleteByFirebaseID(id: String)

  @Query("DELETE FROM MealTable") fun deleteAll()
}
