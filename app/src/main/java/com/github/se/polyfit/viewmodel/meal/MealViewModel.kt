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

  init {
    if (firebaseID.isNotEmpty()) {
      mealRepo.getMeal(firebaseID).addOnCompleteListener {
        if (it.isSuccessful) {
          _meal.value = it.result
        }
      }
    } else if (initialMeal != null) {
      _meal.value = initialMeal.copy()
    } else {
      _meal.value = Meal.default()
    }
  }

  fun setLiveMeal(livemeal: LiveData<Meal>) {
    livemeal.observeForever { _meal.value = it }
  }

  fun setMealData(meal: Meal) {
    _meal.value = meal
  }

  fun setMealName(name: String) {
    _meal.value!!.name = name
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
      // TODO: Ideally use the Meal.addIngredient method but its not working rn...
      val updatedMeal =
          currentMeal.copy(
              ingredients = currentMeal.ingredients.toMutableList().apply { add(ingredient) },
              nutritionalInformation =
                  currentMeal.nutritionalInformation.plus(ingredient.nutritionalInformation))
      _meal.value = updatedMeal // Emit the new instance as the current state
    }
  }

  fun removeIngredient(ingredient: Ingredient) {
    val currentMeal = _meal.value
    if (currentMeal != null) {
      // TODO: Use a method of Meal somehow
      val updatedMeal =
          currentMeal.copy(
              ingredients = currentMeal.ingredients.toMutableList().apply { remove(ingredient) },
              nutritionalInformation =
                  currentMeal.nutritionalInformation.minus(ingredient.nutritionalInformation))
      _meal.value = updatedMeal
    }
  }
}
