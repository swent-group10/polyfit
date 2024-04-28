package com.github.se.polyfit.viewmodel.post

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.model.post.UnmodifiablePost
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltViewModel
class CreatePostViewModel
@Inject
constructor(
    private val mealRepository: MealRepository,
    private val postFirebaseRepository: PostFirebaseRepository,
) : ViewModel() {
  private val _post = Post.default()

  val post: UnmodifiablePost
    get() = _post

  suspend fun getRecentMeals() = mealRepository.getAllMeals().sortedBy { it.createdAt }.take(5)

  fun setPostDescription(description: String) {
    _post.description = description
  }

  fun setPostData(
      userId: String = _post.userId,
      description: String = _post.description,
      location: Location = _post.location,
      meal: Meal = _post.meal,
      createdAt: LocalDate = _post.createdAt
  ) {
    _post.userId = userId
    _post.description = description
    _post.location = location
    _post.meal = meal
    _post.createdAt = createdAt
  }

  fun setPostLocation(location: Location) {
    _post.location = location
  }

  fun getCarbs(): Double {
    return _post.getCarbs()?.amount ?: 0.0
  }

  fun getFat(): Double {
    return _post.getFat()?.amount ?: 0.0
  }

  fun getProtein(): Double {
    return _post.getProtein()?.amount ?: 0.0
  }

  fun setPost() {
    try {
      // Store the post in the database
      // Needs to extends beyond the lifespam of the viewModel
      GlobalScope.launch { postFirebaseRepository.storePost(_post) }
    } catch (e: Exception) {
      Log.e("CreatePostViewModel", "Failed to store post in the database : ${e.message}", e)
      throw Exception("Failed to store post in the database : ${e.message}")
    }
  }
}
