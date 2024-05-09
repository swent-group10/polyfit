package com.github.se.polyfit.viewmodel.recipe

import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.model.recipe.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class RecipeRecommendationViewModel
@Inject
constructor(private val spoonacularApiCaller: SpoonacularApiCaller) : ViewModel() {

  suspend fun recipeFromIngredients(ingredients: List<String>): List<Recipe> {
    val recipesResponse =
        withContext(Dispatchers.IO) { spoonacularApiCaller.recipeByIngredients(ingredients) }
    return recipesResponse.recipes
  }

  // A mock for now while waiting to the QR code scanner implementation
  fun ingredientList(): List<String> {
    return listOf("apple", "banana")
  }
}
