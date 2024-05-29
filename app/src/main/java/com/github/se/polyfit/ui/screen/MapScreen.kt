package com.github.se.polyfit.ui.screen

// import the colors PrimaryPink

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.ui.components.postinfo.PostCard
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.viewmodel.map.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Size in meters of the rayon of the circle to see other posts
val MIN_SIZE_RAYON = 100f
val MAX_SIZE_RAYON = 2000f
val EPFL_LATITUDE = 46.5181
val EPFL_LONGITUDE = 6.5659

@Composable
fun MapScreen(paddingValues: PaddingValues, mapViewModel: MapViewModel = hiltViewModel()) {

  val posts = remember { mutableStateOf(listOf<Post>()) }
  val currentLocation = remember { mutableStateOf(Location.default()) }

  var selectedPost by remember { mutableStateOf<Post?>(null) }

  var isCircleShown by remember { mutableStateOf(false) }
  var mainLaunchedIsFinished by remember { mutableStateOf(false) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(LatLng(EPFL_LATITUDE, EPFL_LONGITUDE), 15f)
  }
  var sliderPosition by remember { mutableFloatStateOf((MIN_SIZE_RAYON + MAX_SIZE_RAYON) / 2) }

  /* LaunchedEffect(currentLocation) {
    mutex.withLock {
      currentLocation.value = mapViewModel.getCurrentLocation()
      mapViewModel.setLocation(currentLocation.value)
    }

    mapViewModel.listenToPosts()
    mapViewModel.posts.observeForever { posts.value = it }
  } */

  LaunchedEffect(key1 = mapViewModel) {
    currentLocation.value = mapViewModel.getCurrentLocation().await()
    mapViewModel.setLocation(currentLocation.value)

    Log.i("MapScreen", "in MapScreen Location after setting: ${currentLocation.value}")

    mapViewModel.listenToPosts()

    mapViewModel.posts.observeForever { posts.value = it }

    cameraPositionState.position =
        CameraPosition.fromLatLngZoom(
            LatLng(currentLocation.value.latitude, currentLocation.value.longitude), 15f)

    mainLaunchedIsFinished = true

    Log.i("MapScreen", "Camera position set to: ${cameraPositionState.position}")
  }
  /*LaunchedEffect(key1 =sliderPosition) {
    if (mainLaunchedIsFinished) {
      isCircleShown = true
      mapViewModel.setRadius(sliderPosition.toDouble() / 1000)
      mapViewModel.listenToPosts()
      mapViewModel.posts.observeForever { posts.value = it }
      delay(3000)
      isCircleShown = false
    }
  }*/

  fun onSliderChanged(scope: CoroutineScope, newPosition: Float) {
    sliderPosition = newPosition

    scope.launch {
      if (mainLaunchedIsFinished) {

        mapViewModel.setRadius(newPosition.toDouble() / 1000)
        mapViewModel.listenToPosts()


      }
    }
  }

  val listMarker =
      remember(posts.value) {
        posts.value.map { post ->
          PostMarker(
              post = post,
              markerState = MarkerState(LatLng(post.location.latitude, post.location.longitude)))
        }
      }

  Box(
      modifier = Modifier.fillMaxSize().padding(paddingValues),
      contentAlignment = Alignment.BottomStart) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            onMapClick = { selectedPost = null }) {
              Circle(
                  center = LatLng(currentLocation.value.latitude, currentLocation.value.longitude),
                  radius =
                      (sliderPosition).toDouble() + 10, // Make the shadow circle slightly larger
                  strokeColor =
                      Color.Black.copy(alpha = 0.2f) // Use a semi-transparent color for the shadow
                  ,
                  visible = isCircleShown)
              Circle(
                  center = LatLng(currentLocation.value.latitude, currentLocation.value.longitude),
                  radius = (sliderPosition).toDouble(),
                  strokeColor = PrimaryPurple,
                  visible = isCircleShown)
              listMarker.forEach { postMarker ->
                Marker(
                    state = postMarker.markerState,
                    title = "Post",
                    contentDescription = postMarker.post.description,
                    icon =
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                    onClick = {
                      selectedPost = postMarker.post
                      true
                    })
              }
            }
        Row(modifier = Modifier.padding(end = 64.dp)) {
          val scope = rememberCoroutineScope()
          Slider(
              value = sliderPosition,
              onValueChange = {
                onSliderChanged(scope, it)
                isCircleShown = true
              },
              valueRange = MIN_SIZE_RAYON..MAX_SIZE_RAYON,
              modifier = Modifier.align(Alignment.CenterVertically).widthIn(10.dp, 200.dp),
              onValueChangeFinished = { isCircleShown = false })
        }

        selectedPost?.let { post ->
          Box(modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.BottomCenter)) {
            PostCard(post)
          }
        }
      }
}

data class PostMarker(val post: Post, val markerState: MarkerState)
