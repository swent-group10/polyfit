package com.github.se.polyfit.viewmodel.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
  val _nearPost = MutableLiveData<List<Post>>(_posts.value)
  val nearPost: LiveData<List<Post>> = _nearPost

  init {
    setRadius(1.0) // default radius can be change later for a better default value
  }

  fun listenToPosts() {
    Log.d("MapViewModel", "listenToPosts1: ")
    Log.d("MapViewModel", "listenToPosts2: ${location.value}")

    if (_location.value == null) return
    repository.queryNearbyPosts(
        centerLatitude = location.value!!.latitude,
        centerLongitude = location.value!!.longitude,
        radiusInKm = radius.value!!,
        completion = { posts ->
          Log.d("MapViewModel", "listenToPosts3: $posts")
          Log.d("MapViewModel", "listenToPosts4: ${posts.size}")

          _posts.postValue(posts)
          setNearPost(radius.value, location.value, posts)
        })
  }

  fun setRadius(radiusKm: Double) {
    val radiusToSetKm: Double =
        if (radiusKm < 0) {
          0.0
        } else {
          radiusKm
        }

    _radius.value = radiusToSetKm
    setNearPost(radiusToSetKm, location.value, _posts.value)
  }

  private fun setNearPost(radiusToSetKm: Double?, location: Location?, post: List<Post>?){
    Log.i("MapViewModel", "setNearPost radius: $radiusToSetKm, location: $location, post: $post")
    if(location == null || radiusToSetKm == null || post == null) return

    Log.i("MapViewModel", "setNearPost number of posts: ${post.size}")
    val nearPostValue = post.filter {
      val distanceMeter = measure(it.location.latitude, it.location.longitude, location.latitude, location.longitude)
      Log.i("MapViewModel", "distance: $distanceMeter, radius: $radiusToSetKm")
      distanceMeter <= radiusToSetKm * 1000
    }
    _nearPost.postValue(nearPostValue)
  }

  fun setLocation(location: Location) {
    _location.value = location
    setNearPost(radius.value, location, _posts.value)
    Log.i("MapViewModel", "setLocation: $location")
  }

  fun getLocation(): Location {
    Log.i("MapViewModel", "getLocation: $location")
    return location.value!!
  }

  fun getCurrentLocation(): Deferred<Location> {
    return viewModelScope.async(Dispatchers.Default) {
      val locationToSet = positionModel.getCurrentLocation(CurrentLocationRequest.Builder().build())
      locationToSet
    }
  }
}

private fun measure(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double{  // generally used geo measurement function
  val r = 6378.137; // Radius of earth in KM
  val dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
  val dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
  val a = sin(dLat/2) * sin(dLat/2) +
          cos(lat1 * Math.PI / 180) * cos(lat2 * Math.PI / 180) *
          sin(dLon/2) * sin(dLon/2);
  val c = 2 * atan2(sqrt(a), sqrt(1-a));
  val d = r * c;
  return d * 1000; // meters
}
