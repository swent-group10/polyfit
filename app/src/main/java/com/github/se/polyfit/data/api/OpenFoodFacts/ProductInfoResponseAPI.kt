package com.github.se.polyfit.data.api.OpenFoodFacts

import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/** Data class that represents the response from the OpenFoodFacts API. */
class ProductResponseAPI(val productResponse: ProductResponse?, val status: APIResponse) {
  companion object {
    val moshi: Moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    val adapter = moshi.adapter(ProductResponse::class.java).lenient()

    /**
     * Parse a JSON string into a ProductResponseAPI object.
     *
     * @param jsonString The JSON string to parse
     * @return The ProductResponseAPI object
     * @throws IllegalArgumentException If the JSON data is invalid
     */
    fun fromJson(jsonString: String): ProductResponse {
      return adapter.fromJson(jsonString) ?: throw IllegalArgumentException("Invalid JSON data")
    }
  }
}

@JsonClass(generateAdapter = true) data class ProductResponse(val product: Product)

@JsonClass(generateAdapter = true)
data class Product(val product_name: String, val nutriments: Nutriments, val quantity: String?)

@JsonClass(generateAdapter = true)
data class Nutriments(
    val fat: Double?,
    val carbohydrates: Double?,
    val sugars_value: Double?,
    val proteins: Double?
) {
  fun getNutrientList(): NutritionalInformation {

    val fatNutrient = Nutrient("fat", fat ?: 0.0, MeasurementUnit.G)
    val carbohydratesNutrient = Nutrient("carbohydrates", carbohydrates ?: 0.0, MeasurementUnit.G)
    val sugarNutrient = Nutrient("sugar", sugars_value ?: 0.0, MeasurementUnit.G)
    val proteinsNutrient = Nutrient("protein", proteins ?: 0.0, MeasurementUnit.G)
    return NutritionalInformation(
        mutableListOf(fatNutrient, carbohydratesNutrient, sugarNutrient, proteinsNutrient))
  }
}
