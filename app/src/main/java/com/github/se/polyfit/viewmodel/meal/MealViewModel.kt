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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepo: MealRepository) : ViewModel() {
    init {
        Log.d("MealViewModelCreation", "MealViewModel created")
    }

    private val _meal = MutableStateFlow<Meal>(Meal.default())
    val meal: StateFlow<Meal>
        get() = _meal

    private val _isComplete = MutableStateFlow<Boolean>(false)
    val isComplete: StateFlow<Boolean>
        get() = _isComplete

    init {
        viewModelScope.launch { _meal.collect { meal -> _isComplete.value = meal.isComplete() } }
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
        firebaseID: String = _meal.value.firebaseId,
        createdAt: LocalDate = _meal.value.createdAt,
        tags: MutableList<MealTag> = _meal.value.tags
    ) {
        // When we make a new Meal, we add all the ingredient values into nutritionalInfo. If we pass
        // existing values, then its double adding
        val newNutritionalInformation = NutritionalInformation(mutableListOf())
        _meal.value =
            Meal(
                mealOccasion,
                name,
                mealID,
                mealTemp,
                newNutritionalInformation,
                ingredients,
                firebaseID,
                createdAt,
                tags
            )
    }

    fun setMealCreatedAt(createdAt: LocalDate) {
        _meal.value = _meal.value.deepCopy(createdAt = createdAt)
    }

    fun setMealOccasion(occasion: MealOccasion) {
        _meal.value = _meal.value.deepCopy(occasion = occasion)
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
        _meal.value = _meal.value.deepCopy(ingredients = updatedIngredients)
    }

    fun removeIngredient(ingredient: Ingredient) {
        val updatedIngredients =
            _meal.value.ingredients.toMutableList().apply { remove(ingredient) }

        _meal.value = _meal.value.deepCopy(ingredients = updatedIngredients)
    }

    fun addTag(tag: MealTag) {
        _meal.value.tags.add(tag)
    }

    fun removeTag(tag: MealTag) {
        _meal.value.tags.remove(tag)
    }

    // TODO: This can be removed once we are properly using a new ViewModel for each Meal
    fun reset() {
        _meal.value = Meal.default()
        _isComplete.value = false
    }
}
