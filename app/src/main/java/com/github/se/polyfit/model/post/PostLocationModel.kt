package com.github.se.polyfit.model.post

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.KeyCharacterMap.UnavailableException
import android.widget.Toast
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

class PostLocationModel(
    val context: Context,
    val success: (com.github.se.polyfit.model.post.Location) -> Unit
) : Activity() {
  private var fusedLocationClient: FusedLocationProviderClient =
      LocationServices.getFusedLocationProviderClient(context)

  private val currentLocationRequest =
      CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

  private val permission = LocationPermissionsModel(context)

  fun getCurrentLocation() {
    permission.checkLocationPermissions(this) { currentLocation() }
  }

  fun currentLocation() {

    try {
      val locationTask: Task<Location> =
          fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
      locationTask.addOnSuccessListener { location: Location? ->
        location?.let {
          Log.d("Location", "Location: $location")
          val locationToSet =
              Location(
                  longitude = it.longitude,
                  latitude = it.latitude,
                  altitude = it.altitude,
                  name = "")
          success(locationToSet)
        }
      }

      locationTask.addOnFailureListener { exception ->
        when (exception) {
          is SecurityException -> {
            Toast.makeText(context, "Location permissions are missing", Toast.LENGTH_SHORT).show()
          }
          is UnavailableException -> {
            Toast.makeText(context, "Location services are disabled", Toast.LENGTH_SHORT).show()
          }
          else -> {
            Log.e("Location", "Error getting current location", exception)
            Toast.makeText(context, "Unable to get current location", Toast.LENGTH_SHORT).show()
          }
        }
      }
    } catch (e: SecurityException) {
      Toast.makeText(context, "Location permissions are missing", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
  ) {
    if (requestCode == permission.getRequestLocationPermissionValue()) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        currentLocation()
      } else {
        Toast.makeText(context, "Permission has been denied", Toast.LENGTH_SHORT).show()
      }
    }
  }
}
