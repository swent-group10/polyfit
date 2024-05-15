package com.github.se.polyfit.ui.components.selector

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.PurpleGrey80
import kotlin.reflect.KFunction0

@Composable
fun PictureSelector(
    modifier: Modifier = Modifier,
    getBitMap: KFunction0<Bitmap?>,
    setBitmap: (Bitmap?) -> Unit
) {
  val context = LocalContext.current

  var bitmapPicture by remember { mutableStateOf(getBitMap()) }

  val takePicturePreview =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.TakePicturePreview(),
          onResult = { bitmap: Bitmap? ->
            if (bitmap != null) {
              bitmapPicture = bitmap
              setBitmap(bitmap)
            }
          })

  val requestPermission =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
          isGranted: Boolean ->
        if (isGranted) {
          takePicturePreview.launch(null) // No input needed, directly launches camera for a preview
        } else {
          Toast.makeText(context, context.getString(R.string.PermissionDenied), Toast.LENGTH_SHORT)
              .show()
        }
      }

  Box(
      modifier = modifier.fillMaxWidth().testTag("PictureSelector"),
      contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier.size(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                      if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                          PackageManager.PERMISSION_GRANTED) {
                        takePicturePreview.launch(
                            null) // Launch the camera if permission is already granted
                      } else {
                        requestPermission.launch(
                            Manifest.permission.CAMERA) // Request permission if not already granted
                      }
                    }
                    .background(color = PurpleGrey80, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center) {
              if (bitmapPicture != null) {

                Image(
                    bitmap = bitmapPicture!!.asImageBitmap(),
                    contentDescription = context.getString(R.string.CreatePostImageDesription),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
                Box(
                    modifier = Modifier.align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center) {
                      FloatingActionButton(
                          onClick = {
                            bitmapPicture = null
                            setBitmap(null)
                          },
                          shape = MaterialTheme.shapes.small,
                          containerColor = PurpleGrey80,
                          contentColor = Color.White) {
                            Text("X", color = PrimaryPurple)
                          }
                    }
              } else {
                Text(context.getString(R.string.TakeToTakePicture), color = PrimaryPurple)
              }
            }
      }
}
