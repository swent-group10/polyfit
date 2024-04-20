package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.components.showToastMessage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Size in meters of the rayon of the circle to see other posts
val MIN_SIZE_RAYON = 100f
val MAX_SIZE_RAYON = 2000f

@Preview
@Composable
fun Map() {

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(LatLng(46.5181, 6.5659), 15f)
  }

  val m1 = MarkerState(LatLng(46.5181, 6.5659))
  val m2 = MarkerState(LatLng(46.5183, 6.56))
  val m3 = MarkerState(LatLng(46.514, 6.566))
  val listMarker = remember { mutableStateListOf<MarkerState>(m1, m2, m3) }
  var sliderPosition by remember { mutableFloatStateOf(MIN_SIZE_RAYON) }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
    GoogleMap(cameraPositionState = cameraPositionState) {
      Circle(
          center = LatLng(46.5181, 6.5659),
          radius = (sliderPosition).toDouble(),
          strokeColor = Color.Blue)
      listMarker.forEach { markerState ->
        Marker(
            state = markerState,
            title = "title",
            contentDescription = "description",
            onClick = { goToMarker(markerState) })
      }
    }
    Row(modifier = Modifier.padding(end = 64.dp)) {
      FloatingActionButton(
          onClick = showToastMessage(LocalContext.current), modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Filled.Menu, "Go settings")
          }

      Slider(
          value = sliderPosition,
          onValueChange = { sliderPosition = it },
          valueRange = MIN_SIZE_RAYON..MAX_SIZE_RAYON,
          modifier = Modifier.align(Alignment.CenterVertically).widthIn(10.dp, 200.dp))
    }
  }
}

fun goToMarker(marker: MarkerState): Boolean {
  // TODO
  Log.i("MapScreen", "goToMarker $marker")

  return true
}
