package com.github.se.polyfit.viewmodel.post

import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreatePostViewModelTest {

  private lateinit var viewModel: CreatePostViewModel
  private val mockMealRepository = mockk<MealRepository>(relaxed = true)
  private val mockPostFirebaseRepository = mockk<PostFirebaseRepository>(relaxed = true)

  @Before
  fun setup() {
    viewModel = CreatePostViewModel(mockMealRepository, mockPostFirebaseRepository)
  }

  @Test
  fun getRecentMealsReturnsSortedMeals() = runTest {
    val meals = listOf(Meal.default(), Meal.default(), Meal.default())
    coEvery { mockMealRepository.getAllMeals() } returns meals

    val result = viewModel.getRecentMeals()

    assertEquals(meals.sortedBy { it.createdAt }, result)
  }

  @Test
  fun setPostDescriptionUpdatesPostDescription() {
    val description = "Test description"

    viewModel.setPostDescription(description)

    assertEquals(description, viewModel.post.description)
  }

  @Test
  fun setPostLocationUpdatesPostLocation() {
    val location = Location.default()

    viewModel.setPostLocation(location)

    assertEquals(location, viewModel.post.location)
  }

  @Test
  fun setPostStoresPostInRepository() = runTest {
    viewModel.setPost()

    coVerify { mockPostFirebaseRepository.storePost(any<Post>()) }
  }

  @Test
  fun setPostDataUpdatesPostData() {
    val userId = "testUserId"
    val description = "Test description"
    val location = Location.default()
    val meal = Meal.default()
    val createdAt = mockk<LocalDate>()

    viewModel.setPostData(userId, description, location, meal, createdAt)

    assertEquals(userId, viewModel.post.userId)
    assertEquals(description, viewModel.post.description)
    assertEquals(location, viewModel.post.location)
    assertEquals(meal, viewModel.post.meal)
    assertEquals(createdAt, viewModel.post.createdAt)
  }

  @Test
  fun getCarbsReturnsCarbsAmount() {
    val carbs = Nutrient("carbohydrates", 10.0, MeasurementUnit.G)
    val meal = Meal.default()
    meal.addIngredient(
        Ingredient.default()
            .copy(nutritionalInformation = NutritionalInformation(mutableListOf(carbs))))

    val post = Post.default().copy(meal = meal)
    viewModel.setPostData(meal = meal)

    val result = viewModel.getCarbs()

    assertEquals(carbs.amount, result, 0.1)
  }

  @Test
  fun getFatReturnsFatAmount() {
    val fat = Nutrient("fat", 10.0, MeasurementUnit.G)
    val meal = Meal.default()
    meal.addIngredient(
        Ingredient.default()
            .copy(nutritionalInformation = NutritionalInformation(mutableListOf(fat))))
    val post = Post.default().copy(meal = meal)
    viewModel.setPostData(meal = meal)

    val result = viewModel.getFat()

    assertEquals(fat.amount, result, 0.1)
  }

  @Test
  fun getProteinReturnsProteinAmount() {
    val protein = Nutrient("protein", 10.0, MeasurementUnit.G)
    val meal = Meal.default()
    meal.addIngredient(
        Ingredient.default()
            .copy(nutritionalInformation = NutritionalInformation(mutableListOf(protein))))

    viewModel.setPostData(meal = meal)

    val result = viewModel.getProtein()

    assertEquals(protein.amount, result, 0.1)
  }

  @Test
  fun setPostFailed(): Unit = runBlocking {
    coEvery { mockPostFirebaseRepository.storePost(any()) } throws
        Exception("Failed to store post in the database")
    assertFailsWith<Exception> { viewModel.setPost() }
  }
}
