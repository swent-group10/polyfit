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
import com.github.se.polyfit.model.post.PostLocationModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import java.time.LocalDate
import kotlin.test.assertFailsWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreatePostViewModelTest {

  private lateinit var viewModel: CreatePostViewModel
  private val mockMealRepository = mockk<MealRepository>(relaxed = true)
  private val mockPostFirebaseRepository = mockk<PostFirebaseRepository>(relaxed = true)
  private val mockPostLocationModel = mockk<PostLocationModel>(relaxed = true)
  private val postLocationModel = mockk<PostLocationModel>(relaxed = true)


  @Before
  fun setup() {

    viewModel =
      CreatePostViewModel(mockMealRepository, mockPostFirebaseRepository, mockPostLocationModel)
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
        .copy(nutritionalInformation = NutritionalInformation(mutableListOf(carbs)))
    )

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
        .copy(nutritionalInformation = NutritionalInformation(mutableListOf(fat)))
    )
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
        .copy(nutritionalInformation = NutritionalInformation(mutableListOf(protein)))
    )

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

  @Test
  fun testSetPostLocation() = runTest {
    val mockLocation = com.github.se.polyfit.model.post.Location(0.0, 0.0, 0.0, "")
    coEvery { postLocationModel.getCurrentLocation() } returns mockLocation

    viewModel.setPostLocation().join()

    assertEquals(mockLocation.latitude, viewModel.post.location.latitude, 0.0001)
    assertEquals(mockLocation.altitude, viewModel.post.location.altitude, 0.0001)
    assertEquals(mockLocation.altitude, viewModel.post.location.altitude, 0.0001)
  }

  @Test
  fun testSetInOrder() = runTest {
    val mockJob = mockk<Job> {
      coEvery { join() } just Runs
    }

    // Call setInOrder with the mocked job and a dummy function for second
    viewModel.setInOrder(first = mockJob, second = { viewModel.setPost() })

    // Verify that first.join() was called before second() was executed
    coVerifyOrder {
      mockJob.join()
      viewModel.setPost()
    }
  }
}
