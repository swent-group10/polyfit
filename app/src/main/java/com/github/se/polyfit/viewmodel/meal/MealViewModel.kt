package com.github.se.polyfit.viewmodel.meal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MealViewModel
@Inject
constructor(
    private val mealRepo: MealRepository,
    private val user: User,
) : ViewModel() {
  private val _meal = MutableStateFlow<Meal>(Meal.default())
  val meal: StateFlow<Meal>
    get() = _meal

  private val _isComplete: StateFlow<Boolean> =
      _meal.map { it.isComplete() }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
  val isComplete: StateFlow<Boolean>
    get() = _isComplete

  fun setMealData(meal: Meal) {
    _meal.value = meal
  }

  fun setMealData(mealId: String?) {
    if (mealId.isNullOrEmpty()) {
      _meal.value = Meal.default()
      return
    }
    viewModelScope.launch(Dispatchers.Default) {
      val meal = mealRepo.getMealById(mealId)
      if (meal != null) {
        _meal.value = meal
      } else {
        Log.e("MealViewModel", "Meal with ID $mealId not found")
        _meal.value = Meal.default()
      }
    }
  }

  fun updateMealData(
      mealOccasion: MealOccasion = _meal.value.occasion,
      name: String = _meal.value.name,
      mealTemp: Double = _meal.value.mealTemp,
      ingredients: MutableList<Ingredient> = _meal.value.ingredients,
      createdAt: LocalDate = _meal.value.createdAt,
      tags: MutableList<MealTag> = _meal.value.tags
  ) {
    _meal.value =
        Meal(
            mealOccasion,
            name,
            _meal.value.id,
            _meal.value.userId,
            mealTemp,
            ingredients,
            createdAt,
            tags)
  }

  fun setMealCreatedAt(createdAt: LocalDate) {
    _meal.value = _meal.value.deepCopy(createdAt = createdAt)
  }

  fun setMealOccasion(occasion: MealOccasion) {
    _meal.value = _meal.value.deepCopy(occasion = occasion)
  }

  fun setMeal() {
    _meal.value.userId = user.id

    if (!_meal.value.isComplete()) {
      throw Exception("Meal is incomplete")
    }
    GlobalScope.launch {
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
    _meal.value = _meal.value.deepCopy(ingredients = updatedIngredients)
  }

  fun removeIngredient(ingredient: Ingredient) {
    val updatedIngredients = _meal.value.ingredients.toMutableList().apply { remove(ingredient) }

    _meal.value = _meal.value.deepCopy(ingredients = updatedIngredients)
  }

  fun addTag(tag: MealTag) {
    _meal.value.tags.add(tag)
  }

  fun removeTag(tag: MealTag) {
    _meal.value.tags.remove(tag)
  }
}
