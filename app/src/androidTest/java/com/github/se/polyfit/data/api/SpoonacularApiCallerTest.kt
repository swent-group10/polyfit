package com.github.se.polyfit.data.api

import android.graphics.BitmapFactory
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.data.api.Spoonacular.ImageAnalysisResponseAPI
import com.github.se.polyfit.data.api.Spoonacular.RecipeInstruction
import com.github.se.polyfit.data.api.Spoonacular.RecipeNutritionResponseAPI
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.data.mockData.jsonImageAnalysis
import com.github.se.polyfit.data.mockData.jsonRecipeNutrition
import com.github.se.polyfit.data.mockData.recipeFromIngredientsReponse
import com.github.se.polyfit.data.mockData.recipeStepsMock
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import java.io.File
import java.io.InputStream
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpoonacularApiCallerTest {

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var mockWebServer: MockWebServer
  private lateinit var file: File
  private lateinit var spoonacularApiCaller: SpoonacularApiCaller

  private val dispatcher =
      object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
          Log.e("SpoonacularApiCallerTest", "Received request: ${request.method} ${request.path}")
          return when (request.path) {
            "/food/images/analyze" ->
                MockResponse()
                    .setResponseCode(200)
                    .setBody(jsonImageAnalysis.toString())
                    .addHeader("Content-Type", "application/json")
            "/recipes/1/nutritionWidget.json" ->
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(jsonRecipeNutrition.toString())
            "/recipes/61867/nutritionWidget.json" ->
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(jsonRecipeNutrition.toString())
            "/recipes/findByIngredients?ingredients=apple%2Cbanana&number=5&ignorePantry=true&ranking=1" ->
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(recipeFromIngredientsReponse.toString())
            "/recipes/1/analyzedInstructions?stepBreakdown=true" ->
                MockResponse()
                    .setResponseCode(200)
                    .setBody(recipeStepsMock.toString())
                    .addHeader("Content-Type", "application/json")
            "/recipes/641803/analyzedInstructions?stepBreakdown=true" ->
                MockResponse()
                    .setResponseCode(200)
                    .setBody(recipeStepsMock.toString())
                    .addHeader("Content-Type", "application/json")
            "/error" -> MockResponse().setResponseCode(500).setBody("Server error")
            else -> MockResponse().setResponseCode(402).setBody("Not found")
          }
        }
      }
  private val halfFaultyDispacher =
      object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
          Log.e("SpoonacularApiCallerTest", "Received request: ${request.method} ${request.path}")
          return when (request.path) {
            "/food/images/analyze" ->
                MockResponse()
                    .setResponseCode(200)
                    .setBody(jsonImageAnalysis.toString())
                    .addHeader("Content-Type", "application/json")
            "/recipes/1/nutritionWidget.json" ->
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(jsonRecipeNutrition.toString())
            "/recipes/61867/nutritionWidget.json" -> MockResponse().setResponseCode(300)
            "/recipes/findByIngredients?ingredients=apple%2Cbanana&number=5&ignorePantry=true&ranking=1" ->
                MockResponse().setResponseCode(300)
            "/recipes/1/analyzedInstructions?stepBreakdown=true" ->
                MockResponse().setResponseCode(300)
            "/error" -> MockResponse().setResponseCode(500).setBody("Server error")
            else -> MockResponse().setResponseCode(402).setBody("Not found")
          }
        }
      }

  private val faultyDispatcher =
      object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {

          Log.e("SpoonacularApiCallerTest", "Received request: ${request.method} ${request.path}")
          return when (request.path) {
            "/food/images/analyze" -> MockResponse().setResponseCode(404)
            "/recipes/1/nutritionWidget.json" ->
                MockResponse().setResponseCode(500).addHeader("Content-Type", "application/json")
            "/error" -> MockResponse().setResponseCode(500).setBody("Server error")
            else -> MockResponse().setResponseCode(404).setBody("Not found")
          }
        }
      }
  private lateinit var inputStream: InputStream

  @Before
  fun setUp() {
    mockWebServer = MockWebServer()
    mockWebServer.dispatcher = dispatcher
    mockWebServer.start()

    // Set the base URL to the mock server URL
    spoonacularApiCaller = SpoonacularApiCaller()
    spoonacularApiCaller.setBaseUrl(mockWebServer.url("/").toString())

    inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")
    file = File.createTempFile("image", ".jpg")
    file.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }
  }

  @Test
  fun imageAnalysisReturnsExpectedResponse() {

    val response = spoonacularApiCaller.imageAnalysis(file)

    assert(response.status == APIResponse.SUCCESS)
    assert(response.category == "cheesecake")
    assert(response.nutrition.first { it.unit == MeasurementUnit.CAL }.amount == 293.0)
    val fatNutrient = response.nutrition.find { it.nutrientType == "fat" }
    assert(fatNutrient != null)
    assert(fatNutrient!!.amount == 17.0) // Check the value of fat
    assert(fatNutrient.unit == MeasurementUnit.G) // Check the unit of fat
  }

  @Test
  fun imageAnalysisHandlesErrorResponse() {
    // empty server queue
    mockWebServer.dispatcher = faultyDispatcher

    assertFailsWith<Exception> { spoonacularApiCaller.imageAnalysis(file) }
  }

  @Test
  fun getRecipeNutritionReturnsExpectedResponse() {

    val response = spoonacularApiCaller.getRecipeNutrition(1) // Add your test recipeId here

    assert(response.nutrients.first { it.unit == MeasurementUnit.KCAL }.amount == 899.16)
    assert(response.nutrients.first { it.nutrientType == "Carbohydrates" }.amount == 111.24)
    assert(response.nutrients.first { it.nutrientType == "Fat" }.amount == 45.33)
    assert(response.nutrients.first { it.nutrientType == "Protein" }.amount == 11.64)
  }

  @Test
  fun getRecipeNutritionHandlesErrorResponse() {
    mockWebServer.dispatcher = faultyDispatcher

    assertFailsWith<Exception> {
      spoonacularApiCaller.getRecipeNutrition(1) // Add your test recipeId here
    }
  }

  @Test
  fun testImageAnalysisFromJson() {
    val response = ImageAnalysisResponseAPI.fromJsonObject(jsonImageAnalysis)

    assert(response.status == APIResponse.SUCCESS)
    assert(response.category == "cheesecake")
    assert(response.nutrition.first { it.unit == MeasurementUnit.CAL }.amount == 293.0)
    val fatNutrient = response.nutrition.find { it.nutrientType == "fat" }
    assert(fatNutrient != null)
    assert(fatNutrient!!.amount == 17.0) // Check the value of fat
    assert(fatNutrient.unit == MeasurementUnit.G) // Check the unit of fat
  }

  @Test
  fun testRecipeNutritionFromJson() {
    val response = RecipeNutritionResponseAPI.fromJsonObject(jsonRecipeNutrition)

    assert(
        response.nutrients
            .first {
              Log.e("SpoonacularApiCallerTest", "testRecipeNutritionFromJson: it.unit = ${it.unit}")
              it.unit == MeasurementUnit.KCAL
            }
            .amount == 899.16)
    assert(response.nutrients.first { it.nutrientType == "Carbohydrates" }.amount == 111.24)
    assert(response.nutrients.first { it.nutrientType == "Fat" }.amount == 45.33)
    assert(response.nutrients.first { it.nutrientType == "Protein" }.amount == 11.64)
  }

  @Test
  fun testingImageToMeal() {
    mockWebServer.dispatcher = dispatcher

    // Arrange
    val inputStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")

    // Act
    val actualMeal = runBlocking {
      spoonacularApiCaller.getMealsFromImage(BitmapFactory.decodeStream(inputStream))
    }

    // Assert
    assertEquals("cheesecake", actualMeal.name)

    // Observe the LiveData returned by getMealsFromImage and log the result in the observer

  }

  @Test
  fun testingImageToMealSecondAPIcallFails() {
    mockWebServer.dispatcher = halfFaultyDispacher

    // Arrange
    val inputStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")

    // Act
    val actualMeal = runBlocking {
      spoonacularApiCaller.getMealsFromImage(
          BitmapFactory.decodeStream(inputStream),
      )
    }

    // Assert
    val expected = Meal.default().deepCopy(id = actualMeal.id)
    assertEquals(expected, actualMeal)
  }

  @Test
  fun testingImageToMealAllAPIcallFails() {
    mockWebServer.dispatcher = faultyDispatcher

    // Arrange
    val inputStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")

    // Act
    val actualMeal = runBlocking {
      spoonacularApiCaller.getMealsFromImage(
          BitmapFactory.decodeStream(inputStream),
      )
    }

    // Assert
    val expected = Meal.default().deepCopy(id = actualMeal.id)
    assertEquals(expected, actualMeal)
  }

  @Test
  fun testRecipeByIngredients() {
    val response = spoonacularApiCaller.recipeByIngredients(listOf("apple", "banana")).recipes

    assert(response.size == 5)
    assert(response[0].title == "Apple Banana Smoothie")
    assert(response[0].imageUrl.toString() == "https://spoonacular.com/recipeImages/1-312x231.jpg")
    assert(response[0].usedIngredients == 2L)
    assert(response[0].missingIngredients == 1L)
    assert(response[0].likes == 0L)
  }

  @Test
  fun recipeFromIngredients() = runBlocking {
    val ingredients = listOf("apple", "banana")

    val response = spoonacularApiCaller.recipeByIngredients(ingredients)

    assertEquals(APIResponse.SUCCESS, response.status)
    assertEquals(5, response.recipes.size)
    assertEquals(1, response.recipes[0].id)
    assertEquals("Apple Banana Smoothie", response.recipes[0].title)
  }

  @Test
  fun recipeFromIngredientsFailure() {
    mockWebServer.dispatcher = faultyDispatcher
    val ingredients = listOf("apple", "banana")
    val response = spoonacularApiCaller.recipeByIngredients(ingredients)

    assertEquals(response.status, APIResponse.FAILURE)
  }

  @Test
  fun recipeStepFromId() {
    val response = spoonacularApiCaller.getRecipeSteps(1)

    assertEquals("cheese", response.name)
    assertEquals(2, response.steps.size)
    assertEquals(1, response.steps[0].number)
  }

  @Test
  fun recipeStepFromIdFailure() {
    mockWebServer.dispatcher = faultyDispatcher

    val response = spoonacularApiCaller.getRecipeSteps(1)

    assertEquals(response, RecipeInstruction.failure())
  }

  @Test
  fun getCompleteRecipesFromIngredients() {
    val recipe = spoonacularApiCaller.getCompleteRecipesFromIngredients(listOf("apple", "banana"))

    assert(recipe.isNotEmpty())
    assert(recipe.first().recipeInformation.ingredients.isNotEmpty())
    assert(recipe.first().recipeInformation.instructions.isNotEmpty())
  }

  @Test
  fun getCompleteRecipesFromIngredientsFaillure() {
    mockWebServer.dispatcher = faultyDispatcher

    val response = spoonacularApiCaller.getCompleteRecipesFromIngredients(listOf("apple", "banana"))

    assert(response.first().recipeInformation.ingredients.isEmpty())
    assert(response.first().recipeInformation.instructions.isEmpty())
  }

  @After
  fun tearDown() {
    mockWebServer.shutdown()
  }
}
