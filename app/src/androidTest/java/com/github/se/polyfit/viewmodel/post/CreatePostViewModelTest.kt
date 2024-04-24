package com.github.se.polyfit.viewmodel.post

import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
}