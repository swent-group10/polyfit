package com.github.se.polyfit.viewmodel.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate
import kotlinx.coroutines.launch

class MealViewModel(
    private val userId: String,
    private val context: android.content.Context,
    firebaseID: String = "",
    var initialMeal: Meal? = null,
    val mealRepo: MealRepository
) : ViewModel() {
  // after friday use hilt dependency injection to make code cleaner, for now i guess this is ok
  private val _meal: MutableLiveData<Meal> = MutableLiveData(null)
  val meal: LiveData<Meal> = _meal

  // Todo: If find a way to import Transformations, can use that to prevent duplicating updates
  private val _isComplete: MutableLiveData<Boolean> = MutableLiveData(false)
  val isComplete: LiveData<Boolean> = _isComplete

  init {
    viewModelScope.launch {
      if (firebaseID.isNotEmpty()) {
        mealRepo.getMeal(firebaseID).let {
          if (it != null) {
            _meal.value = it
            _isComplete.value = it.isComplete()
          }
        }
      } else {
        _meal.value = initialMeal?.copy() ?: Meal.default()
        _isComplete.value = _meal.value?.isComplete() ?: false
      }

      _meal.observeForever { initialMeal = it }
    }
  }

  fun setMealData(meal: Meal) {
    _meal.value = meal
  }

  /** Allows for setting individual meal data fields instead of setting the whole meal */
  fun setMealData(
      mealOccasion: MealOccasion = _meal.value?.occasion ?: Meal.default().occasion,
      name: String = _meal.value?.name ?: Meal.default().name,
      mealID: Long = _meal.value?.mealID ?: Meal.default().mealID,
      mealTemp: Double = _meal.value?.mealTemp ?: Meal.default().mealTemp,
      ingredients: MutableList<Ingredient> = _meal.value?.ingredients ?: Meal.default().ingredients,
      nutritionalInformation: NutritionalInformation =
          _meal.value?.nutritionalInformation ?: Meal.default().nutritionalInformation,
      firebaseID: String = _meal.value?.firebaseId ?: Meal.default().firebaseId,
      createdAt: LocalDate = _meal.value?.createdAt ?: Meal.default().createdAt
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

  fun setMealName(name: String) {
    _meal.value!!.name = name
    _isComplete.value = _meal.value?.isComplete() ?: false
  }

  /** Store the meal in the meal repository */
  fun setMeal() {
    if (_meal.value == null) {
      throw IllegalStateException("Meal is null")
    }

    if (!_meal.value!!.isComplete()) {
      throw Exception("Meal is incomplete")
    }

    viewModelScope.launch { mealRepo.storeMeal(_meal.value!!) }
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

  fun clearMeal() {
    _meal.value = Meal.default()
    _isComplete.value = false
  }
}
