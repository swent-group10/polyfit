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
import kotlinx.coroutines.withContext

@HiltViewModel
class ViewPostViewModel
@Inject
constructor(
    private val postFirebaseRepository: PostFirebaseRepository,
    private val postLocalRepository: PostLocationModel
) : ViewModel() {
  private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(mutableListOf())
  val posts: StateFlow<List<Post>> = _posts

  private val _isFetching: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isFetching: StateFlow<Boolean> = _isFetching

  private val _location = MutableLiveData<Location>()
  val location: LiveData<Location> = _location

  init {

    viewModelScope.launch {
      _location.value =
          postLocalRepository.getCurrentLocation(
              CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build())
    }
    getNearbyPosts()
  }

  fun getNearbyPosts() {
    location.observeForever {
      viewModelScope.launch {
        _isFetching.value = true

        withContext(Dispatchers.Main) {
          postFirebaseRepository.queryNearbyPosts(
              centerLatitude = it.latitude,
              centerLongitude = it.longitude,
              radiusInKm = 2.0,
              completion = { posts -> _posts.value = posts })
        } // FYI: UI updates only on Main Thread

        _isFetching.value = false
      }
    }
  }
}
