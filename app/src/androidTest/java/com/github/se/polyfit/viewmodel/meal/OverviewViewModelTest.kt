package com.github.se.polyfit.viewmodel.meal

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class OverviewViewModelTest {

  private val mockMealDao: MealDao = mockk()
  private val mockSpoonacularApiCaller: SpoonacularApiCaller = mockk()
  private lateinit var overviewViewModel: OverviewViewModel
  private lateinit var context: Context

  @Before
  fun setup() {

    context = ApplicationProvider.getApplicationContext()

    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    overviewViewModel =
        OverviewViewModel(mockMealDao, mockSpoonacularApiCaller, User.testUser(), mockk())
  }

  @Test
  fun storeMeal_withNonNullBitmap_storesMeal() {
    val bitmap: Bitmap = mockk()
    val meal: Meal = mockk(relaxed = true)
    every { mockSpoonacularApiCaller.getMealsFromImage(bitmap) } returns meal
    every { mockMealDao.insert(meal) } returns "1"
    Assert.assertEquals("1", overviewViewModel.storeMeal(bitmap))
  }

  @Test
  fun storeMeal_withNullBitmap_returnsNull() {
    val result = overviewViewModel.storeMeal(null)

    Assert.assertNull(result)
  }

  @Test
  fun deleteByDBId_deletesMeal() {
    val id = "1L"
    every { mockMealDao.deleteById(id) } returns Unit

    // Call the method under test
    overviewViewModel.deleteById(id)

    // Verify that the method was called with the correct parameters
    verify { mockMealDao.deleteById(id) }
  }

  @Test
  fun getMealsByCalorieRange_returnsCorrectMeals() {
    // Prepare the meals
    val meal1 =
        Meal(
            name = "Meal 1",
            occasion = MealOccasion.BREAKFAST,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.KCAL))))))
    val meal2 =
        Meal(
            name = "Meal 2",
            occasion = MealOccasion.LUNCH,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 200.0, MeasurementUnit.KCAL))))))

    val meal3 =
        Meal(
            name = "Meal 3",
            occasion = MealOccasion.DINNER,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 300.0, MeasurementUnit.KCAL))))))

    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealsByCalorieRange(150.0, 250.0)

    // Verify the result
    Assert.assertEquals(1, result.size)
    Assert.assertEquals(meal2, result[0])
  }

  @Test
  fun getMealsByCalorieRange_emptyDatabase() {

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns listOf()

    // Call the method under test
    val result = overviewViewModel.getMealsByCalorieRange(150.0, 250.0)

    // Verify the result
    Assert.assertEquals(0, result.size)
  }

  @Test
  fun getMealsByName_noMatchingMeals() {
    // Prepare the meals
    val meal1 = Meal.default().apply { name = "Fish" }
    val meal2 = Meal.default().apply { name = "Beef" }
    val meal3 = Meal.default().apply { name = "Chicken" }
    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealsByName("Fish")

    // Verify the result
    Assert.assertEquals(1, result.size)
  }

  @Test
  fun getMealsByOccasion_returnsCorrectMeals() {
    // Prepare the meals
    val meal1 = Meal(name = "Breakfast Meal", occasion = MealOccasion.BREAKFAST)
    val meal2 = Meal(name = "Lunch Meal", occasion = MealOccasion.LUNCH)
    val meal3 = Meal(name = "Dinner Meal", occasion = MealOccasion.DINNER)
    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealsByOccasion(MealOccasion.BREAKFAST)

    // Verify the result
    Assert.assertEquals(1, result.size)
    Assert.assertEquals(meal1, result[0])
  }

  @Test
  fun getMealsByOccasion_noMatchingMeals() {
    // Prepare the meals
    val meal1 = Meal(name = "Breakfast Meal", occasion = MealOccasion.BREAKFAST)
    val meal2 = Meal(name = "Lunch Meal", occasion = MealOccasion.LUNCH)
    val meal3 = Meal(name = "Dinner Meal", occasion = MealOccasion.DINNER)
    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealsByOccasion(MealOccasion.SNACK)

    // Verify the result
    Assert.assertEquals(0, result.size)
  }

  @Test
  fun getMealsByMultipleCriteria_returnsCorrectMeals() {
    // Prepare the meals
    val meal1 =
        Meal(
            name = "Chicken Salad",
            occasion = MealOccasion.BREAKFAST,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 150.0, MeasurementUnit.KCAL))))))
    val meal2 =
        Meal(
            name = "Beef Stew",
            occasion = MealOccasion.LUNCH,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 250.0, MeasurementUnit.KCAL))))))
    val meal3 =
        Meal(
            name = "Chicken Soup",
            occasion = MealOccasion.DINNER,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 350.0, MeasurementUnit.KCAL))))))

    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result =
        overviewViewModel.getMealsByMultipleCriteria(
            "Chicken", 100.0, 200.0, MealOccasion.BREAKFAST)

    // Verify the result
    Assert.assertEquals(1, result.size)
    Assert.assertEquals(meal1, result[0])
  }

  @Test
  fun getMealsByMultipleCriteria_OneMatchingMeals() {
    // Prepare the meals
    val meal1 =
        Meal(
            name = "Chicken Salad",
            occasion = MealOccasion.BREAKFAST,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 150.0, MeasurementUnit.KCAL))))))
    val meal2 =
        Meal(
            name = "Beef Stew",
            occasion = MealOccasion.LUNCH,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 250.0, MeasurementUnit.KCAL))))))
    val meal3 =
        Meal(
            name = "Chicken Soup",
            occasion = MealOccasion.DINNER,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 350.0, MeasurementUnit.KCAL))))))

    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result =
        overviewViewModel.getMealsByMultipleCriteria("Fish", 100.0, 200.0, MealOccasion.BREAKFAST)

    // Verify the result
    Assert.assertEquals(1, result.size)
  }

  @Test
  fun getMealWithHighestCalories_returnsCorrectMeal() {
    // Prepare the meals
    val meal1 =
        Meal(
            name = "Chicken Salad",
            occasion = MealOccasion.BREAKFAST,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 150.0, MeasurementUnit.KCAL))))))
    val meal2 =
        Meal(
            name = "Beef Stew",
            occasion = MealOccasion.LUNCH,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 250.0, MeasurementUnit.KCAL))))))
    val meal3 =
        Meal(
            name = "Chicken Soup",
            occasion = MealOccasion.DINNER,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 350.0, MeasurementUnit.KCAL))))))

    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealWithHighestCalories()

    // Verify the result
    Assert.assertEquals(meal3, result)
  }

  @Test
  fun getMealWithHighestCalories_noMealsInDatabase() {
    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns listOf()

    // Call the method under test
    val result = overviewViewModel.getMealWithHighestCalories()

    // Verify the result
    Assert.assertNull(result)
  }

  fun getMealsByMultipleCriteria_noMatchingMeals() {
    // Prepare the meals
    val meal1 =
        Meal(
            name = "Chicken Salad",
            occasion = MealOccasion.BREAKFAST,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 150.0, MeasurementUnit.KCAL))))))
    val meal2 =
        Meal(
            name = "Beef Stew",
            occasion = MealOccasion.LUNCH,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 250.0, MeasurementUnit.KCAL))))))
    val meal3 =
        Meal(
            name = "Chicken Soup",
            occasion = MealOccasion.DINNER,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 350.0, MeasurementUnit.KCAL))))))

    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result =
        overviewViewModel.getMealsByMultipleCriteria("Fish", 0.0, 50.0, MealOccasion.BREAKFAST)

    // Verify the result
    Assert.assertEquals(0, result.size)
  }

  @Test
  fun getMealWithLowestCalories_returnsCorrectMeal() {
    // Prepare the meals
    val meal1 =
        Meal(
            name = "Chicken Salad",
            occasion = MealOccasion.BREAKFAST,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 150.0, MeasurementUnit.KCAL))))))
    val meal2 =
        Meal(
            name = "Beef Stew",
            occasion = MealOccasion.LUNCH,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 250.0, MeasurementUnit.KCAL))))))
    val meal3 =
        Meal(
            name = "Chicken Soup",
            occasion = MealOccasion.DINNER,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.KCAL))))))
    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealWithLowestCalories()

    // Verify the result
    Assert.assertEquals(meal3, result)
  }

  @Test
  fun getMealWithLowestCalories_noMealsInDatabase() {
    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns listOf()

    // Call the method under test
    val result = overviewViewModel.getMealWithLowestCalories()

    // Verify the result
    Assert.assertNull(result)
  }

  @Test
  fun getMealWithHighestCalories_returnFails() {
    // Prepare the meals
    val meal1 =
        Meal(
            occasion = MealOccasion.BREAKFAST,
            name = "Chicken Salad",
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "ingredient1",
                        id = 0,
                        amount = 10.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.KCAL))))))

    val allMeals = listOf(meal1)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals(any()) } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealWithHighestCalories()

    // Verify the result
    Assert.assertEquals(meal1, result)
  }

  @Test
  fun getUserDisplayName() {
    val result = overviewViewModel.getUserName()
    Assert.assertEquals(User.testUser().displayName, result)
  }

  @Test
  fun getEmailIFDispalyIsNotSet() {
    val user = User.testUser().apply { displayName = null }
    val overviewViewModel = OverviewViewModel(mockMealDao, mockSpoonacularApiCaller, user, mockk())
    val result = overviewViewModel.getUserName()
    Assert.assertEquals(user.email, result)
  }

  @Test
  fun uriToBitmap_returnsBitmap() {
    val uri = Uri.parse("android.resource://com.github.se.polyfit/drawable/picture_example")
    val bitmap = overviewViewModel.uriToBitmap(context, uri)
    assertTrue(bitmap is Bitmap)
  }

  @Test
  fun handleSelectedImage_withUri_returnsString() {
    val uri = Uri.parse("android.resource://com.github.se.polyfit/drawable/picture_example")
    every { overviewViewModel.storeMeal(any()) } returns "1"
    every { mockSpoonacularApiCaller.getMealsFromImage(any()) } returns Meal.default()
    every { mockMealDao.insert(any<Meal>()) } returns "1"
    val result = overviewViewModel.handleSelectedImage(context, uri, overviewViewModel)
    assertEquals("1", result)
  }

  @Test
  fun handleSelectedImage_withoutUri_returnsNull() {
    val result = overviewViewModel.handleSelectedImage(context, null, overviewViewModel)
    assertNull(result)
  }
}
