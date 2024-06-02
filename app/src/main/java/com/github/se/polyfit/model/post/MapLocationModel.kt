package com.github.se.polyfit.model.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

class MapLocationModel(val context: Context) : Activity() {
  private var fusedLocationClient: FusedLocationProviderClient =
      LocationServices.getFusedLocationProviderClient(context)

  private val currentLocationRequest =
      CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

  private val locationRequestCompat: LocationRequest = LocationRequest.Builder(10000).build()

  private var locationCallback: LocationCallback? = null

  private var requestingLocationUpdates = false

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

  private fun startLocationUpdates() {

    val callBack =
        object : LocationCallback() {
          override fun onLocationResult(locationResult: LocationResult) {
            // Handle the new location
            val location = locationResult.lastLocation
            // Do something with the location, such as update the UI
          }
        }

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      fusedLocationClient.requestLocationUpdates(
          locationRequestCompat, callBack, Looper.getMainLooper())

      locationCallback = callBack
      return
    }
  }

  private fun checkLocationPermissions(locationFunction: () -> Unit) {
    if (ActivityCompat.checkSelfPermission(
        this.context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            this.context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      startLocationUpdates()
    } else {
      locationFunction()
    }
  }
}
