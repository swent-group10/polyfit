package com.github.se.polyfit.model.post

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import android.content.Context
import android.location.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority

class LocationModel(context: Context) {
  private var fusedLocationClient: FusedLocationProviderClient =
      LocationServices.getFusedLocationProviderClient(context)
  private val currentLocationRequest = CurrentLocationRequest.Builder()
      .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
      .build()

  fun getCurrentLocation() {
    try {
      val locationTask: Task<Location> =
          fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
      locationTask.addOnSuccessListener { location: Location? -> location?.let {} }

      locationTask.addOnFailureListener {
        // Handle possible failures, such as no location available
      }
    } catch (e: SecurityException) {
      // Handle case where location permissions are missing
    }
  }

  private fun checkLocationPermissions(activity: Activity) {
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      requestLocationPermissions(activity)
    } else {
      // Permission already granted, you can now access the user's location
    }
  }

  private fun requestLocationPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
        REQUEST_LOCATION_PERMISSION)
  }

  companion object {
    private const val REQUEST_LOCATION_PERMISSION = 1
  }
}
