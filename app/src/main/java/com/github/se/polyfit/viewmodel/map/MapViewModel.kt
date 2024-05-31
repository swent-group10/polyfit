package com.github.se.polyfit.viewmodel.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.model.post.PostLocationModel
import com.google.android.gms.location.CurrentLocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val repository: PostFirebaseRepository,
    private val positionModel: PostLocationModel
) : ViewModel() {

  private val _location = MutableLiveData<Location>()
  val location: LiveData<Location> = _location

  private val _radius = MutableLiveData<Double>()
  val radius: LiveData<Double> = _radius

  private var _posts = MutableLiveData<List<Post>>()
  val posts: LiveData<List<Post>> = _posts

  init {
    setRadius(1.0) // default radius can be change later for a better default value
  }

  /** fetch the posts from the repository */
  fun listenToPosts() {
    if (_location.value == null) return
    repository.queryNearbyPosts(
        centerLatitude = location.value!!.latitude,
        centerLongitude = location.value!!.longitude,
        radiusInKm = radius.value!!,
        completion = { posts -> _posts.postValue(posts) })
  }

  fun setRadius(radius: Double) {
    val radiusToSet: Double =
        if (radius < 0.0) {
          0.0
        } else {
          radius
        }
    _radius.value = radiusToSet
  }

  fun setLocation(location: Location) {
    _location.value = location
  }

  fun getLocation(): Location {
    return location.value!!
  }

  fun getCurrentLocation(): Deferred<Location> {
    return viewModelScope.async(Dispatchers.Default) {
      val locationToSet = positionModel.getCurrentLocation(CurrentLocationRequest.Builder().build())
      locationToSet
    }
  }
}
