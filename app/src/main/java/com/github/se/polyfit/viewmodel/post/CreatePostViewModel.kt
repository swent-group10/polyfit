package com.github.se.polyfit.viewmodel.post

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.model.post.PostLocationModel
import com.github.se.polyfit.model.post.UnmodifiablePost
import com.google.android.gms.location.CurrentLocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CreatePostViewModel
@Inject
constructor(
    private val mealRepository: MealRepository,
    private val postFirebaseRepository: PostFirebaseRepository,
    private val postLocationModel: PostLocationModel,
) : ViewModel() {
  private val _post = Post.default()

  private var bitmap: Bitmap? = null

  val post: UnmodifiablePost
    get() = _post

  /** Set the image (bitmap) to be stored in the post. */
  fun setBitMap(newBitmap: Bitmap?) {
    bitmap = newBitmap
  }

  /** Get the image (bitmap) to be stored in the post. */
  fun getBitMap(): Bitmap? {
    return bitmap?.copy(bitmap!!.config, false)
  }

  suspend fun getRecentMeals() =
      withContext(Dispatchers.Default) {
        mealRepository.getAllMeals().sortedBy { it.createdAt }.take(5)
      }

  private val _meals = MutableStateFlow<List<Meal>>(emptyList())
  val meals = _meals

  init {
    viewModelScope.launch { _meals.value = getRecentMeals() }
  }

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

  fun initPostLocation(locationRequest: CurrentLocationRequest): Job {
    return viewModelScope.launch(Dispatchers.Default) {
      val location = postLocationModel.getCurrentLocation(locationRequest)
      _post.location = location
    }
  }

  fun setInOrder(locationRequest: CurrentLocationRequest) {
    viewModelScope.launch(Dispatchers.Default) {
      initPostLocation(locationRequest).join()
      setPost()
    }
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

  /**
   * Store the post set on the firebase database.
   *
   * @throws Exception if the post could not be stored in the database
   */
  fun setPost() {
    viewModelScope.launch {
      try {
        var imageDownload: Uri? = null
        if (bitmap != null) {
          imageDownload = postFirebaseRepository.uploadImage(bitmap!!)
        }
        _post.apply {
          if (imageDownload != null) {
            imageDownloadURL = imageDownload
          }
        }
        postFirebaseRepository.storePost(_post)
      } catch (e: Exception) {
        Log.e("CreatePostViewModel", "Failed to store post in the database : ${e.message}", e)
        throw Exception("Failed to store post in the database : ${e.message}")
      }
    }
  }
}
