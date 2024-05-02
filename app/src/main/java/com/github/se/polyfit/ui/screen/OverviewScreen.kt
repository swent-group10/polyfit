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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.yml.charts.ui.linechart.LineChart
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.GradientButton
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.dialog.PictureDialog
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.ui.viewModel.DisplayScreen
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.github.se.polyfit.viewmodel.meal.OverviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

data class Meal(val name: String, val calories: Int)

@Composable
fun MealTrackerCard(
    caloriesGoal: Int,
    meals: List<Pair<MealOccasion, Double>>,
    onCreateMealFromPhoto: () -> Unit,
    onCreateMealWithoutPhoto: () -> Unit,
    onViewRecap: () -> Unit,
) {
  val context = LocalContext.current

  GradientBox(outerModifier = Modifier.testTag(OverviewTags.overviewMain)) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("MealColumn")) {
      Text(
          text = context.getString(R.string.main_card_title),
          fontSize = MaterialTheme.typography.headlineMedium.fontSize,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.testTag(OverviewTags.overviewGoal))
      Spacer(modifier = Modifier.height(8.dp))
      Row {
        val totalCalories = meals.sumOf { it.second }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag(OverviewTags.overviewCalorie)) {
              Text(
                  text = "$totalCalories/",
                  fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.testTag("Number"))
              Text(
                  text = "$caloriesGoal",
                  fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.align(Alignment.Bottom).testTag("Goal"))
            }
        Spacer(modifier = Modifier.width(5.dp)) // Fixed line
        Column(modifier = Modifier.align(Alignment.CenterVertically).testTag("CaloriePerMeal")) {
          meals.forEach { (meal, calories) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                  Text(
                      meal.toLowerCaseString(),
                      fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                      color = MaterialTheme.colorScheme.secondary)
                  Text(
                      "${calories.toInt()}",
                      fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                      color = MaterialTheme.colorScheme.secondary)
                }
          }
        }
      }
      Text(
          text = context.getString(R.string.meal_tracking_headline),
          fontSize = MaterialTheme.typography.headlineSmall.fontSize,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.testTag(OverviewTags.overviewTrack))
      Spacer(modifier = Modifier.height(16.dp))
      Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround) {
            GradientButton(
                onClick = onCreateMealFromPhoto,
                modifier = Modifier.testTag(OverviewTags.overviewPictureBtn),
                active = true,
                icon = {
                  Icon(
                      painterResource(R.drawable.outline_photo_camera),
                      contentDescription = "photoIcon",
                      tint = MaterialTheme.colorScheme.primary)
                })
            GradientButton(
                onClick = onCreateMealWithoutPhoto,
                modifier = Modifier.testTag(OverviewTags.overviewManualBtn),
                active = true,
                icon = {
                  Icon(
                      painterResource(R.drawable.baseline_mode_edit_outline),
                      contentDescription = "penIcon",
                      tint = MaterialTheme.colorScheme.primary)
                })
            GradientButton(
                onClick = onViewRecap,
                modifier = Modifier.testTag(OverviewTags.overviewDetailsBtn),
                active = true,
                icon = {
                  Icon(
                      painterResource(R.drawable.outline_assignment),
                      contentDescription = "historyIcon",
                      tint = MaterialTheme.colorScheme.primary)
                })
          }
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
fun OverviewScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    overviewViewModel: OverviewViewModel = hiltViewModel()
) {

  val context = LocalContext.current
  val navigation = Navigation(navController)
  var showPictureDialog by remember { mutableStateOf(false) }
  val isTestEnvironment = System.getProperty("isTestEnvironment") == "true"

  // State to hold the URI, the image and the bitmap
  var imageUri by remember { mutableStateOf<Uri?>(null) }
  val iconExample = BitmapFactory.decodeResource(context.resources, R.drawable.picture_example)
  var imageBitmap by remember { mutableStateOf(iconExample) }

  // Launcher for starting the camera activity
  val startCamera =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
        ->
        val bitmap = result.data?.extras?.get("data") as? Bitmap
        imageBitmap = bitmap

        showPictureDialog = false
        var id: Long? = runBlocking(Dispatchers.IO) { overviewViewModel.storeMeal(imageBitmap) }
        navigation.navigateToAddMeal(id)
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
            // Handle the exception if the camera intent cannot be launched
          }
        } else {
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

  if (showPictureDialog) {
    PictureDialog(
        onDismiss = { showPictureDialog = false },
        onFirstButtonClick = callCamera(context, startCamera, requestPermissionLauncher),
        onSecondButtonClick = { pickImageLauncher.launch("image/*") },
        firstButtonName = context.getString(R.string.take_picture_dialog),
        secondButtonName = context.getString(R.string.import_picture_dialog))
  }

  Box(modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("OverviewScreen")) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 20.dp),
        modifier = Modifier.testTag("OverviewScreenLazyColumn")) {
          item {
            Text(
                text = context.getString(R.string.welcome_message),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag(OverviewTags.overviewWelcome))
          }
          item {
            MealTrackerCard(
                caloriesGoal = 2200,
                meals =
                    listOf(
                        Pair(MealOccasion.BREAKFAST, 300.0),
                        Pair(MealOccasion.LUNCH, 456.0),
                        Pair(MealOccasion.DINNER, 0.0)),
                onCreateMealFromPhoto = {
                  showPictureDialog = true
                  Log.d("OverviewScreen", "Photo button clicked")
                },
                onCreateMealWithoutPhoto = navigation::navigateToAddMeal,
                onViewRecap = navigation::navigateToDailyRecap)
            PrimaryButton(
                text = "Create a Post",
                onClick = navigation::navigateToCreatePost,
                modifier = Modifier.padding(top = 8.dp).testTag("CreateAPost"))
          }
          item {
            OutlinedCard(
                modifier =
                    Modifier.align(Alignment.Center)
                        .padding(top = 10.dp)
                        .size(width = 350.dp, height = 300.dp)
                        .testTag("SecondCard"),
                border =
                    BorderStroke(
                        2.dp,
                        brush =
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.inversePrimary,
                                    MaterialTheme.colorScheme.primary))),
                colors = CardDefaults.cardColors(Color.Transparent)) {
                  imageBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Captured image",
                        modifier = Modifier.testTag("GenericPicture"))
                  }
                }
          }
          item {
            OutlinedCard(
                modifier =
                    Modifier.align(Alignment.Center)
                        .padding(top = 10.dp)
                        .size(width = 350.dp, height = 300.dp)
                        .testTag("Graph Card")
                        .clickable { navigation.navigateToGraph() },
                border =
                    BorderStroke(
                        2.dp,
                        brush =
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.inversePrimary,
                                    MaterialTheme.colorScheme.primary))),
                colors = CardDefaults.cardColors(Color.Transparent)) {
                  Column(modifier = Modifier.fillMaxSize().testTag("Graph Card Column")) {
                    Text(
                        text = "Calories Graph",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier =
                            Modifier.padding(start = 10.dp, top = 10.dp)
                                .weight(1f)
                                .testTag("Graph Card Title")
                                .clickable { navigation.navigateToGraph() })
                    Box(
                        modifier =
                            Modifier.fillMaxSize(0.85f)
                                .align(Alignment.CenterHorizontally)
                                .testTag("Graph Box")) {
                          if (!isTestEnvironment) {
                            LineChart(
                                modifier = Modifier.testTag("Overview Line Chart").fillMaxSize(),
                                lineChartData =
                                    lineChartData(
                                        hiltViewModel<GraphViewModel>().DataPoints(),
                                        hiltViewModel<GraphViewModel>().DateList(),
                                        DisplayScreen.OVERVIEW))
                          } else {
                            Spacer(modifier = Modifier.fillMaxSize().testTag("LineChartSpacer"))
                          }
                        }
                  }
                }
          }
        }
  }
}
