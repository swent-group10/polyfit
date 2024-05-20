package com.github.se.polyfit.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.model.recipe.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeRecommendationViewModel
@Inject
constructor(private val spoonacularApiCaller: SpoonacularApiCaller) : ViewModel() {
  private var _showIngredient = MutableLiveData<Boolean>()

  val showIngredient: LiveData<Boolean> = _showIngredient

  init {
    _showIngredient.value = true
  }

  suspend fun recipeFromIngredients(ingredients: List<String>): List<Recipe> {
    // Removed to avoid using the Spoonacular API unnecessarily
    //    val recipesResponse =
    //        withContext(Dispatchers.Default) {
    // spoonacularApiCaller.recipeByIngredients(ingredients) }
    //    return recipesResponse.recipes

    return listOf(Recipe.default())
  }

  // A mock for now while waiting to the QR code scanner implementation
  fun ingredientList(): List<String> {
    return listOf("apple", "banana")
  }

  fun getSelectedRecipe(): Recipe {
    return Recipe.default()
  }

  fun setRecipe(recipe: Recipe) {
    // TODO
  }

  fun setShowIngredientFalse() {
    _showIngredient.value = false
  }

  fun setShowIngredientTrue() {
    _showIngredient.value = true
  }
}
