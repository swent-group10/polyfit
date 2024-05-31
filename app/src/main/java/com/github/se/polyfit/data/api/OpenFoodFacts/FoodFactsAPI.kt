package com.github.se.polyfit.data.api.OpenFoodFacts

import android.util.Log
import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Data class that represents the response from the OpenFoodFacts API.
 *
 * @property productResponse The product information from the API
 * @property status The status of the API call
 */
class OpenFoodFactsApi {
  private val client = OkHttpClient()
  private var BASE_URL = "https://world.openfoodfacts.net/api/v2/product/"
  private val TAG = "OpenFoodFactsAPICaller"
  private val URL_FIELDS = "?fields=product_name,nutriments,quantity"

  // Needed for testing to reroute the API calls to a local server
  fun setBaseUrl(url: String) {
    BASE_URL = url
  }

  /**
   * Get the product information from the OpenFoodFacts API given a the barcode of the product.
   *
   * @param barcode The barcode of the product
   * @return The product information from the API
   */
  fun getFoodFacts(barcode: String = "3017624010701"): ProductResponseAPI? {
    val request = Request.Builder().url(BASE_URL + barcode + URL_FIELDS).build()

    return try {
      val response = client.newCall(request).execute()
      if (!response.isSuccessful) {
        Log.e(TAG, "Error in getting product facts: ${response.code}")
        ProductResponseAPI(null, APIResponse.FAILURE)
      } else {
        response.body?.string()?.let {
          ProductResponseAPI(ProductResponseAPI.fromJson(it), APIResponse.SUCCESS)
        } ?: ProductResponseAPI(null, APIResponse.FAILURE)
      }
    } catch (e: IOException) {
      Log.e(TAG, e.stackTraceToString())
      ProductResponseAPI(null, APIResponse.FAILURE)
    }
  }

  /**
   * Get the ingredient information from the OpenFoodFacts API given a the barcode of the product.
   * Handles the conversion of the API response to an Ingredient object.
   *
   * @param barcode The barcode of the product
   * @return The ingredient information from the API. If the API call fails, returns a default
   *   Ingredient object.
   */
  fun getIngredient(barcode: String): Ingredient {
    var ingredient = Ingredient.default()

    try {

      val apiResponse = getFoodFacts(barcode)
      if (apiResponse!!.status == APIResponse.SUCCESS) {
        val productInfo = apiResponse.productResponse
        val newIngredient =
            Ingredient(
                name = productInfo?.product!!.product_name,
                amount = productInfo.product.quantity?.toDoubleOrNull() ?: 0.0,
                id = 0,
                unit = MeasurementUnit.G,
                nutritionalInformation = productInfo.product.nutriments.getNutrientList())
        ingredient = newIngredient
      }
    } catch (e: IOException) {
      Log.e(TAG, "Unable to create Ingredient from information")
      Ingredient.default()
    }

    return ingredient
  }
}
