import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.data.api.RecipeFromIngredientsResponseAPI
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.model.recipe.RecipeInformation
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.mockk.coEvery
import io.mockk.mockk
import java.net.URL
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipeRecommendationViewModelTest {

  private lateinit var spoonacularApiCaller: SpoonacularApiCaller
  private lateinit var viewModel: RecipeRecommendationViewModel

  @Before
  fun setUp() {
    spoonacularApiCaller = mockk(relaxed = true)
    viewModel = RecipeRecommendationViewModel(spoonacularApiCaller)
  }

  @Test
  fun `recipeFromIngredients returns expected recipes when ingredients are valid`() = runBlocking {
    val ingredients = listOf("apple", "banana")
    val expectedRecipes =
        listOf(
            Recipe(
                1,
                "Apple Banana Smoothie",
                URL("https://spoonacular.com/recipeImages/1-312x231.jpg"),
                2,
                1,
                0,
                RecipeInformation.default()),
            Recipe(
                2,
                "Apple Banana Bread",
                URL("https://spoonacular.com/recipeImages/2-312x231.jpg"),
                2,
                1,
                0,
                RecipeInformation.default()))

    coEvery { spoonacularApiCaller.recipeByIngredients(ingredients) } returns
        RecipeFromIngredientsResponseAPI(APIResponse.SUCCESS, expectedRecipes)

    val actualRecipes = viewModel.recipeFromIngredients(listOf("apple", "banana"))

    assertEquals(expectedRecipes, actualRecipes)
  }

  @Test
  fun `recipeFromIngredients returns empty list when ingredients are invalid`() = runBlocking {
    val ingredients = listOf("invalid", "ingredients")
    val expectedRecipes = emptyList<Recipe>()

    coEvery { spoonacularApiCaller.recipeByIngredients(ingredients) } returns
        RecipeFromIngredientsResponseAPI(APIResponse.FAILURE, expectedRecipes)

    val actualRecipes = viewModel.recipeFromIngredients(ingredients)

    assertEquals(expectedRecipes, actualRecipes)
  }

  @Test
  fun `test ingredientList returns expected list`() {
    val expectedIngredients = listOf("apple", "banana")
    val actualIngredients = viewModel.ingredientList()

    assertEquals(expectedIngredients, actualIngredients)
  }
}
