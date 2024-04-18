package com.github.se.polyfit.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.entity.MealEntity
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealDatabaseTest {
  private lateinit var mealDatabase: MealDatabase
  private lateinit var mealDao: MealDao

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()

    mealDatabase = Room.databaseBuilder(context, MealDatabase::class.java, "meal_database").build()
    mealDao = mealDatabase.mealDao()
  }

  @Test
  fun addMeal() {
    val nowTime = LocalDate.now()
    mealDao.insert(
        MealEntity(
            occasion = MealOccasion.BREAKFAST,
            name = "Oatmeal",
            mealID = 1,
            mealTemp = 20.0,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient("calories", 100.0, MeasurementUnit.UG),
                        Nutrient("protein", 10.0, MeasurementUnit.G),
                        Nutrient("carbs", 20.0, MeasurementUnit.G),
                        Nutrient("fat", 5.0, MeasurementUnit.ML)),
                ),
            ingredients =
                mutableListOf(
                    Ingredient("Oats", 12, 192.2, MeasurementUnit.G),
                    Ingredient("Milk", 200, 12.0, MeasurementUnit.ML)),
            firebaseId = "1",
            createdAt = nowTime,
            tags = mutableListOf()))

    val meals = mealDao.getAll().map { it.toMeal() }

    assert(meals.size == 1)
    assertEquals(meals.first().name, "Oatmeal")
    assertEquals(meals.first().createdAt, nowTime)

    val meal2 = Meal.default()

    mealDao.insert(meal2)

    val allMeals = mealDao.getAllMeals()

    assertEquals(allMeals.size, 2)
  }

  @Test
  fun testOneMealDelete() {
    mealDao.deleteAll()

    mealDao.insert(Meal.default().apply { firebaseId = "aer1" })
    mealDao.insert(Meal.default())

    mealDao.deleteByFirebaseID("aer1")

    assertEquals(mealDao.getAllMeals().size, 1)
  }

  @After
  fun closeDB() {
    mealDatabase.close()
  }
}