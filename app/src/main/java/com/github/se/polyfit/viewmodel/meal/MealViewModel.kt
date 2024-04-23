package com.github.se.polyfit.viewmodel.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepo: MealFirebaseRepository) :
    ViewModel() {

  private val _meal: MutableLiveData<Meal> = MutableLiveData(Meal.default())
  val meal: LiveData<Meal> = _meal // Expose _meal as an immutable LiveData

  private val _isComplete: LiveData<Boolean> = _meal.map { it?.isComplete() ?: false }

  val isComplete: LiveData<Boolean> = _isComplete

  fun setMealData(meal: Meal) {
    _meal.value = meal
    _isComplete.value = meal.isComplete()
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

    _isComplete.value = _meal.value?.isComplete() ?: false
  }

  fun setMealName(name: String) {
    _meal.value!!.name = name
  }

  /** Store the meal in the meal repository */
  fun setMeal() {
    if (!_meal.value!!.isComplete()) {
      throw Exception("Meal is incomplete")
    }

    //    try {
    mealRepo.storeMeal(_meal.value!!).continueWith {
      if (it.isSuccessful && _meal.value != null) {
        _meal.value!!.firebaseId = it.result.toString()
      }
      //      }
      //    } catch (e: Exception) {
      //      Log.e("Error storing meal", e.message.toString())
      //      throw Exception("Error storing meal : ${e.message} ")
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

  fun clearMeal() {
    _meal.value = Meal.default()
    _isComplete.value = false
  }
}
