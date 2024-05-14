package com.github.se.polyfit.model.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class LocationPermissionsModel(val context: Context) {

  fun checkLocationPermissions(activity: Activity, locationFunction: () -> Unit) {
    if (ActivityCompat.checkSelfPermission(
        this.context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            this.context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      requestLocationPermissions(activity)
    } else {
      locationFunction()
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

  fun getRequestLocationPermissionValue(): Int {
    return REQUEST_LOCATION_PERMISSION
  }
}
