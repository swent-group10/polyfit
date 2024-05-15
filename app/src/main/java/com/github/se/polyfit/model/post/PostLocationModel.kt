package com.github.se.polyfit.model.post

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class PostLocationModel(private val context: Context) {

  private val currentLocationRequest =
      CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

  suspend fun getCurrentLocation(): com.github.se.polyfit.model.post.Location {
    return checkLocationPermissions()
  }

  suspend fun currentLocation(
      query: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
  ): com.github.se.polyfit.model.post.Location {
    var locationToSet: com.github.se.polyfit.model.post.Location =
        com.github.se.polyfit.model.post.Location.default()

    try {
      val location: Location? = query.getCurrentLocation(currentLocationRequest, null).await()

      locationToSet =
          location?.let {
            com.github.se.polyfit.model.post.Location(
                longitude = it.longitude, latitude = it.latitude, altitude = it.altitude, name = "")
          } ?: com.github.se.polyfit.model.post.Location.default()
    } catch (e: SecurityException) {
      Toast.makeText(context, "Location permissions are missing", Toast.LENGTH_SHORT).show()
    }

    return locationToSet
  }

  suspend fun checkLocationPermissions(): com.github.se.polyfit.model.post.Location {

    if (ActivityCompat.checkSelfPermission(
        this.context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            this.context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      Log.e("LocationPermissionsModel", "Location permissions not granted")

      return com.github.se.polyfit.model.post.Location.default()
    }

    return currentLocation()
  }
}
