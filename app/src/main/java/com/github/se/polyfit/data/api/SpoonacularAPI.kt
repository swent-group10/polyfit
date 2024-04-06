package com.github.se.polyfit.data.api

import com.github.se.polyfit.BuildConfig
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject

object SpoonacularApiCaller {
  private val client = OkHttpClient()

  fun searchRecipe(recipe: String): String {
    val request =
        Request.Builder()
            .url(
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=${recipe}")
            .get()
            .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
            .addHeader("X-RapidAPI-Host", BuildConfig.X_RapidAPI_Host)
            .build()

    val response = client.newCall(request).execute()
    return response.body?.string() ?: ""
  }

  fun imageAnalysis(imagePath: String): ImageAnalysisResponse {
    val file = File(imagePath)
    return imageAnalysis(file)
  }

  /**
   * Analyze a food image. The API tries to classify the image, guess the nutrition, and find a
   * matching recipes.
   *
   * @param file The image file to classify
   * @return The response from the API
   *
   * Example of a response :
   * {"status":"success","nutrition":{"recipesUsed":25,"calories":{"value":391.0,"unit":"calories",
   * "confidenceRange95Percent":{"min":339.96,"max":454.49},"standardDeviation":146.08},
   * "fat":{"value":12.0,"unit":"g","confidenceRange95Percent":{"min":8.95,"max":15.7},
   * "standardDeviation":8.62},"protein":{"value":13.0,"unit":"g",
   * "confidenceRange95Percent":{"min":11.25,"max":15.51},"standardDeviation":5.43},
   * "carbs":{"value":51.0,"unit":"g","confidenceRange95Percent":{"min":48.7,"max":70.88},
   * "standardDeviation":28.29}},
   * "category":{"name":"baked_beans","probability":0.18275378138156873},
   * "recipes":[{"id":532358,"title":"The Best BBQ Baked Beans and 5 More Baked Beans to
   * Love","imageType":"jpg", "url":"http://www.foodiecrush.com/2014/06/the-best-bbq-baked-beans/"},
   * {"id":603179,"title":"green beans with fermented black beans ~ aka dirty green beans",
   * "imageType":"jpg","url":"http://www.healthyseasonalrecipes.com/green-beans-with-fermented-black-beans-dirty/"},
   * {"id":92007,"title":"Philippines Monggo Beans and Pechay (Mung Beans and Bok Choi)",
   * "imageType":"jpg","url":"http://www.food.com/recipe/philippines-monggo-beans-and-pechay-mung-beans-and-bok-choi-305953"},
   * {"id":112454,"title":"Caramelized Onions & Fava Beans (Broad Beans)",
   * "imageType":"jpg","url":"http://www.food.com/recipe/caramelized-onions-fava-beans-broad-beans-95854"},
   * {"id":112611,"title":"French String Beans/ Green Beans, Tomato & Basil
   * Salad","imageType":"jpg",
   * "url":"http://www.food.com/recipe/french-string-beans-green-beans-tomato-basil-salad-105135"},
   * {"id":112928,"title":"Glazed Chinese Long Beans(Or Green Beans)","imageType":"png",
   * "url":"http://chinese.food.com/recipe/glazed-chinese-long-beans-or-green-beans-385445"},
   * {"id":112995,"title":"Mean Beans (Pork and Beans)","imageType":"jpg",
   * "url":"http://www.food.com/recipe/mean-beans-pork-and-beans-369827"},
   * {"id":114158,"title":"Puerto Rican Rice and Beans (Pink Beans)",
   * "imageType":"jpg","url":"http://www.food.com/recipe/puerto-rican-rice-and-beans-pink-beans-147650"},
   * {"id":202046,"title":"Farro Salad with Squid, White Beans, and Green Beans","imageType":"jpg",
   * "url":"http://www.seriouseats.com/recipes/2013/03/farro-salad-squid-white-beans-green-beans-recipe.html"}]}
   */
  fun imageAnalysis(file: File): ImageAnalysisResponse {
    val mediaType = "multipart/form-data".toMediaTypeOrNull()
    val requestBody = file.asRequestBody(mediaType)
    val body =
        MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, requestBody)
            .build()

    val request =
        Request.Builder()
            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/images/analyze")
            .post(body)
            .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
            .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
            .addHeader("X-RapidAPI-Host", BuildConfig.X_RapidAPI_Host)
            .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string() ?: ""
    val jsonObject = JSONObject(responseBody)

    val nutritionObject = jsonObject.getJSONObject("nutrition")
    // Iterate over all elements
    val nutrients = mutableListOf<Nutrient>()
    for (key in nutritionObject.keys()) {
      val nutrientObject = nutritionObject.getJSONObject(key)
      val value = nutrientObject.getDouble("value")
      val unit = nutrientObject.getString("unit")
      val standardDeviation = nutrientObject.getDouble("standardDeviation")
      nutrients.add(
          Nutrient(
              nutrientType = key,
              amount = value,
              unit = MeasurementUnit.fromString(unit),
          ))
    }

    val category = jsonObject.getJSONObject("category").getString("name")

    val recipesArray = jsonObject.getJSONArray("recipes")
    val recipes = mutableListOf<Int>()
    for (i in 0 until recipesArray.length()) {
      recipes.add(recipesArray.getJSONObject(i).getInt("id"))
    }

    return ImageAnalysisResponse(
        status = jsonObject.getString("status"),
        nutrition = nutrients,
        category = category,
        recipes = recipes)
  }

  fun imageClassification(imagePath: String): ImageClassificationResponse {
    val file = File(imagePath)
    return imageClassification(file)
  }

  /**
   * 2024-04-06 23:40:22.153 21050-21077 Spoonacula...CallerTest com.github.se.polyfit Classify a
   * food image. The API tries to classify the image and guess the food category.
   *
   * @param file The image file to classify
   * @return The response from the API
   *
   * Example of a response :
   * {"status":"success","category":"cheesecake","probability":0.9826896137682608}
   */
  fun imageClassification(file: File): ImageClassificationResponse {
    val mediaType = "multipart/form-data".toMediaTypeOrNull()
    val requestBody = file.asRequestBody(mediaType)
    val body =
        MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, requestBody)
            .build()

    val request =
        Request.Builder()
            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/images/classify")
            .post(body)
            .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
            .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
            .addHeader("X-RapidAPI-Host", BuildConfig.X_RapidAPI_Host)
            .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string() ?: ""
    val jsonObject = JSONObject(responseBody)
    return ImageClassificationResponse(
        status = jsonObject.getString("status"),
        category = jsonObject.getString("category"),
        probability = jsonObject.getDouble("probability"))
  }
}

data class ImageClassificationResponse(
    val status: String,
    val category: String,
    val probability: Double
)

data class ImageAnalysisResponse(
    val status: String,
    val nutrition: List<Nutrient>,
    val category: String,
    val recipes: List<Int>
)
