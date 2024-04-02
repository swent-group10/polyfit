package com.github.se.polyfit.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.compose.kaiseiFont

@Composable
@Preview
fun OverviewScreen() {
  // Context is used to launch the intent from the current Composable


  Scaffold(
      modifier = Modifier,
      topBar = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
          Title(modifier = Modifier, 35.sp)
        }
      }
  ) { paddingValues -> OverviewContent(paddingValues) }
}

@Composable
fun Title(modifier: Modifier, fontSize: TextUnit) {
  val shape = RoundedCornerShape(35)

  Box(
      modifier =
          Modifier.clip(shape)
              .background(color = MaterialTheme.colorScheme.background)
              .padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Polyfit",
            fontSize = fontSize,
            modifier = modifier,
            fontWeight = FontWeight.Bold,
            fontFamily = kaiseiFont)
      }
}

@Composable
fun CalorieCardContent(onPhoto : () -> Unit) {

  Box(modifier = Modifier.fillMaxSize()) {
    Text(
        text = "Calories Goal",
        modifier = Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 10.dp),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray)
    Row(modifier = Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 50.dp)) {
      Text(
          text = "756",
          fontSize = 30.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary)

      Text(
          text = "/",
          fontSize = 30.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary)

      Text(
          text = "2200",
          fontSize = 30.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary)
    }

    Column(modifier = Modifier.align(Alignment.TopEnd).padding(end = 90.dp, top = 50.dp)) {
      Text(
          text = "Breakfast",
          color = colorResource(R.color.purple_200),
          fontWeight = FontWeight.Bold)
      Text(text = "Lunch", color = colorResource(R.color.purple_200), fontWeight = FontWeight.Bold)
      Text(text = "Dinner", color = colorResource(R.color.purple_200), fontWeight = FontWeight.Bold)
    }
    Text(
        text = "Track your meals",
        modifier = Modifier.align(Alignment.CenterStart).padding(top = 30.dp, start = 10.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.secondary)

    Button(
        onClick =  onPhoto,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        colors =
            ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier =
            Modifier.align(Alignment.BottomStart)
                .padding(start = 25.dp, bottom = 15.dp)
                .background(MaterialTheme.colorScheme.background)) {
          Icon(
              painter = painterResource(R.drawable.outline_photo_camera_24),
              contentDescription = null)
        }

    Button(
        onClick = { /*TODO*/},
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        colors =
            ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier =
            Modifier.align(Alignment.BottomStart)
                .padding(start = 130.dp, bottom = 15.dp)
                .background(Color.Transparent)) {
          Icon(
              painter = painterResource(R.drawable.baseline_mode_edit_outline_24),
              contentDescription = null)
        }

    Button(
        onClick = { /*TODO*/},
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
        colors =
            ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier =
            Modifier.align(Alignment.BottomStart)
                .padding(start = 235.dp, bottom = 15.dp)
                .background(Color.Transparent)) {
          Icon(
              painter = painterResource(R.drawable.outline_assignment_24),
              contentDescription = null)
        }
  }
}

private fun callCamera(
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

@Composable
fun OverviewContent(paddingValues: PaddingValues) {

    val context = LocalContext.current

    // State to hold the URI, the image and the bitmap
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val iconExample = BitmapFactory.decodeResource(context.resources, R.drawable.picture_example)
    var imageBitmap by remember { mutableStateOf<Bitmap?>(iconExample) }

    // Launcher for starting the camera activity
    val startCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
            ->
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap = bitmap
        }

    // Launcher for requesting the camera permission
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
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



    // Create a launcher to open gallery
    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri?
            ->
            uri?.let {
                imageUri = uri // Update the UI with the selected image URI

                try {
                    val bitmap = ImageDecoder.decodeBitmap(createSource(context.contentResolver, uri))
                    imageBitmap = bitmap
                } catch (e: Exception) {
                    Log.e(
                        "OverviewScreen",
                        "Error decoding image: $e," + " are you sure the image is a bitmap?")
                }
            }
        }

    var showPictureDialog by remember {mutableStateOf(false)}

    if(showPictureDialog){
        PictureDialog(
            onDismiss = {showPictureDialog = false},
            onTakePic = callCamera(context, startCamera, requestPermissionLauncher),
            onImportPic = {pickImageLauncher.launch("image/*")}
        )
    }


    Box(modifier = Modifier.padding(paddingValues).fillMaxWidth()) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 30.dp, vertical = 20.dp)) {
      item {
        Text(
            text = "Welcome Back, User432!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
      }
      item {
        OutlinedCard(
            modifier = Modifier.align(Alignment.Center).size(width = 350.dp, height = 210.dp),
            border =
                BorderStroke(
                    2.dp,
                    brush =
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                MaterialTheme.colorScheme.primary))),
            colors = CardDefaults.cardColors(Color.Transparent)) {
              CalorieCardContent(){showPictureDialog = true}
            }
      }
      item {
        OutlinedCard(
            modifier =
                Modifier.align(Alignment.Center)
                    .padding(top = 10.dp)
                    .size(width = 350.dp, height = 300.dp),
            border =
                BorderStroke(
                    2.dp,
                    brush =
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                MaterialTheme.colorScheme.primary))),
            colors = CardDefaults.cardColors(Color.Transparent)) {

              //



              imageBitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "Captured image")
              }
            }
      }
      item {
        OutlinedCard(
            modifier =
                Modifier.align(Alignment.Center)
                    .padding(top = 10.dp)
                    .size(width = 350.dp, height = 200.dp),
            border =
                BorderStroke(
                    2.dp,
                    brush =
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                MaterialTheme.colorScheme.primary))),
            colors = CardDefaults.cardColors(Color.Transparent)) {}
      }
    }
  }
}
