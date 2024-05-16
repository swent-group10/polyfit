package com.github.se.polyfit.ui.screen

//import the colors PrimaryPink
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.ui.components.showToastMessage
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.viewmodel.map.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

// Size in meters of the rayon of the circle to see other posts
val MIN_SIZE_RAYON = 100f
val MAX_SIZE_RAYON = 2000f

@Composable
fun MapScreen(paddingValues: PaddingValues, mapviewMap: MapViewModel = hiltViewModel()) {

    val posts = remember { mutableStateOf(listOf<Post>()) }
    val currentLocation = remember { mutableStateOf(Location.default()) }

    var isCircleShown by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(46.5181, 6.5659), 15f)
    }
    var sliderPosition by remember { mutableFloatStateOf(MIN_SIZE_RAYON) }

    LaunchedEffect(currentLocation) {
        mapviewMap.setLocation(
            currentLocation.value
        )
        mapviewMap.setRadius(500000.0)
        mapviewMap.listenToPosts()
        delay(10000) // delay for 3 seconds00) // delay for 3 seconds
        posts.value = mapviewMap._posts.value!!
        isCircleShown = true
        delay(1500) // delay for 3 seconds
        isCircleShown = false


    }

    LaunchedEffect(key1 = mapviewMap) {
//        mapviewMap.getAllPost().collect { posts.value = it }
        Log.i("MapScreen", "MapScreen $posts")
        
        currentLocation.value = mapviewMap.getCurrentLocation()
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            LatLng(currentLocation.value.latitude, currentLocation.value.longitude),
            15f
        )
    }

    val m1 = MarkerState(LatLng(46.5181, 6.5659))
    val m2 = MarkerState(LatLng(46.5183, 6.56))
    val m3 = MarkerState(LatLng(46.514, 6.566))
    val listMarker = remember(posts.value) {
        mutableStateListOf<MarkerState>().apply {
            posts.value?.map { post ->
                add(MarkerState(LatLng(post.location.latitude, post.location.longitude)))
            }

            Log.d("MapScreenMarker", "listMarker ${this.toList()}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.BottomStart
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
//            properties = MapProperties(isMyLocationEnabled = true)
        ) {
            Circle(
                center = LatLng(currentLocation.value.latitude, currentLocation.value.longitude),
                radius = (sliderPosition).toDouble() + 10, // Make the shadow circle slightly larger
                strokeColor = Color.Black.copy(alpha = 0.2f) // Use a semi-transparent color for the shadow
                , visible = isCircleShown
            )
            Circle(
                center = LatLng(currentLocation.value.latitude, currentLocation.value.longitude),
                radius = (sliderPosition).toDouble(),
                strokeColor = PrimaryPurple,
                visible = isCircleShown
            )
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
                onClick = showToastMessage(LocalContext.current), modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Menu, "Go settings")
            }

            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = MIN_SIZE_RAYON..MAX_SIZE_RAYON,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .widthIn(10.dp, 200.dp)
            )
        }
    }
}

fun goToMarker(marker: MarkerState): Boolean {
    // TODO
    Log.i("MapScreen", "goToMarker $marker")

    return true
}
