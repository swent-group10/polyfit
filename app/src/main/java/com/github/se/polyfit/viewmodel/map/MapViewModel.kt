package com.github.se.polyfit.viewmodel.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.model.post.PostLocationModel
import com.google.android.gms.location.CurrentLocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
    setRadius(0.0) // default radius can be change later for a better default value
  }

  fun listenToPosts() {

    if (_location.value == null) return
    repository.queryNearbyPosts(
        centerLatitude = _location.value!!.latitude,
        centerLongitude = _location.value!!.longitude,
        radiusInKm = radius.value!!,
        completion = { posts ->
          Log.d("MapViewModel", "listenToPosts: $posts")
          Log.d("MapViewModel", "listenToPosts: ${posts.size}")
          _posts.postValue(posts)
        })
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
    Log.i("Map", "setLocation: $location")
  }

  suspend fun getCurrentLocation(): Location {
    return positionModel.getCurrentLocation(CurrentLocationRequest.Builder().build())
  }
}
