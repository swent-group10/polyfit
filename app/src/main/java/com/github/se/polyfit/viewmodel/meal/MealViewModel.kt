package com.github.se.polyfit.viewmodel.meal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepo: MealRepository) : ViewModel() {
  private val _meal = MutableStateFlow<Meal>(Meal.default())
  val meal: StateFlow<Meal>
    get() = _meal

  private val _isComplete = MutableStateFlow<Boolean>(false)
  val isComplete: StateFlow<Boolean>
    get() = _isComplete

  init {
    viewModelScope.launch {
      _meal.collect { meal ->
        _isComplete.value = meal.isComplete()
        Log.d("MealViewModel", "Meal is complete: ${_isComplete.value}")
      }
    }
  }

  fun setMealData(meal: Meal) {
    _meal.value = meal
  }

  fun updateMealData(
      mealOccasion: MealOccasion = _meal.value.occasion,
      name: String = _meal.value.name,
      mealID: Long = _meal.value.mealID,
      mealTemp: Double = _meal.value.mealTemp,
      ingredients: MutableList<Ingredient> = _meal.value.ingredients,
      nutritionalInformation: NutritionalInformation = _meal.value.nutritionalInformation,
      firebaseID: String = _meal.value.firebaseId,
      createdAt: LocalDate = _meal.value.createdAt
  ) {
    _meal.value =
        Meal(
            mealOccasion,
            name,
            mealID,
            mealTemp,
            nutritionalInformation,
            ingredients,
            firebaseID,
            createdAt)
  }

  fun setMealCreatedAt(createdAt: LocalDate) {
    _meal.value = _meal.value?.copy(createdAt = createdAt)!!
  }

  fun setMealOccasion(occasion: MealOccasion) {
    _meal.value = _meal.value?.copy(occasion = occasion)!!
  }

  fun setMeal() {
    if (!_meal.value.isComplete()) {
      throw Exception("Meal is incomplete")
    }
    viewModelScope.launch {
      try {
        mealRepo.storeMeal(_meal.value)
      } catch (e: Exception) {
        Log.e("Error storing meal", e.message.toString())
        throw Exception("Error storing meal : ${e.message}")
      }
    }
  }

  fun addIngredient(ingredient: Ingredient) {
    val updatedIngredients = _meal.value.ingredients.toMutableList().apply { add(ingredient) }
    _meal.value = _meal.value.copy(ingredients = updatedIngredients)
  }

  fun removeIngredient(ingredient: Ingredient) {
    val updatedIngredients =
        _meal.value.ingredients.toMutableList().apply {
          remove(ingredient)
          Log.d("MealViewModel", "Removed ingredient: $ingredient")
        }
    Log.d("MealViewModel", "Removed ingredient: $ingredient")

    _meal.value = _meal.value.copy(ingredients = updatedIngredients)
  }

  fun addTag(tag: MealTag) {
    _meal.value!!.tags.add(tag)
  }

  fun removeTag(tag: MealTag) {
    _meal.value!!.tags.remove(tag)
  }
}
