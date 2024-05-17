package com.github.se.polyfit.ui.screen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.yml.charts.ui.linechart.LineChart
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.card.MealTrackerCard
import com.github.se.polyfit.ui.components.dialog.PictureDialog
import com.github.se.polyfit.ui.components.lineChartData
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.ui.viewModel.DisplayScreen
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.github.se.polyfit.viewmodel.meal.OverviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

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
        val id: String? = runBlocking(Dispatchers.IO) { overviewViewModel.storeMeal(imageBitmap) }
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
            Log.e("OverviewScreen", "Error decoding image: $e, are you sure the image is a bitmap?")
          }
        }
      }

  if (showPictureDialog) {
    PictureDialog(
        onDismiss = { showPictureDialog = false },
        onButtonsClick =
            listOf(
                overviewViewModel.callCamera(context, startCamera, requestPermissionLauncher),
                { pickImageLauncher.launch("image/*") },
                {}),
        buttonsName =
            listOf(
                context.getString(R.string.take_picture_dialog),
                context.getString(R.string.import_picture_dialog),
                context.getString(R.string.scan_picture_dialog)))
  }

  Box(modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("OverviewScreen")) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 20.dp),
        modifier = Modifier.testTag("OverviewScreenLazyColumn"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          item {
            Text(
                text = context.getString(R.string.welcome_message, overviewViewModel.getUserName()),
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 30.sp,
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
          }

          item {
            Box(
                modifier =
                    Modifier.align(Alignment.Center)
                        .padding(top = 10.dp)
                        .size(width = 350.dp, height = 300.dp)
                        .testTag("Graph Card")
                        .clickable { navigation.navigateToGraph() },
            ) {
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
                            .testTag("Graph Box")
                            .clickable { navigation.navigateToGraph() }) {
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
