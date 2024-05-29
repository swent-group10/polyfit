package com.github.se.polyfit.viewmodel.recipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.data.api.RecipeFromIngredientsResponseAPI
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.model.recipe.Recipe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecipeRecommendationViewModelTest {
  @get:Rule val rule = InstantTaskExecutorRule()

  private lateinit var spoonacularApiCaller: SpoonacularApiCaller
  private lateinit var viewModel: RecipeRecommendationViewModel

  @Before
  fun setUp() {
    spoonacularApiCaller = mockk(relaxed = true)
    viewModel = RecipeRecommendationViewModel(spoonacularApiCaller)
  }

  @Test
  fun recipeFromIngredientsReturnsRecipes() = runBlocking {
    val ingredients = listOf("apple", "banana")
    val expectedRecipes = listOf(Recipe.default())

    coEvery { spoonacularApiCaller.recipeByIngredients(any()) } returns
        RecipeFromIngredientsResponseAPI(APIResponse.SUCCESS, expectedRecipes)
    coEvery { spoonacularApiCaller.getCompleteRecipesFromIngredients(any()) } returns
        listOf(Recipe.default())

    val actualRecipes = viewModel.recipeFromIngredients(listOf("apple", "banana"))

    assertEquals(listOf(Recipe.default()), actualRecipes)
  }

  @Test
  fun recipeFromIngredientsReturnsEmptyWhenInvalid() = runBlocking {
    val ingredients = listOf("invalid", "ingredients")
    val expectedRecipes = emptyList<Recipe>()

    coEvery { spoonacularApiCaller.recipeByIngredients(ingredients) } returns
        RecipeFromIngredientsResponseAPI(APIResponse.FAILURE, expectedRecipes)

    val actualRecipes = viewModel.recipeFromIngredients(ingredients)

    assertEquals(listOf<Recipe>(), actualRecipes)
  }

  @Test
  fun testIngredientListReturns() {
    val expectedIngredients = listOf("apple", "banana")
    val actualIngredients = viewModel.ingredientList()

    assertEquals(expectedIngredients, actualIngredients)
  }
}
