package com.github.se.polyfit.viewmodel.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.model.post.PostLocationModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the post view. It is responsible for fetching the posts from the Firebase and
 * handling the user interaction with the posts. It is responsible for keeping the list up to date
 *
 * @property postFirebaseRepository PostFirebaseRepository
 */
@HiltViewModel
class ViewPostViewModel
@Inject
constructor(
    private val postFirebaseRepository: PostFirebaseRepository,
    private val postLocalRepository: PostLocationModel
) : ViewModel() {
  private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(mutableListOf())
  val posts: StateFlow<List<Post>> = _posts

  private val _isFetching: MutableLiveData<Boolean> = MutableLiveData(false)
  val isFetching: LiveData<Boolean> = _isFetching

  private val _location = MutableLiveData<Location>()
  val location: LiveData<Location> = _location

  init {
    viewModelScope.launch {
      _isFetching.postValue(true)
      _location.value =
          postLocalRepository.getCurrentLocation(
              CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build())
    }
    _location.observeForever { getNearbyPosts(it) }
  }

  fun getNearbyPosts(location: Location) {
    viewModelScope.launch(Dispatchers.Main) {
      postFirebaseRepository.queryNearbyPosts(
          centerLatitude = location.latitude,
          centerLongitude = location.longitude,
          radiusInKm = 2.0,
          completion = { posts ->
            _isFetching.postValue(false)
            _posts.value = posts
          })
    } // FYI: UI updates only on Main Thread
  }
}
