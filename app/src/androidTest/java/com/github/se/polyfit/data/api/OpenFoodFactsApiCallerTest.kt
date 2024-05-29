package com.github.se.polyfit.data.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.data.api.OpenFoodFacts.Nutriments
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.data.api.OpenFoodFacts.Product
import com.github.se.polyfit.data.api.OpenFoodFacts.ProductResponse
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.json.JSONObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OpenFoodFactsApiCallerTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var mockWebSever: MockWebServer
  private lateinit var foodFactsApi: OpenFoodFactsApi

  private val nutellaCode: String = "3017624010701"

  private val dispatcher =
      object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
          return when (request.path) {
            "/$nutellaCode?fields=product_name,nutriments,quantity" ->
                MockResponse().setResponseCode(200).setBody(nutellaResult.toString())
            else -> MockResponse().setResponseCode(402).setBody("Not Found")
          }
        }
      }

  private val emptyResultDispatcher =
      object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
          return when (request.path) {
            "/$nutellaCode?fields=product_name,nutriments,quantity" ->
                MockResponse().setResponseCode(200).setBody("")
            else -> MockResponse().setResponseCode(402).setBody("Not Found")
          }
        }
      }

  private val faultyDispatcher =
      object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
          return when (request.path) {
            "/$nutellaCode?fields=product_name,nutriments,quantity" ->
                MockResponse().setResponseCode(404).setBody("Error")
            else -> MockResponse().setResponseCode(402).setBody("Not Found")
          }
        }
      }

  private val _nutriments: Nutriments = Nutriments(230.0, 57.5, 56.3, 134.2)

  private val _product = Product("Nutella", _nutriments, "")

  private val _productResponse = ProductResponse(product = _product)

  @Before
  fun setup() {
    mockWebSever = MockWebServer()
    mockWebSever.dispatcher = dispatcher
    mockWebSever.start()

    foodFactsApi = OpenFoodFactsApi()
    foodFactsApi.setBaseUrl(mockWebSever.url("/").toString())
  }

  @Test
  fun fetchingNutellaInfoFromBarcode() {
    val response = foodFactsApi.getFoodFacts(nutellaCode)

    val productResponse = response!!.productResponse
    val product = productResponse?.product
    val nutriments = product!!.nutriments

    assert(response.status == APIResponse.SUCCESS)
    assert(product.product_name == "Nutella")
    assert(product.quantity == "")
    assert(nutriments.carbohydrates == 57.5)
    assert(nutriments.fat == 230.0)
    assert(nutriments.proteins == 134.2)
    assert(nutriments.sugars_value == 56.3)
  }

  @Test
  fun fetchingNutellaIngredient() {
    val ingredient = foodFactsApi.getIngredient(nutellaCode)

    assert(ingredient.id == 0L)
    assert(ingredient.name == "Nutella")
    assert(ingredient.unit == MeasurementUnit.G)
    assert(ingredient.amount == 0.0)
    assert(
        ingredient.nutritionalInformation.nutrients
            .first { it.nutrientType == "carbohydrates" }
            .amount == 57.5)
    assert(
        ingredient.nutritionalInformation.nutrients.first { it.nutrientType == "fat" }.amount ==
            230.0)
    assert(
        ingredient.nutritionalInformation.nutrients.first { it.nutrientType == "sugar" }.amount ==
            56.3)
    assert(
        ingredient.nutritionalInformation.nutrients.first { it.nutrientType == "protein" }.amount ==
            134.2)
  }

  @Test
  fun errorHandlingInNotSuccessfulFetching() {
    mockWebSever.dispatcher = faultyDispatcher

    val response = foodFactsApi.getFoodFacts(nutellaCode)
    assert(response!!.status == APIResponse.FAILURE)
    assert(response.productResponse == null)
  }

  @Test
  fun testingDataClasses() {

    val nutritionalInfo =
        NutritionalInformation(
            mutableListOf(
                Nutrient("fat", 230.0, MeasurementUnit.G),
                Nutrient("carbohydrates", 57.5, MeasurementUnit.G),
                Nutrient("sugar", 56.3, MeasurementUnit.G),
                Nutrient("protein", 134.2, MeasurementUnit.G)))

    val response = foodFactsApi.getFoodFacts(nutellaCode)
    assert(response!!.productResponse == _productResponse)
    assert(response.productResponse!!.product == _product)
    assert(response.productResponse!!.product.nutriments == _nutriments)

    assert(response.productResponse!!.product.nutriments.getNutrientList() == nutritionalInfo)
  }

  @Test
  fun apiReturnsEmpty() {
    mockWebSever.dispatcher = emptyResultDispatcher
    val response = foodFactsApi.getFoodFacts(nutellaCode)
    assert(response!!.status == APIResponse.FAILURE)
    assert(response.productResponse == null)
    assert(response.productResponse?.product?.product_name == null)
    assert(response.productResponse?.product?.quantity == null)
    assert(response.productResponse?.product?.nutriments == null)

    val p_nutriments = response.productResponse?.product?.nutriments

    assert(p_nutriments?.fat == null)
    assert(p_nutriments?.carbohydrates == null)
    assert(p_nutriments?.sugars_value == null)
    assert(p_nutriments?.proteins == null)
  }
}

private val nutellaResult =
    JSONObject(
        """
{
    "code": "3017624010701",
    "product": {
        "nutriments": {
            "carbohydrates": 57.5,
            "carbohydrates_100g": 57.5,
            "carbohydrates_unit": "g",
            "carbohydrates_value": 57.5,
            "energy": 2255,
            "energy-kcal": 539,
            "energy-kcal_100g": 539,
            "energy-kcal_unit": "kcal",
            "fat": 230,
            "proteins": 134.2,
            "sugars": 56.3,
            "sugars_100g": 56.3,
            "sugars_unit": "g",
            "sugars_value": 56.3
        },
        "quantity": "",
        "product_name": "Nutella"
    },
    "status": 1,
    "status_verbose": "product found"
}
        """)
