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
import junit.framework.TestCase.assertFalse
import kotlin.test.assertContains
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
    mealDao.deleteAll()
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

  @Test
  fun getMealByFirebaseID() {
    mealDao.deleteAll()
    val meal = Meal.default().apply { firebaseId = "aer1" }
    mealDao.insert(meal)

    val mealFromDB = mealDao.getMealByFirebaseID("aer1")

    assertEquals(meal, mealFromDB)
  }

  @Test
  fun getAllIngredients() {
    val ingredientList =
        mutableListOf(
            Ingredient("Oats", 12, 192.2, MeasurementUnit.G),
            Ingredient("Milk", 200, 12.0, MeasurementUnit.ML))
    mealDao.deleteAll()
    val meal =
        Meal(
            name = "Oatmeal",
            occasion = MealOccasion.BREAKFAST,
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
            ingredients = ingredientList,
            firebaseId = "1",
            createdAt = LocalDate.now())
    mealDao.insert(MealEntity.toMealEntity(meal))

    val ingredients = mealDao.getAllIngredients()

    assertEquals(ingredients.size, 2)
    assertContains(ingredients, ingredientList.first())
  }

  @Test
  fun testGetAllMealsFromACertainDate() {
    mealDao.deleteAll()

    val meal = createMeal("Oatmeal", "1", LocalDate.now())
    val meal2 = createMeal("Chocolate", "2", LocalDate.now())
    val meal3 = createMeal("Lobster", "3", LocalDate.now().minusWeeks(1))

    mealDao.insert(meal)
    mealDao.insert(meal2)
    mealDao.insert(meal3)

    val meals = mealDao.getMealsCreatedOnOrAfterDate(LocalDate.now())

    assert(meals.size == 2)
    assert(meals.contains(meal))
    assert(meals.contains(meal2))
    assertFalse(meals.contains(meal3))

    val newMeals = mealDao.getMealsCreatedOnOrAfterDate(LocalDate.now().minusMonths(1))

    assert(newMeals.contains(meal3))
  }

  @Test
  fun testGetMealByDatabaseID() {
    mealDao.deleteAll()
    val meal = createMeal("Oatmeal", "1", LocalDate.now())
    val id = mealDao.insert(meal)
    val mealFromDB = mealDao.getMealByDatabaseID(id)
    assertEquals(meal, mealFromDB)
  }

  @Test
  fun testDeleteByDatabaseID() {
    mealDao.deleteAll()
    val meal = createMeal("Oatmeal", "1", LocalDate.now())
    val id = mealDao.insert(meal)
    mealDao.deleteByDatabaseID(id)
    val mealFromDB = mealDao.getMealByDatabaseID(id)
    assertEquals(null, mealFromDB)
  }

  private fun createMeal(name: String, firebaseId: String, createdAt: LocalDate): Meal {
    return Meal(
        name = name,
        occasion = MealOccasion.BREAKFAST,
        mealID = 1,
        mealTemp = 20.0,
        nutritionalInformation =
            NutritionalInformation(
                mutableListOf(
                    Nutrient("calories", 100.0, MeasurementUnit.UG),
                    Nutrient("protein", 10.0, MeasurementUnit.G),
                    Nutrient("carbs", 20.0, MeasurementUnit.G),
                    Nutrient("fat", 5.0, MeasurementUnit.ML))),
        ingredients =
            mutableListOf(
                Ingredient("Oats", 12, 192.2, MeasurementUnit.G),
                Ingredient("Milk", 200, 12.0, MeasurementUnit.ML)),
        firebaseId = firebaseId,
        createdAt = createdAt)
  }

  @After
  fun closeDB() {
    mealDatabase.close()
  }
}
