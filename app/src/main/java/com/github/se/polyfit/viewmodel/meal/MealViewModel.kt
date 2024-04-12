package com.github.se.polyfit.viewmodel.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal

class MealViewModel(
    private val userId: String,
    firebaseID: String = "",
    initialMeal: Meal? = null,
    private val mealRepo: MealFirebaseRepository = MealFirebaseRepository(userId)
) : ViewModel() {
  // after friday use hilt dependency injection to make code cleaner, for now i guess this is ok
  private val _meal: MutableLiveData<Meal> = MutableLiveData(null)
  val meal: LiveData<Meal> = _meal
  // Todo: If find a way to import Transformations, can use that to prevent duplicating updates
  private val _isComplete: MutableLiveData<Boolean> = MutableLiveData(false)
  val isComplete: LiveData<Boolean> = _isComplete

  init {
    if (firebaseID.isNotEmpty()) {
      mealRepo.getMeal(firebaseID).addOnCompleteListener {
        if (it.isSuccessful) {
          _meal.value = it.result
          _isComplete.value = it.result?.isComplete() ?: false
        }
      }
    } else {
      _meal.value = initialMeal?.copy() ?: Meal.default()
      _isComplete.value = _meal.value?.isComplete() ?: false
    }
  }

  fun setMealName(name: String) {
    _meal.value!!.name = name
    _isComplete.value = _meal.value?.isComplete() ?: false
  }

  fun setMeal() {
    if (_meal.value == null) {
      throw IllegalStateException("Meal is null")
    }

    if (!_meal.value!!.isComplete()) {
      throw Exception("Meal is incomplete")
    }

    try {
      mealRepo.storeMeal(_meal.value!!)
    } catch (e: Exception) {
      throw e
    }
  }

  fun addIngredient(ingredient: Ingredient) {
    val currentMeal = _meal.value
    if (currentMeal != null) {
      val updatedMeal = currentMeal.copy()
      updatedMeal.addIngredient(ingredient)
      _meal.value = updatedMeal // Emit the new instance as the current state
    }
    _isComplete.value = _meal.value?.isComplete() ?: false
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
    _isComplete.value = _meal.value?.isComplete() ?: false
  }
}
