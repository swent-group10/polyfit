package com.github.se.polyfit.viewmodel.recipe

import android.util.Log
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

  private val _ingredientList = MutableLiveData<List<String>>(emptyList())

  private val _isFetching = MutableLiveData<Boolean>()
  val isFetching: LiveData<Boolean> = _isFetching

  private val _recipes = MutableLiveData<List<Recipe>>()
  val recipes: LiveData<List<Recipe>> = _recipes

  init {
    _showIngredient.postValue(true)
  }

  fun setIngredientList(ingredientList: List<IngredientsScanned>) {
    _ingredientList.value = ingredientList.map { it.name }

    Log.d("RecipeRecommendationViewModel", "setIngredientList: ${_ingredientList.value}")
  }

  suspend fun recipeFromIngredients(): List<Recipe> {
    // Primitive caching logic
    if (!_recipes.value.isNullOrEmpty()) {
      return _recipes.value!!
    }

    Log.d(
        "RecipeRecommendationViewModel",
        "recipeFromIngredients fetching recipe: ${_ingredientList.value}")
    val recipesResponse =
        withContext(Dispatchers.Default) {
          _isFetching.postValue(true)
          Log.d("RecipeRecommendationViewModel", "recipeFromIngredients: runBlocking")
          if (!_ingredientList.value.isNullOrEmpty()) {
            Log.d("RecipeRecommendationViewModel", "recipeFromIngredients: empty list")
            val listRecipe =
                spoonacularApiCaller.getCompleteRecipesFromIngredients(_ingredientList.value!!)
            _isFetching.postValue(false)

            return@withContext listRecipe
          } else {
            _isFetching.postValue(false)

            return@withContext listOf()
          }
        }

    Log.d("RecipeRecommendationViewModel", "recipeFromIngredients: ${recipesResponse.size}")
    _recipes.postValue(recipesResponse)

    return recipesResponse
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
