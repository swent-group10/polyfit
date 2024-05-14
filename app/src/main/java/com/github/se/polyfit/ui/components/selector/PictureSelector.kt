package com.github.se.polyfit.ui.components.selector

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.se.polyfit.ui.theme.PurpleGrey80
import com.github.se.polyfit.viewmodel.post.CreatePostViewModel

private const val PERMISSION_DENIED_MESSAGE = "Permission denied"
private const val TAG = "PictureSelector"

@Composable
fun PictureSelector(modifier: Modifier = Modifier, postViewModel: CreatePostViewModel) {
  val context = LocalContext.current

  var bitmapPicture = remember { mutableStateOf<Bitmap?>(null) }

  val takePicturePreview =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.TakePicturePreview(),
          onResult = { bitmap: Bitmap? ->
            // Handle the bitmap, maybe show it in the UI or save it
            if (bitmap != null) {
              // Here you could update the UI or handle the bitmap further as needed
              Toast.makeText(context, "Bitmap captured", Toast.LENGTH_SHORT).show()
              bitmapPicture.value = bitmap
              postViewModel.setBitMap(bitmap)
              Log.d("PstNotSoting", "PictureSelector: ${bitmapPicture.value}")
            }
          })

  val requestPermission =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
          isGranted: Boolean ->
        if (isGranted) {
          takePicturePreview.launch(null) // No input needed, directly launches camera for a preview
        } else {
          Toast.makeText(context, PERMISSION_DENIED_MESSAGE, Toast.LENGTH_SHORT).show()
        }
      }

  Box(
      modifier =
          modifier.fillMaxWidth().testTag(TAG).clickable {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
              takePicturePreview.launch(null) // Launch the camera if permission is already granted
            } else {
              requestPermission.launch(
                  Manifest.permission.CAMERA) // Request permission if not already granted
            }
          },
      contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier.size(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = PurpleGrey80, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center) {
              if (bitmapPicture.value != null) {
                // Display the bitmap here
                Image(
                    bitmap = bitmapPicture.value!!.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
              } else {
                Text("Tap to take photo")
              }
            }
      }
}
