package com.github.se.polyfit.model.post

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.github.se.polyfit.model.post.Location as ourLocation
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class PostLocationModel(private val context: Context) {

  suspend fun getCurrentLocation(locationRequest: CurrentLocationRequest): ourLocation {
    return checkLocationPermissions(locationRequest)
  }

  suspend fun currentLocation(
      locationRequest: CurrentLocationRequest,
      query: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
  ): ourLocation {
    var locationToSet = ourLocation.default()

    try {
      Log.i("PostLocationModel", "Getting current location")
      val location: Location? = query.getCurrentLocation(locationRequest, null).await()
      Log.i("PostLocationModel", "Location: $location")

      locationToSet =
          ourLocation(
              longitude = location!!.longitude,
              latitude = location.latitude,
              altitude = location.altitude,
              name = "")
    } catch (e: SecurityException) {
      Toast.makeText(context, "Location permissions are missing", Toast.LENGTH_SHORT).show()
    }

    return locationToSet
  }

  suspend fun checkLocationPermissions(locationRequest: CurrentLocationRequest): ourLocation {

    if (ActivityCompat.checkSelfPermission(
        this.context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this.context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      Log.i("LocationPermissionsModel", "Location permissions not granted")

      return ourLocation.default()
    }

    return currentLocation(locationRequest)
  }
}
