package com.github.se.polyfit.viewmodel.meal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel
@Inject
constructor(
    private val mealDao: MealDao,
    private val spoonacularApiCaller: SpoonacularApiCaller,
    private val user: User,
    private val localDataProcessor: LocalDataProcessor
) : ViewModel() {

  fun storeMeal(imageBitmap: Bitmap?): String? {
    if (imageBitmap == null) {
      Log.e("OverviewViewModel", "Image is null")
      return null
    }

    val meal = spoonacularApiCaller.getMealsFromImage(imageBitmap)
    meal.userId = user.id
    return mealDao.insert(meal)
  }

  fun deleteById(mealDatabaseId: String) {
    mealDao.deleteById(mealDatabaseId)
  }

  fun getMealsByCalorieRange(minCalories: Double, maxCalories: Double): List<Meal> {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals(user.id)

    // Filter meals based on their calorie content
    val filteredMeals =
        allMeals.filter { meal ->
          val calories =
              meal.nutritionalInformation.nutrients.find { it.nutrientType == "calories" }?.amount
          calories != null && calories >= minCalories && calories <= maxCalories
        }

    // Log the result
    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found in the specified calorie range")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals in the specified calorie range")
    }

    // Return the filtered meals
    return filteredMeals
  }

  fun getMealsByName(name: String): List<Meal> {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals(user.id)

    // Filter meals based on their name
    val filteredMeals = allMeals.filter { meal -> meal.name.contains(name, ignoreCase = true) }

    // Log the result
    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found with the specified name")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals with the specified name")
    }

    // Return the filtered meals
    return filteredMeals
  }

  fun getMealsByOccasion(occasion: MealOccasion): List<Meal> {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals(user.id)

    // Filter meals based on their occasion
    val filteredMeals = allMeals.filter { meal -> meal.occasion == occasion }

    // Log the result
    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found for the specified occasion")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals for the specified occasion")
    }

    // Return the filtered meals
    return filteredMeals
  }

  fun getMealsByMultipleCriteria(
      name: String,
      minCalories: Double,
      maxCalories: Double,
      occasion: MealOccasion
  ): List<Meal> {
    val allMeals = mealDao.getAllMeals(user.id)

    val filteredMeals =
        allMeals.filter { meal ->
          meal.nutritionalInformation.nutrients
              .find { it.nutrientType == "calories" }
              ?.amount
              ?.let { calories -> calories in minCalories..maxCalories } ?: false
        }

    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found matching all criteria")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals matching all criteria")
    }

    return filteredMeals
  }

  fun getMealWithHighestCalories(): Meal? {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals(user.id)

    // Find the meal with the highest calories
    val highestCalorieMeal =
        allMeals.maxByOrNull { meal ->
          meal.nutritionalInformation.nutrients.find { it.nutrientType == "calories" }?.amount
              ?: 0.0
        }

    // Log the result
    if (highestCalorieMeal == null) {
      Log.e("OverviewViewModel", "No meals found in the database")
    } else {
      Log.i("OverviewViewModel", "Meal with the highest calories found: ${highestCalorieMeal.name}")
    }

    // Return the meal with the highest calories
    return highestCalorieMeal
  }

  fun getMealWithLowestCalories(): Meal? {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals(user.id)

    // Find the meal with the lowest calories
    val lowestCalorieMeal =
        allMeals.minByOrNull { meal ->
          meal.nutritionalInformation.nutrients.find { it.nutrientType == "calories" }?.amount
              ?: Double.MAX_VALUE
        }

    // Log the result
    if (lowestCalorieMeal == null) {
      Log.e("OverviewViewModel", "No meals found in the database")
    } else {
      Log.i("OverviewViewModel", "Meal with the lowest calories found: ${lowestCalorieMeal.name}")
    }

    // Return the meal with the lowest calories
    return lowestCalorieMeal
  }

  fun getUserName(): String {
    return when {
      user.displayName != null -> user.displayName!!
      else -> user.email
    }
  }

  fun launchCamera(
      context: Context,
      requestPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
      callback: () -> Unit
  ) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED) {
      try {
        callback()
      } catch (e: Exception) {
        Log.e("HomeScreen", "Error launching camera intent: $e")
      }
    } else {
      requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }

  fun getCaloriesPerMealOccasionTodayLiveData(): LiveData<List<Pair<MealOccasion, Double>>> {

    return localDataProcessor.getCaloriesPerMealOccasionTodayLiveData()
  }

  fun getCaloryGoal(): Long {
    return user.calorieGoal
  }

  /**
   * Converts a URI to a Bitmap.
   *
   * @param context The context of the application
   * @param uri The URI to convert stored locally on the device
   * @return The Bitmap if the conversion was successful, null otherwise
   */
  fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    // Open an InputStream from the URI
    val inputStream = context.contentResolver.openInputStream(uri)

    // Use the InputStream if it's not null
    inputStream?.use { stream ->
      // Create a ByteArrayOutputStream and copy the InputStream into it
      val outputStream = ByteArrayOutputStream().apply { stream.copyTo(this) }

      // Convert the ByteArrayOutputStream to a byte array
      val byteArray = outputStream.toByteArray()

      // Decode the byte array into a Bitmap
      return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // If the InputStream was null, return null
    return null
  }

  /**
   * Handles the selected image by converting it to a Bitmap. It handles calling the spoonacular API
   * to get the meal, and storing the meal in the database.
   *
   * @param context The context of the application
   * @param uri The URI of the selected image
   * @param overviewViewModel The OverviewViewModel instance
   * @return The ID of the stored meal in the local database if the image was successfully stored,
   *   null otherwise
   */
  fun handleSelectedImage(
      context: Context,
      uri: Uri?,
      overviewViewModel: OverviewViewModel,
  ): String? {
    return if (uri != null) {
      val imageBitmap = uriToBitmap(context, uri)

      overviewViewModel.storeMeal(imageBitmap)
    } else {
      null
    }
  }
}
