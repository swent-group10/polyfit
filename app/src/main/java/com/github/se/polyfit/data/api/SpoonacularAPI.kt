package com.github.se.polyfit.data.api

import android.util.Log
import com.github.se.polyfit.BuildConfig
import java.io.File
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject

object SpoonacularApiCaller {
  private val client = OkHttpClient()
  private val API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
  private val IMAGE_ANALYSIS_ENDPOINT = "$API_URL/food/images/analyze"
  private val RECIPE_NUTRITION_ENDPOINT = "$API_URL/recipes/%d/nutritionWidget.json"

  fun imageAnalysis(filePath: String): ImageAnalysisResponseAPI {
    val file = File(filePath)
    return imageAnalysis(file)
  }

  /**
   * Analyze a food image. The API tries to classify the image, guess the nutrition, and find a
   * matching recipes.
   *
   * @param file The image file to classify
   * @return The response from the API
   */
  fun imageAnalysis(file: File): ImageAnalysisResponseAPI {
    val mediaType = "multipart/form-data".toMediaTypeOrNull()
    val requestBody = file.asRequestBody(mediaType)
    val body =
        MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, requestBody)
            .build()

    val request =
        Request.Builder()
            .url(IMAGE_ANALYSIS_ENDPOINT)
            .post(body)
            .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
            .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
            .addHeader("X-RapidAPI-Host", BuildConfig.X_RapidAPI_Host)
            .build()

    return try {
      val response = client.newCall(request).execute()

      if (!response.isSuccessful) {
        Log.e("SpoonacularApiCaller", "Error during image analysis: ${response.code}")
        throw Exception("Error during image analysis: ${response.code}")
      }
      val responseBody = response.body?.string() ?: ""
      val jsonObject = JSONObject(responseBody)
      ImageAnalysisResponseAPI.fromJsonObject(jsonObject)
    } catch (e: IOException) {
      Log.e("SpoonacularApiCaller", "Error during image analysis", e)
      throw Exception("Error during image analysis : ", e)
    }
  }

  /**
   * Get the nutritional information for a recipe.
   *
   * @param recipeId The ID of the recipe
   * @return The response from the API
   */
  fun getRecipeNutrition(recipeId: Int): RecipeNutritionResponseAPI {
    val request =
        Request.Builder()
            .url(RECIPE_NUTRITION_ENDPOINT.format(recipeId))
            .get()
            .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
            .addHeader("X-RapidAPI-Host", BuildConfig.X_RapidAPI_Host)
            .build()

    return try {
      val response = client.newCall(request).execute()

      val requestStatus = response.code

      if (requestStatus != 200) {
        Log.e("SpoonacularApiCaller", "Error getting recipe nutrition: $requestStatus")
        throw Exception("Error getting recipe nutrition: $requestStatus")
      }

      val responseBody = response.body?.string() ?: ""
      val jsonObject = JSONObject(responseBody)
      RecipeNutritionResponseAPI.fromJsonObject(jsonObject)
    } catch (e: IOException) {
      Log.e("SpoonacularApiCaller", "Error getting recipe nutrition", e)
      throw Exception("Error getting recipe nutrition : ", e)
    }
  }
}
