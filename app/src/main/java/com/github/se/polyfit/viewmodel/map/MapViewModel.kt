package com.github.se.polyfit.viewmodel.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post

class MapViewModel(private val repository: PostFirebaseRepository = PostFirebaseRepository()) :
    ViewModel() {

  private val _location = MutableLiveData<Location>()
  val location: LiveData<Location> = _location

  private val _radius = MutableLiveData<Double>()
  val radius: LiveData<Double> = _radius

  private val _posts = MutableLiveData<List<Post>>()
  val posts: LiveData<List<Post>> = _posts

  init {
    setRadius(5.0)
  }

  fun listenToPosts() {
    repository.queryNearbyPosts(
        centerLatitude = location.value!!.latitude,
        centerLongitude = location.value!!.longitude,
        radiusInKm = radius.value!!,
        completion = { posts -> _posts.postValue(posts) })
  }

  fun setRadius(radius: Double) {
    _radius.postValue(radius)
  }

  fun setLocation(location: Location) {
    _location.postValue(location)
  }
}
