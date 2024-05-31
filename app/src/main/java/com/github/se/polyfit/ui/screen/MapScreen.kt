package com.github.se.polyfit.ui.screen

// import the colors PrimaryPink

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
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Size in meters of the rayon of the circle to see other posts
const val MIN_SIZE_RAYON = 100f
const val MAX_SIZE_RAYON = 2000f
const val EPFL_LATITUDE = 46.5181
const val EPFL_LONGITUDE = 6.5659
const val DELAY_CIRCLE_DISAPPEAR = 3000L

@OptIn(FlowPreview::class)
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

  var sliderPositionInMeters by remember {
    mutableFloatStateOf((MIN_SIZE_RAYON + MAX_SIZE_RAYON) / 2)
  }

  /**
   * Effect to be launched when the mapViewModel changes. Initializes the current location and sets
   * up the map and post listeners.
   *
   * This effect fetches the current location asynchronously, sets the location in the mapViewModel,
   * and begins listening to posts. It also updates the camera position of the map and observes
   * posts to update the state.
   *
   * @param mapViewModel The ViewModel that handles map-related data and operations.
   */
  LaunchedEffect(Unit) {
    currentLocation.value = mapViewModel.getCurrentLocation().await()
    mapViewModel.setLocation(currentLocation.value)

    mapViewModel.listenToPosts()
    mapViewModel.nearPost.observeForever { it?.let { posts.value = it } }

    cameraPositionState.position =
        CameraPosition.fromLatLngZoom(
            LatLng(currentLocation.value.latitude, currentLocation.value.longitude), 15f)

    mainLaunchedIsFinished = true
  }

  /**
   * Handles changes in the slider position. Updates the slider position and adjusts the map's
   * search radius accordingly. If the main process has finished, it sets the new radius and
   * triggers an update to listen for posts.
   *
   * @param scope The CoroutineScope in which to launch the coroutine.
   * @param newPosition The new position of the slider.
   */
  val mutex = Mutex()
  fun onSliderChanged(scope: CoroutineScope) {
    scope.launch {
      mutex.withLock {
        if (mainLaunchedIsFinished) {

          mapViewModel.setRadius(sliderPositionInMeters.toDouble() / 1000)
          mapViewModel.listenToPosts()
          delay(
              DELAY_CIRCLE_DISAPPEAR) //  a comment saying that the delay is needed for creating an
          // animation.
          isCircleShown = false
        }
      }
    }
  }

  /**
   * List of markers to display on the map. Maps each post to a PostMarker object containing the
   * post and its marker state.
   */
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
              val latLng = LatLng(currentLocation.value.latitude, currentLocation.value.longitude)
              val doubleSliderPosition = sliderPositionInMeters.toDouble()
              CircleOnMap(
                  center = latLng,
                  radius = doubleSliderPosition,
                  strokeColor = PrimaryPurple,
                  visible = isCircleShown)
              CircleOnMap(
                  center = latLng,
                  radius = doubleSliderPosition + 10,
                  strokeColor = Color.Black.copy(alpha = 0.2f),
                  visible = isCircleShown)

              Marker(listMarker) { post -> selectedPost = post }
            }

        SliderView(
            sliderPosition = sliderPositionInMeters,
            onSliderChanged = ::onSliderChanged,
            updateSliderPosition = { sliderPositionInMeters = it },
            isCircleShown = { isCircleShown = it })

        selectedPost?.let { post ->
          Box(modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.BottomCenter)) {
            PostCard(post)
          }
        }
      }
}

@Composable
@GoogleMapComposable
fun CircleOnMap(center: LatLng, radius: Double, strokeColor: Color, visible: Boolean) {
  Circle(center = center, radius = radius, strokeColor = strokeColor, visible = visible)
}

/**
 * Composable function to display a list of markers on a Google Map. Each marker represents a post
 * and can be clicked to select the post.
 *
 * This function iterates over the list of PostMarker objects, creating a Marker for each post. When
 * a marker is clicked, it sets the selected post.
 *
 * @param listMarker The list of PostMarker objects to be displayed on the map.
 * @param selectedPost A lambda function that sets the selected post when a marker is clicked.
 */
@Composable
@GoogleMapComposable
fun Marker(listMarker: List<PostMarker>, selectedPost: (Post) -> Unit) {
  listMarker.forEach { postMarker ->
    Marker(
        state = postMarker.markerState,
        contentDescription = postMarker.post.description,
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
        onClick = {
          selectedPost(postMarker.post)
          true
        })
  }
}

/**
 * Composable function to display a slider for adjusting a value. The slider is used to change the
 * radius of the map search area.
 *
 * This function creates a row containing a slider. The slider's value changes are handled
 * asynchronously with a coroutine scope. When the value changes, it updates the slider position and
 * shows or hides a circle on the map.
 *
 * @param sliderPosition The current position of the slider.
 * @param onSliderChanged A lambda function to handle changes in the slider value, accepting a
 *   coroutine scope and the new slider position.
 * @param isCircleShown A lambda function to control the visibility of the circle on the map.
 */
@Composable
fun SliderView(
    sliderPosition: Float,
    onSliderChanged: (CoroutineScope) -> Unit,
    updateSliderPosition: (Float) -> Unit,
    isCircleShown: (Boolean) -> Unit
) {
  val scope = rememberCoroutineScope()
  Row(modifier = Modifier.padding(end = 64.dp)) {
    Slider(
        value = sliderPosition,
        onValueChange = {
          isCircleShown(true)
          updateSliderPosition(it)
        },
        valueRange = MIN_SIZE_RAYON..MAX_SIZE_RAYON,
        modifier = Modifier.align(Alignment.CenterVertically).widthIn(10.dp, 200.dp),
        onValueChangeFinished = { onSliderChanged(scope) })
  }
}

data class PostMarker(val post: Post, val markerState: MarkerState)
