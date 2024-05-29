package com.github.se.polyfit.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.model.recipe.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class RecipeRecommendationViewModel
@Inject
constructor(private val spoonacularApiCaller: SpoonacularApiCaller) : ViewModel() {
  private var _showIngredient = MutableLiveData<Boolean>()

  val showIngredient: LiveData<Boolean> = _showIngredient

  private val _selectedRecipe: MutableLiveData<Recipe> = MutableLiveData<Recipe>()
  val selectedRecipe: LiveData<Recipe> = _selectedRecipe

  init {
    _showIngredient.value = true
  }

  suspend fun recipeFromIngredients(ingredients: List<String>): List<Recipe> {
    // Removed to avoid using the Spoonacular API unnecessarily
    //    val recipesResponse =
    //        withContext(Dispatchers.Default) {
    // spoonacularApiCaller.recipeByIngredients(ingredients) }
    //    return recipesResponse.recipes

    return withContext(Dispatchers.IO) {
      spoonacularApiCaller.getCompleteRecipesFromIngredients(ingredients)
    }
  }

  // A mock for now while waiting to the QR code scanner implementation
  fun ingredientList(): List<String> {
    return listOf("apple", "banana")
  }

  fun onSelectedRecipe(recipe: Recipe) {
    _selectedRecipe.value = recipe
  }

  fun setShowIngredientFalse() {
    _showIngredient.value = false
  }

  fun setShowIngredientTrue() {
    _showIngredient.value = true
  }
}
