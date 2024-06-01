package com.github.se.polyfit.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.data.local.ingredientscanned.IngredientsScanned
import com.github.se.polyfit.model.recipe.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ViewModel for the RecipeRecommendation uis. It is responsible for fetching the recipes from the
 * Spoonacular API and handling the user interaction with the recipes.
 *
 * @property spoonacularApiCaller SpoonacularApiCaller
 */
@HiltViewModel
class RecipeRecommendationViewModel
@Inject
constructor(private val spoonacularApiCaller: SpoonacularApiCaller) : ViewModel() {
  private var _showIngredient = MutableLiveData<Boolean>()

  val showIngredient: LiveData<Boolean> = _showIngredient

  private val _selectedRecipe: MutableLiveData<Recipe> = MutableLiveData<Recipe>()
  val selectedRecipe: LiveData<Recipe> = _selectedRecipe

  private val _ingredientList = MutableLiveData<List<String>>()

  init {
    _showIngredient.postValue(true)
  }

  fun setIngredientList(ingredientList: List<IngredientsScanned>) {
    _ingredientList.value = ingredientList.map { it.name }
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
