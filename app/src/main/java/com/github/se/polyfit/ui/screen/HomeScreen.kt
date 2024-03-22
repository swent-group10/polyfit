package com.github.se.polyfit.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.github.se.polyfit.R


@Preview
@Composable
fun HomeScreen() {
  Column {


    // Context is used to launch the intent from the current Composable
    val context = LocalContext.current
    val iconExample = BitmapFactory.decodeResource(context.resources, R.drawable.picture_example)

    var imageBitmap by remember { mutableStateOf<Bitmap?>(iconExample) }
    Text("Home Screen", modifier = Modifier.testTag("HomeScreen"))


    // Launcher for starting the camera activity
    val startCamera = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      val bitmap = result.data?.extras?.get("data") as? Bitmap
      imageBitmap = bitmap
    }

    // Launcher for requesting the camera permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
      ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
      if (isGranted) {
        // Permission is granted, you can start the camera
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
          startCamera.launch(takePictureIntent)
        } catch (e: Exception) {
          Log.e("HomeScreen", "Error launching camera intent: $e")
          // Handle the exception if the camera intent cannot be launched
        }
      } else {
        Log.e("HomeScreen", "Permission denied")
        // Permission is denied. Handle the denial appropriately.
      }
    }

    Button(onClick = call_camera(context, startCamera, requestPermissionLauncher)) {
      Text("Take a picture")
    }

    imageBitmap?.let {
      Image(bitmap = it.asImageBitmap(), contentDescription = "Captured image")
    }
  }
}

@Composable
private fun call_camera(
  context: Context,
  startCamera: ManagedActivityResultLauncher<Intent, ActivityResult>,
  requestPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>
): () -> Unit = {
  // Check if the permission has already been granted
  when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
    PackageManager.PERMISSION_GRANTED -> {
      // You can use the camera
      val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      try {
        startCamera.launch(takePictureIntent)
      } catch (e: Exception) {
        // Handle the exception if the camera intent cannot be launched
        Log.e("HomeScreen", "Error launching camera intent: $e")
      }
    }

    else -> {
      // Request the permission
      requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }
}
