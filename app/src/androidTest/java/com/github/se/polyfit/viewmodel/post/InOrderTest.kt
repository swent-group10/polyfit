package com.github.se.polyfit.viewmodel.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.post.PostLocationModel
import com.google.android.gms.location.CurrentLocationRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlin.test.Test
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class InOrderTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private val testDispatcher = TestCoroutineDispatcher()
  private val testScope = TestCoroutineScope(testDispatcher)

  private lateinit var viewModel: CreatePostViewModel
  private val mockMealRepository = mockk<MealRepository>(relaxed = true)
  private val mockPostFirebaseRepository = mockk<PostFirebaseRepository>(relaxed = true)
  private val mockPostLocationModel = mockk<PostLocationModel>(relaxed = true)
  private val locationRequest = mockk<CurrentLocationRequest>(relaxed = true)

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel =
        spyk(
            CreatePostViewModel(
                mockMealRepository, mockPostFirebaseRepository, mockPostLocationModel),
            recordPrivateCalls = true)

    // Mock the suspend function initPostLocation
    coEvery { viewModel.initPostLocation(locationRequest) } returns
        CompletableDeferred(Unit).also { it.complete(Unit) }

    // Mock the regular function setPost
    every { viewModel.setPost() } just Runs
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    testScope.cleanupTestCoroutines()
    unmockkAll()
  }

  @Test
  fun testSetInOrder() = runTest {
    // Call the function under test
    viewModel.setInOrder(locationRequest)

    // Verify the order of calls
    coVerifyOrder {
      viewModel.initPostLocation(locationRequest)
      viewModel.setPost()
    }
  }
}
