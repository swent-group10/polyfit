package com.github.se.polyfit.data.api

import android.util.Log
import co.yml.charts.common.extensions.roundTwoDecimal
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class OpenFoodFactsApi {
    private val client = OkHttpClient()
    private val BASE_URL = "https://world.openfoodfacts.net/api/v2/product/"
    private val TAG = "OpenFoodFactsAPICaller"
    private val URL_FIELDS = "?fields=product_name,nutriments,quantity"

    fun getFoodFacts(barcode: String): ProductResponseAPI? {
        val request = Request.Builder()
            .url(BASE_URL + barcode + URL_FIELDS)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.e(TAG, "Error in getting product facts: ${response.code}")
                ProductResponseAPI(null, APIResponse.FAILURE)
            } else {
                response.body?.string()?.let {
                    ProductResponseAPI(ProductResponseAPI.fromJson(it), APIResponse.SUCCESS)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    fun getIngredient(barcode : String) : Ingredient{
        var ingredient = Ingredient.default()

        try {

            val apiResponse = getFoodFacts(barcode)
            if(apiResponse!!.status == APIResponse.SUCCESS){
                val productInfo = apiResponse.productResponse
                val newIngredient =
                    Ingredient(
                        name = productInfo?.product!!.product_name,
                        amount = productInfo.product.quantity?.toDouble()!!.roundTwoDecimal(),
                        id = 0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation = productInfo.product.nutriments.getNutrientList()
                    )
                ingredient = newIngredient
            }
        }catch (e : IOException){
            Log.e(TAG, "Unable to create Ingredient from information")
            throw IOException(e)
        }

        return ingredient
    }
}
