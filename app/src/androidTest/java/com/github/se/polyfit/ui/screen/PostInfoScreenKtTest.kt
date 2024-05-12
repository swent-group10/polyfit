package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.github.se.polyfit.viewmodel.post.ViewPostViewModel
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostInfoScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var viewPostViewModel: ViewPostViewModel

  @Before
  fun setup() {
    viewPostViewModel = mockk<ViewPostViewModel>(relaxed = true)
    every { viewPostViewModel.isFetching } returns MutableStateFlow(false)
  }

  @Test
  fun PostInfoScreen_displays_post_information() {
    val mealViewModel = mockk<MealViewModel>(relaxed = true)
    val post = Post.default()
    post.meal.addIngredient(
        Ingredient(
            "ingredient1",
            100,
            100.0,
            MeasurementUnit.G,
            NutritionalInformation(
                mutableListOf(
                    Nutrient("Protein", 12.0, MeasurementUnit.G),
                    Nutrient("Carbohydrates", 21.50, MeasurementUnit.G),
                    Nutrient("Fats", 25.0, MeasurementUnit.G),
                    Nutrient("Vitamins", 40.0, MeasurementUnit.G),
                ))))
    post.description = "Meal of the day, with a lot of nutrients and vitamins"
    val posts = listOf(post)

    every { mealViewModel.meal } returns MutableStateFlow(Meal.default())
    every { mealViewModel.isComplete } returns MutableStateFlow(true)
    every { viewPostViewModel.posts } returns MutableStateFlow(posts)

    composeTestRule.setContent {
      PostInfoScreenContent(posts, viewPostViewModel = viewPostViewModel)
    }

    composeTestRule
        .onNodeWithTag("DescriptionTitle")
        .assertExists()
        .assertTextContains("Description")
    composeTestRule
        .onNodeWithTag("Description")
        .assertExists()
        .assertTextContains("Meal of the day, with a lot of nutrients and vitamins")
    composeTestRule
        .onNodeWithTag("Date")
        .assertExists()
        .assertTextContains(LocalDate.now().dayOfMonth.toString(), substring = true)
        .assertTextContains(LocalDate.now().month.toString(), substring = true)
    composeTestRule.onNodeWithTag("NutrientTitle").assertExists()
  }

  @Test
  fun PostInfoScreen_displays_no_post_available_when_there_are_no_posts() {
    val posts = listOf<Post>()
    every { viewPostViewModel.posts } returns MutableStateFlow(posts)

    composeTestRule.setContent {
      PostInfoScreenContent(posts, viewPostViewModel = viewPostViewModel)
    }

    composeTestRule
        .onNodeWithTag("NoPostText")
        .assertExists()
        .assertTextContains("Unfortunately there is no post", substring = true)
        .assertExists()
        .assertTextContains("available around you", substring = true)
  }
}
