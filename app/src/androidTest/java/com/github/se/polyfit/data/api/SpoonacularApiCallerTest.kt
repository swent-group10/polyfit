package com.github.se.polyfit.data.api

import android.graphics.BitmapFactory
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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
import org.json.JSONObject
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
    assertEquals("cheesecake", actualMeal!!.name)

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
    assertEquals(Meal.default(), actualMeal!!)
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
    assertEquals(Meal.default(), actualMeal!!)
  }

  @After
  fun tearDown() {
    mockWebServer.shutdown()
  }

  private val jsonImageAnalysis =
      JSONObject(
          """
            {
              "status": "success",
              "nutrition": {
                "recipesUsed": 25,
                "calories": {
                  "value": 293,
                  "unit": "calories",
                  "confidenceRange95Percent": {
                    "min": 307.52,
                    "max": 505.55
                  },
                  "standardDeviation": 252.58
                },
                "fat": {
                  "value": 17,
                  "unit": "g",
                  "confidenceRange95Percent": {
                    "min": 18.39,
                    "max": 31.03
                  },
                  "standardDeviation": 16.13
                },
                "protein": {
                  "value": 4,
                  "unit": "g",
                  "confidenceRange95Percent": {
                    "min": 4.62,
                    "max": 7.3
                  },
                  "standardDeviation": 3.41
                },
                "carbs": {
                  "value": 35,
                  "unit": "g",
                  "confidenceRange95Percent": {
                    "min": 31.6,
                    "max": 53.17
                  },
                  "standardDeviation": 27.51
                }
              },
              "category": {
                "name": "cheesecake",
                "probability": 0.9826896137682608
              },
              "recipes": [
                {
                  "id": 61867,
                  "title": "Abbey's Infamous Cheesecake Or Cinnamon-apple Cider Cheesecake",
                  "imageType": "jpg",
                  "url": "http://www.honeyandjam.com/2009/04/my-first-daring-bakers-challenge.html"
                },
                {
                  "id": 62063,
                  "title": "The Cheesecake Factory Original Cheesecake",
                  "imageType": "jpg",
                  "url": "http://www.myrecipes.com/recipe/cheesecake-factory-original-cheesecake-10000001875828/"
                },
                {
                  "id": 104410,
                  "title": "The Cheesecake Factory Pumpkin Cheesecake",
                  "imageType": "jpg",
                  "url": "http://www.food.com/recipe/the-cheesecake-factory-pumpkin-cheesecake-46780"
                },
                {
                  "id": 131042,
                  "title": "Cheesecake Factory Key Lime Cheesecake--My Version",
                  "imageType": "jpg",
                  "url": "http://www.food.com/recipe/cheesecake-factory-key-lime-cheesecake-my-version-342573"
                },
                {
                  "id": 486659,
                  "title": "eggless mango cheesecake | no bake mango cheesecake",
                  "imageType": "jpg",
                  "url": "http://www.vegrecipesofindia.com/eggless-mango-cheesecake-recipe/"
                },
                {
                  "id": 525594,
                  "title": "Easy Cheesecake – Blueberry Cheesecake Bars",
                  "imageType": "jpg",
                  "url": "http://simple-nourished-living.com/food-cooking/recipes/desserts/easy-blueberry-cheesecake-bars/"
                },
                {
                  "id": 539774,
                  "title": "Blueberry Cheesecake Ice Cream: 10 Simple Steps to Sugar & Gluten-Free Perfection: Part 3, Blueberry Cheesecake",
                  "imageType": "jpg",
                  "url": "http://simplysugarandglutenfree.com/blueberry-cheesecake-ice-cream-10-simple-steps-to-sugar-gluten-free-perfection-part-3-blueberry-cheesecake/"
                },
                {
                  "id": 543602,
                  "title": "Cookies and Cream Cheesecake Cupcakes {aka Oreo Cheesecake Cupcakes}",
                  "imageType": "jpg",
                  "url": "http://www.cookingclassy.com/2013/01/cookies-and-cream-cheesecake-cupcakes-aka-oreo-cheesecake-cupcakes/"
                },
                {
                  "id": 552762,
                  "title": "Copycat Cheesecake Factory Oreo Cheesecake",
                  "imageType": "jpg",
                  "url": "http://www.sumptuousspoonfuls.com/copycat-cheesecake-factory-oreo-cheesecake/"
                }
              ]
            }
        """)
  private val jsonRecipeNutrition =
      JSONObject(
          """
    {
    "calories":"899",
    "carbs":"111g",
    "fat":"45g",
    "protein":"11g",
    "bad":[
    {
        "amount":"899",
        "indented":false,
        "title":"Calories",
        "percentOfDailyNeeds":44.96
    },
    {
        "amount":"45g",
        "indented":false,
        "title":"Fat",
        "percentOfDailyNeeds":69.74
    },
    {
        "amount":"14g",
        "indented":true,
        "title":"Saturated Fat",
        "percentOfDailyNeeds":89.9
    },
    {
        "amount":"111g",
        "indented":false,
        "title":"Carbohydrates",
        "percentOfDailyNeeds":37.08
    },
    {
        "amount":"21g",
        "indented":true,
        "title":"Sugar",
        "percentOfDailyNeeds":24.42
    },
    {
        "amount":"1mg",
        "indented":false,
        "title":"Cholesterol",
        "percentOfDailyNeeds":0.63
    },
    {
        "amount":"800mg",
        "indented":false,
        "title":"Sodium",
        "percentOfDailyNeeds":34.81
    }
    ],
    "good":[
    {
        "amount":"11g",
        "indented":false,
        "title":"Protein",
        "percentOfDailyNeeds":23.28
    },
    {
        "amount":"0.94mg",
        "indented":false,
        "title":"Manganese",
        "percentOfDailyNeeds":46.87
    },
    {
        "amount":"0.5mg",
        "indented":false,
        "title":"Vitamin B1",
        "percentOfDailyNeeds":33.39
    },
    {
        "amount":"127µg",
        "indented":false,
        "title":"Folate",
        "percentOfDailyNeeds":31.79
    },
    {
        "amount":"29µg",
        "indented":false,
        "title":"Vitamin K",
        "percentOfDailyNeeds":27.62
    },
    {
        "amount":"4mg",
        "indented":false,
        "title":"Iron",
        "percentOfDailyNeeds":26.03
    },
    {
        "amount":"4mg",
        "indented":false,
        "title":"Vitamin B3",
        "percentOfDailyNeeds":24.69
    },
    {
        "amount":"5g",
        "indented":false,
        "title":"Fiber",
        "percentOfDailyNeeds":22.72
    },
    {
        "amount":"0.34mg",
        "indented":false,
        "title":"Vitamin B2",
        "percentOfDailyNeeds":20.09
    },
    {
        "amount":"11µg",
        "indented":false,
        "title":"Selenium",
        "percentOfDailyNeeds":16.64
    },
    {
        "amount":"134mg",
        "indented":false,
        "title":"Phosphorus",
        "percentOfDailyNeeds":13.42
    },
    {
        "amount":"305mg",
        "indented":false,
        "title":"Potassium",
        "percentOfDailyNeeds":8.74
    },
    {
        "amount":"32mg",
        "indented":false,
        "title":"Magnesium",
        "percentOfDailyNeeds":8.21
    },
    {
        "amount":"0.16mg",
        "indented":false,
        "title":"Copper",
        "percentOfDailyNeeds":7.89
    },
    {
        "amount":"0.77mg",
        "indented":false,
        "title":"Vitamin B5",
        "percentOfDailyNeeds":7.72
    },
    {
        "amount":"5mg",
        "indented":false,
        "title":"Vitamin C",
        "percentOfDailyNeeds":7.26
    },
    {
        "amount":"1mg",
        "indented":false,
        "title":"Vitamin E",
        "percentOfDailyNeeds":7.1
    },
    {
        "amount":"68mg",
        "indented":false,
        "title":"Calcium",
        "percentOfDailyNeeds":6.8
    },
    {
        "amount":"0.87mg",
        "indented":false,
        "title":"Zinc",
        "percentOfDailyNeeds":5.78
    },
    {
        "amount":"0.11mg",
        "indented":false,
        "title":"Vitamin B6",
        "percentOfDailyNeeds":5.51
    },
    {
        "amount":"77IU",
        "indented":false,
        "title":"Vitamin A",
        "percentOfDailyNeeds":1.55
    }
    ],
    "nutrients":[
    {
        "name":"Calories",
        "amount":899.16,
        "unit":"kcal",
        "percentOfDailyNeeds":44.96
    },
    {
        "name":"Fat",
        "amount":45.33,
        "unit":"g",
        "percentOfDailyNeeds":69.74
    },
    {
        "name":"Saturated Fat",
        "amount":14.38,
        "unit":"g",
        "percentOfDailyNeeds":89.9
    },
    {
        "name":"Carbohydrates",
        "amount":111.24,
        "unit":"g",
        "percentOfDailyNeeds":37.08
    },
    {
        "name":"Net Carbohydrates",
        "amount":105.56,
        "unit":"g",
        "percentOfDailyNeeds":38.39
    },
    {
        "name":"Sugar",
        "amount":21.98,
        "unit":"g",
        "percentOfDailyNeeds":24.42
    },
    {
        "name":"Cholesterol",
        "amount":1.88,
        "unit":"mg",
        "percentOfDailyNeeds":0.63
    },
    {
        "name":"Sodium",
        "amount":800.57,
        "unit":"mg",
        "percentOfDailyNeeds":34.81
    },
    {
        "name":"Protein",
        "amount":11.64,
        "unit":"g",
        "percentOfDailyNeeds":23.28
    },
    {
        "name":"Manganese",
        "amount":0.94,
        "unit":"mg",
        "percentOfDailyNeeds":46.87
    },
    {
        "name":"Vitamin B1",
        "amount":0.5,
        "unit":"mg",
        "percentOfDailyNeeds":33.39
    },
    {
        "name":"Folate",
        "amount":127.16,
        "unit":"µg",
        "percentOfDailyNeeds":31.79
    },
    {
        "name":"Vitamin K",
        "amount":29,
        "unit":"µg",
        "percentOfDailyNeeds":27.62
    },
    {
        "name":"Iron",
        "amount":4.69,
        "unit":"mg",
        "percentOfDailyNeeds":26.03
    },
    {
        "name":"Vitamin B3",
        "amount":4.94,
        "unit":"mg",
        "percentOfDailyNeeds":24.69
    },
    {
        "name":"Fiber",
        "amount":5.68,
        "unit":"g",
        "percentOfDailyNeeds":22.72
    },
    {
        "name":"Vitamin B2",
        "amount":0.34,
        "unit":"mg",
        "percentOfDailyNeeds":20.09
    },
    {
        "name":"Selenium",
        "amount":11.65,
        "unit":"µg",
        "percentOfDailyNeeds":16.64
    },
    {
        "name":"Phosphorus",
        "amount":134.18,
        "unit":"mg",
        "percentOfDailyNeeds":13.42
    },
    {
        "name":"Potassium",
        "amount":305.87,
        "unit":"mg",
        "percentOfDailyNeeds":8.74
    },
    {
        "name":"Magnesium",
        "amount":32.85,
        "unit":"mg",
        "percentOfDailyNeeds":8.21
    },
    {
        "name":"Copper",
        "amount":0.16,
        "unit":"mg",
        "percentOfDailyNeeds":7.89
    },
    {
        "name":"Vitamin B5",
        "amount":0.77,
        "unit":"mg",
        "percentOfDailyNeeds":7.72
    },
    {
        "name":"Vitamin C",
        "amount":5.99,
        "unit":"mg",
        "percentOfDailyNeeds":7.26
    },
    {
        "name":"Vitamin E",
        "amount":1.07,
        "unit":"mg",
        "percentOfDailyNeeds":7.1
    },
    {
        "name":"Calcium",
        "amount":68.02,
        "unit":"mg",
        "percentOfDailyNeeds":6.8
    },
    {
        "name":"Zinc",
        "amount":0.87,
        "unit":"mg",
        "percentOfDailyNeeds":5.78
    },
    {
        "name":"Vitamin B6",
        "amount":0.11,
        "unit":"mg",
        "percentOfDailyNeeds":5.51
    },
    {
        "name":"Vitamin A",
        "amount":77.6,
        "unit":"IU",
        "percentOfDailyNeeds":1.55
    }
    ],
    "properties":[
    {
        "name":"Glycemic Index",
        "amount":33.51,
        "unit":""
    },
    {
        "name":"Glycemic Load",
        "amount":15.63,
        "unit":""
    },
    {
        "name":"Nutrition Score",
        "amount":14.347391,
        "unit":"%"
    }
    ],
    "flavonoids":[
    {
        "name":"Cyanidin",
        "amount":2.35,
        "unit":"mg"
    },
    {
        "name":"Petunidin",
        "amount":8.75,
        "unit":"mg"
    },
    {
        "name":"Delphinidin",
        "amount":9.83,
        "unit":"mg"
    },
    {
        "name":"Malvidin",
        "amount":18.76,
        "unit":"mg"
    },
    {
        "name":"Pelargonidin",
        "amount":0,
        "unit":"mg"
    },
    {
        "name":"Peonidin",
        "amount":5.63,
        "unit":"mg"
    },
    {
        "name":"Catechin",
        "amount":2.3,
        "unit":"mg"
    },
    {
        "name":"Epigallocatechin",
        "amount":0.18,
        "unit":"mg"
    },
    {
        "name":"Epicatechin",
        "amount":0.37,
        "unit":"mg"
    },
    {
        "name":"Epicatechin 3-gallate",
        "amount":0.23,
        "unit":"mg"
    },
    {
        "name":"Epigallocatechin 3-gallate",
        "amount":0,
        "unit":"mg"
    },
    {
        "name":"Theaflavin",
        "amount":0,
        "unit":""
    },
    {
        "name":"Thearubigins",
        "amount":0,
        "unit":""
    },
    {
        "name":"Eriodictyol",
        "amount":0.03,
        "unit":"mg"
    },
    {
        "name":"Hesperetin",
        "amount":0.09,
        "unit":"mg"
    },
    {
        "name":"Naringenin",
        "amount":0.01,
        "unit":"mg"
    },
    {
        "name":"Apigenin",
        "amount":0,
        "unit":"mg"
    },
    {
        "name":"Luteolin",
        "amount":0.06,
        "unit":"mg"
    },
    {
        "name":"Isorhamnetin",
        "amount":0,
        "unit":""
    },
    {
        "name":"Kaempferol",
        "amount":0.46,
        "unit":"mg"
    },
    {
        "name":"Myricetin",
        "amount":0.36,
        "unit":"mg"
    },
    {
        "name":"Quercetin",
        "amount":2.13,
        "unit":"mg"
    },
    {
        "name":"Theaflavin-3,3'-digallate",
        "amount":0,
        "unit":""
    },
    {
        "name":"Theaflavin-3'-gallate",
        "amount":0,
        "unit":""
    },
    {
        "name":"Theaflavin-3-gallate",
        "amount":0,
        "unit":""
    },
    {
        "name":"Gallocatechin",
        "amount":0.03,
        "unit":"mg"
    }
    ],
    "ingredients":[
    {
        "name":"blueberries",
        "amount":0.19,
        "unit":"cups",
        "id":9050,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0.12,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0.08,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0.03,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.09,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":14.98,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":2.76,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":3.33,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":21.37,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":5.36,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0.21,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":1.66,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":3.36,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.04,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":1.66,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":1.66,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":1.66,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":4.02,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":15.82,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0.67,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.16,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":2.69,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.09,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0.04,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0.28,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"egg white",
        "amount":0.13,
        "unit":"",
        "id":1124,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0.75,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":0.56,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":6.11,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0.41,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0.41,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":0.26,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0.15,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0.04,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":1.95,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":6.22,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"flour",
        "amount":0.25,
        "unit":"tablespoons",
        "id":20081,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0.11,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0.09,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0.64,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.02,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":2.03,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":2.01,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0.01,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0.19,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0.41,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":1.38,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":0.28,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":3.43,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":2.89,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":1.43,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":6.82,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0.05,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0.04,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"granulated sugar",
        "amount":0.09,
        "unit":"cup",
        "id":19335,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0.11,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.06,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":18.71,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":0.38,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":18.67,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Vitamin B2",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Calcium",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":18.67,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":72.19,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Fluoride",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"lemon juice",
        "amount":0.13,
        "unit":"teaspoon",
        "id":9152,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0.04,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0.02,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":0.05,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":0.64,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Trans Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":286.82
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0.04,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":0.04,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":0.04,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0.13,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":0.04,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":0.14,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0.24,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"nutmeg",
        "amount":0.13,
        "unit":"pinch",
        "id":2025,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.05,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0.13,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0.04,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":0.27,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":0.44,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0.23,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":0.04,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":0.23,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0.09,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":0.06,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":0.66,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"pie dough round",
        "amount":0.25,
        "unit":"",
        "id":18334,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":5.17,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":1.15,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":1.11,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Selenium",
            "amount":2.42,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":11.08,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0.43,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B5",
            "amount":0.17,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Potassium",
            "amount":41.22,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Phosphorus",
            "amount":30.6,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":3.1,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":2.62,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":6.38,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":19.6,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":8.07,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.07,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":29.75,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.12,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Saturated Fat",
            "amount":3.47,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":19.55,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":20.66,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":194.23,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":1.06,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.2,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":1.35,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":173.82,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"pie dough round",
        "amount":0.25,
        "unit":"",
        "id":18334,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":5.17,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":1.15,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":1.11,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Selenium",
            "amount":2.42,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":11.08,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0.43,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B5",
            "amount":0.17,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Potassium",
            "amount":41.22,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Phosphorus",
            "amount":30.6,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":3.1,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":2.62,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":6.38,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":19.6,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":8.07,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.07,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":29.75,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.12,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Saturated Fat",
            "amount":3.47,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":19.55,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":20.66,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":194.23,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":1.06,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.2,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":1.35,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":173.82,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"pie dough round",
        "amount":0.25,
        "unit":"",
        "id":18334,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":5.17,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":1.15,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":1.11,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Selenium",
            "amount":2.42,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":11.08,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0.43,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B5",
            "amount":0.17,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Potassium",
            "amount":41.22,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Phosphorus",
            "amount":30.6,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":3.1,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":2.62,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":6.38,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":19.6,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":8.07,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.07,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":29.75,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.12,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Saturated Fat",
            "amount":3.47,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":19.55,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":20.66,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":194.23,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":1.06,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.2,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":1.35,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":173.82,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"pie dough round",
        "amount":0.25,
        "unit":"",
        "id":18334,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":5.17,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":1.15,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":1.11,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Selenium",
            "amount":2.42,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":11.08,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0.43,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B5",
            "amount":0.17,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Potassium",
            "amount":41.22,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Phosphorus",
            "amount":30.6,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":3.1,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":2.62,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":6.38,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":19.6,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":8.07,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.07,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":29.75,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.12,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Saturated Fat",
            "amount":3.47,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":19.55,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":20.66,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":194.23,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":1.06,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.2,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.19,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":1.35,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":173.82,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"quick cooking tapioca",
        "amount":0.25,
        "unit":"tablespoons",
        "id":93660,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Selenium",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sugar",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Phosphorus",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Trans Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":286.82
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":2.59,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Saturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Carbohydrates",
            "amount":2.59,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":10.41,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Fluoride",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"rhubarb",
        "amount":0.31,
        "unit":"cups",
        "id":9307,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0.11,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0.08,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0.03,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0.42,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.08,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":38.89,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0.42,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":5.34,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":109.8,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":11.17,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0.34,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":4.57,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":1.04,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Zinc",
            "amount":0.04,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Calcium",
            "amount":32.79,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Vitamin B2",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":2.67,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":2.33,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0.02,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":1.73,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":8.01,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0.69,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.1,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":3.05,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Manganese",
            "amount":0.07,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0.04,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":1.52,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"salt",
        "amount":0.04,
        "unit":"teaspoon",
        "id":2047,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":0,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Cholesterol",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Vitamin B2",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Calcium",
            "amount":0.06,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":0,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Fluoride",
            "amount":0.01,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":96.9,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    },
    {
        "name":"unsalted butter",
        "amount":0.06,
        "unit":"tablespoon",
        "id":1145,
        "nutrients":[
        {
            "name":"Mono Unsaturated Fat",
            "amount":0.18,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Vitamin B3",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":24.69
        },
        {
            "name":"Iron",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":26.03
        },
        {
            "name":"Vitamin B5",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.72
        },
        {
            "name":"Caffeine",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Selenium",
            "amount":0.01,
            "unit":"µg",
            "percentOfDailyNeeds":16.64
        },
        {
            "name":"Fat",
            "amount":0.71,
            "unit":"g",
            "percentOfDailyNeeds":69.74
        },
        {
            "name":"Vitamin A",
            "amount":21.87,
            "unit":"IU",
            "percentOfDailyNeeds":1.55
        },
        {
            "name":"Sugar",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":24.42
        },
        {
            "name":"Lycopene",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Phosphorus",
            "amount":0.21,
            "unit":"mg",
            "percentOfDailyNeeds":13.42
        },
        {
            "name":"Potassium",
            "amount":0.21,
            "unit":"mg",
            "percentOfDailyNeeds":8.74
        },
        {
            "name":"Vitamin D",
            "amount":0.01,
            "unit":"µg",
            "percentOfDailyNeeds":0.09
        },
        {
            "name":"Trans Fat",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":286.82
        },
        {
            "name":"Cholesterol",
            "amount":1.88,
            "unit":"mg",
            "percentOfDailyNeeds":0.63
        },
        {
            "name":"Vitamin K",
            "amount":0.06,
            "unit":"µg",
            "percentOfDailyNeeds":27.62
        },
        {
            "name":"Protein",
            "amount":0.01,
            "unit":"g",
            "percentOfDailyNeeds":23.28
        },
        {
            "name":"Magnesium",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":8.21
        },
        {
            "name":"Net Carbohydrates",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":38.39
        },
        {
            "name":"Vitamin B2",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":20.09
        },
        {
            "name":"Calcium",
            "amount":0.21,
            "unit":"mg",
            "percentOfDailyNeeds":6.8
        },
        {
            "name":"Zinc",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.78
        },
        {
            "name":"Alcohol",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Folate",
            "amount":0.03,
            "unit":"µg",
            "percentOfDailyNeeds":31.79
        },
        {
            "name":"Copper",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.89
        },
        {
            "name":"Vitamin B1",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":33.39
        },
        {
            "name":"Choline",
            "amount":0.16,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Saturated Fat",
            "amount":0.45,
            "unit":"g",
            "percentOfDailyNeeds":89.9
        },
        {
            "name":"Folic Acid",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Carbohydrates",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":37.08
        },
        {
            "name":"Vitamin B12",
            "amount":0,
            "unit":"µg",
            "percentOfDailyNeeds":0.08
        },
        {
            "name":"Vitamin B6",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":5.51
        },
        {
            "name":"Calories",
            "amount":6.27,
            "unit":"kcal",
            "percentOfDailyNeeds":44.96
        },
        {
            "name":"Fiber",
            "amount":0,
            "unit":"g",
            "percentOfDailyNeeds":22.72
        },
        {
            "name":"Vitamin E",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":7.1
        },
        {
            "name":"Vitamin C",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":7.26
        },
        {
            "name":"Fluoride",
            "amount":0.02,
            "unit":"mg",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Manganese",
            "amount":0,
            "unit":"mg",
            "percentOfDailyNeeds":46.87
        },
        {
            "name":"Poly Unsaturated Fat",
            "amount":0.03,
            "unit":"g",
            "percentOfDailyNeeds":0
        },
        {
            "name":"Sodium",
            "amount":0.1,
            "unit":"mg",
            "percentOfDailyNeeds":34.81
        }
        ]
    }
    ],
    "caloricBreakdown":{
    "percentFat":45.35,
    "percentCarbs":49.47,
    "percentProtein":5.18
},
    "weightPerServing":{
    "amount":265,
    "unit":"g"
},
    "expires":1692095820085,
    "isStale":true
}
    """)
}
