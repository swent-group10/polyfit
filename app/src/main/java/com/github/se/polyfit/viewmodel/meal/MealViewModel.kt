package com.github.se.polyfit.viewmodel.meal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepo: MealFirebaseRepository) :
    ViewModel() {

  private val _meal: MutableLiveData<Meal> = MutableLiveData(Meal.default())
  val meal: LiveData<Meal> = _meal // Expose _meal as an immutable LiveData

  private val _isComplete: LiveData<Boolean> = _meal.map { it?.isComplete() ?: false }

  val isComplete: LiveData<Boolean> = _isComplete

  fun setMealData(meal: Meal) {
    _meal.value = meal
  }

  fun setMealName(name: String) {
    _meal.value!!.name = name
  }

  fun setMealCreatedAt(createdAt: LocalDate) {
    _meal.value = _meal.value?.copy(createdAt = createdAt)
  }

  fun setMealOccasion(occasion: MealOccasion) {
    _meal.value = _meal.value?.copy(occasion = occasion)
  }

  fun setMeal() {
    if (!_meal.value!!.isComplete()) {
      throw Exception("Meal is incomplete")
    }

    try {
      mealRepo.storeMeal(_meal.value!!).continueWith {
        if (it.isSuccessful && _meal.value != null) {
          _meal.value!!.firebaseId = it.result.toString()
        }
      }
    } catch (e: Exception) {
      Log.e("Error storing meal", e.message.toString())
      throw Exception("Error storing meal : ${e.message} ")
    }
  }

  fun addIngredient(ingredient: Ingredient) {
    val currentMeal = _meal.value
    if (currentMeal != null) {
      val updatedMeal = currentMeal.copy()
      updatedMeal.addIngredient(ingredient)
      _meal.value = updatedMeal // Emit the new instance as the current state
    }
  }

  fun removeIngredient(ingredient: Ingredient) {
    val currentMeal = _meal.value
    if (currentMeal != null) {
      val updatedMeal =
          currentMeal.copy(
              ingredients = currentMeal.ingredients.toMutableList().apply { remove(ingredient) },
              nutritionalInformation =
                  currentMeal.nutritionalInformation.minus(ingredient.nutritionalInformation))
      _meal.value = updatedMeal
    }
  }

  fun addTag(tag: MealTag) {
    _meal.value!!.tags.add(tag)
  }

  fun removeTag(tag: MealTag) {
    _meal.value!!.tags.remove(tag)
  }
}
