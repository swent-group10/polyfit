package com.github.se.polyfit.data.api

import com.github.se.polyfit.BuildConfig
import com.github.se.polyfit.model.ingredient.Ingredient
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
   * Example response from the API:
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

  /**
   * Get the nutritional information for a recipe.
   *
   * @param recipeId The ID of the recipe
   *
   * Example reposne from API:
   * {"calories":"899","carbs":"111g","fat":"45g","protein":"11g","bad":[{"amount":"899",
   * "indented":false,"title":"Calories","percentOfDailyNeeds":44.96},{"amount":"45g",
   * "indented":false,"title":"Fat","percentOfDailyNeeds":69.74},{"amount":"14g","indented":true,
   * "title":"Saturated Fat","percentOfDailyNeeds":89.9},{"amount":"111g","indented":false,
   * "title":"Carbohydrates","percentOfDailyNeeds":37.08},
   * {"amount":"21g","indented":true,"title":"Sugar","percentOfDailyNeeds":24.42},
   * {"amount":"1mg","indented":false,"title":"Cholesterol","percentOfDailyNeeds":0.63},
   * {"amount":"800mg","indented":false,"title":"Sodium","percentOfDailyNeeds":34.81}],"good":[
   * {"amount":"11g","indented":false,"title":"Protein","percentOfDailyNeeds":23.28},
   * {"amount":"0.94mg","indented":false,"title":"Manganese","percentOfDailyNeeds":46.87},
   * {"amount":"0.5mg","indented":false,"title":"Vitamin B1","percentOfDailyNeeds":33.39},
   * {"amount":"127µg","indented":false,"title":"Folate","percentOfDailyNeeds":31.79},
   * {"amount":"29µg","indented":false,"title":"Vitamin K","percentOfDailyNeeds":27.62},
   * {"amount":"4mg","indented":false,"title":"Iron","percentOfDailyNeeds":26.03},
   * {"amount":"4mg","indented":false,"title":"Vitamin B3","percentOfDailyNeeds":24.69},
   * {"amount":"5g","indented":false,"title":"Fiber","percentOfDailyNeeds":22.72},
   * {"amount":"0.34mg","indented":false,"title":"Vitamin B2","percentOfDailyNeeds":20.09},
   * {"amount":"11µg","indented":false,"title":"Selenium","percentOfDailyNeeds":16.64},{"amount":"134mg","indented":false,"title":"Phosphorus","percentOfDailyNeeds":13.42},{"amount":"305mg","indented":false,"title":"Potassium","percentOfDailyNeeds":8.74},{"amount":"32mg","indented":false,"title":"Magnesium","percentOfDailyNeeds":8.21},{"amount":"0.16mg","indented":false,"title":"Copper","percentOfDailyNeeds":7.89},{"amount":"0.77mg","indented":false,"title":"Vitamin
   * B5","percentOfDailyNeeds":7.72},{"amount":"5mg","indented":false,"title":"Vitamin
   * C","percentOfDailyNeeds":7.26},{"amount":"1mg","indented":false,"title":"Vitamin
   * E","percentOfDailyNeeds":7.1},{"amount":"68mg","indented":false,"title":"Calcium","percentOfDailyNeeds":6.8},{"amount":"0.87mg","indented":false,"title":"Zinc","percentOfDailyNeeds":5.78},{"amount":"0.11mg","indented":false,"title":"Vitamin
   * B6","percentOfDailyNeeds":5.51},{"amount":"77IU","indented":false,"title":"Vitamin
   * A","percentOfDailyNeeds":1.55}],"nutrients":[{"name":"Calories","amount":899.16,"unit":"kcal","percentOfDailyNeeds":44.96},{"name":"Fat","amount":45.33,"unit":"g","percentOfDailyNeeds":69.74},{"name":"Saturated
   * Fat","amount":14.38,"unit":"g","percentOfDailyNeeds":89.9},{"name":"Carbohydrates","amount":111.24,"unit":"g","percentOfDailyNeeds":37.08},{"name":"Net
   * Carbohydrates","amount":105.56,"unit":"g","percentOfDailyNeeds":38.39},{"name":"Sugar","amount":21.98,"unit":"g","percentOfDailyNeeds":24.42},{"name":"Cholesterol","amount":1.88,"unit":"mg","percentOfDailyNeeds":0.63},{"name":"Sodium","amount":800.57,"unit":"mg","percentOfDailyNeeds":34.81},{"name":"Protein","amount":11.64,"unit":"g","percentOfDailyNeeds":23.28},{"name":"Manganese","amount":0.94,"unit":"mg","percentOfDailyNeeds":46.87},{"name":"Vitamin
   * B1","amount":0.5,"unit":"mg","percentOfDailyNeeds":33.39},{"name":"Folate","amount":127.16,"unit":"µg","percentOfDailyNeeds":31.79},{"name":"Vitamin
   * K","amount":29,"unit":"µg","percentOfDailyNeeds":27.62},{"name":"Iron","amount":4.69,"unit":"mg","percentOfDailyNeeds":26.03},{"name":"Vitamin
   * B3","amount":4.94,"unit":"mg","percentOfDailyNeeds":24.69},{"name":"Fiber","amount":5.68,"unit":"g","percentOfDailyNeeds":22.72},{"name":"Vitamin
   * B2","amount":0.34,"unit":"mg","percentOfDailyNeeds":20.09},{"name":"Selenium","amount":11.65,"unit":"µg","percentOfDailyNeeds":16.64},{"name":"Phosphorus","amount":134.18,"unit":"mg","percentOfDailyNeeds":13.42},{"name":"Potassium","amount":305.87,"unit":"mg","percentOfDailyNeeds":8.74},{"name":"Magnesium","amount":32.85,"unit":"mg","percentOfDailyNeeds":8.21},{"name":"Copper","amount":0.16,"unit":"mg"
   */

  //    fun getRecipeNutrition(recipeId: Int): RecipeNutrition {
  //        val request =
  //            Request.Builder()
  //                .url(
  //
  // "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/${recipeId}/nutritionWidget.json"
  //                )
  //                .get()
  //                .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
  //                .addHeader("X-RapidAPI-Host", BuildConfig.X_RapidAPI_Host)
  //                .build()
  //
  //        val response = client.newCall(request).execute()
  //        val responseBody = response.body?.string() ?: ""
  //        val jsonObject = JSONObject(responseBody)
  //
  //        val nutrientsJsonArray = jsonObject.getJSONArray("nutrients")
  //        val nutrients = mutableListOf<Nutrient>()
  //
  //        for (i in 0 until nutrientsJsonArray.length()) {
  //            val nutrientJsonObject = nutrientsJsonArray.getJSONObject(i)
  //            val nutrient = Nutrient(
  //                nutrientType = nutrientJsonObject.getString("name"),
  //                amount = nutrientJsonObject.getDouble("amount"),
  //                unit = MeasurementUnit.fromString(nutrientJsonObject.getString("unit")),
  //            )
  //            nutrients.add(nutrient)
  //        }
  //
  //        val ingredientsJsonArray = jsonObject.getJSONArray("ingredients")
  //
  //        val ingredients = mutableListOf<Ingredient>()
  //
  //        for (i in 0 until ingredientsJsonArray.length()) {
  //            val ingredientJsonObject = ingredientsJsonArray.getJSONObject(i)
  //            val nutritionalInformation = NutritionalInformation(mutableListOf())
  //
  //            val nutrientsJSONObject = ingredientJsonObject.getJSONObject("nutrients")
  //
  //            for (j in 0 until nutrientsJSONObject.length()) {
  //                val currNutrient = nutrientsJSONObject.get(j)
  //                val newNutrient = Nutrient()
  //
  //                nutritionalInformation.nutrients.add(newNutrient)
  //            }
  //
  //
  //            val newIngredient = Ingredient(
  //                name = ingredientJsonObject.getString("name"),
  //                id = ingredientJsonObject.getString("id").toInt(),
  //                amount = ingredientJsonObject.getString("amount").toDouble(),
  //                unit = MeasurementUnit.fromString(ingredientJsonObject.getString("unit")),
  //                nutritionalInformation = nutritionalInformation
  //            )
  //
  //            ingredients.add(newIngredient)
  //        }
  //
  //
  //        return RecipeNutrition(
  //            status = jsonObject.getString("status"),
  //            nutrients = nutrients,
  //            ingredients = ingredients
  //        )
  //
  //
  //    }
}

data class RecipeNutrition(
    val status: String,
    val nutrients: MutableList<Nutrient>,
    val ingredients: MutableList<Ingredient>
) {
  companion object {
    fun fromJsonObjtect() {}
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
