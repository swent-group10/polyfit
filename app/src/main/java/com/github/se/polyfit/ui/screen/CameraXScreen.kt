package com.github.se.polyfit.ui.screen

import android.content.pm.PackageManager
import android.hardware.Camera
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.ml.ImageAnalyserBarCode
import com.github.se.polyfit.ui.theme.getGradient
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import com.github.se.polyfit.viewmodel.qrCode.getCameraProvider

@Composable
fun CameraXScreen(
    barCodeCodeViewModel: BarCodeCodeViewModel = hiltViewModel(),
    padding: PaddingValues
) {

  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val preview = Preview.Builder().build()
  val cameraxSelector = CameraSelector.Builder().build()
  val previewView = remember { PreviewView(context) }
  val imageCapture = remember { ImageCapture.Builder().build() }

  val imageAnalysis =
      ImageAnalysis.Builder()
          .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
          .build()

  val imageAnalyserBarCode = ImageAnalyserBarCode(barCodeCodeViewModel::addId)

  imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), imageAnalyserBarCode)

  val isScanned by barCodeCodeViewModel.isScanned.observeAsState()

  val acceptedBrush = getGradient(active = true)
  val neutralBrush = getGradient(active = false)

  var brush by remember { mutableStateOf(neutralBrush) }
  LaunchedEffect(isScanned) {
    Log.i("CameraXScreen", "isScanned: $isScanned")
    brush = if (isScanned == true) acceptedBrush else neutralBrush
  }

  Box(
      modifier = Modifier.fillMaxSize().testTag("BoxCamera"),
      contentAlignment = Alignment.TopCenter) {
        Card(
            modifier =
                Modifier.fillMaxWidth(0.9f)
                    .height(120.dp)
                    .absoluteOffset(0.dp, padding.calculateTopPadding())
                    .testTag("CardCamera"),
            border = BorderStroke(8.dp, brush)) {
              AndroidView({ previewView }, modifier = Modifier.testTag("AndroidView").fillMaxSize())
            }
      }

  // If there is no camera we display nothing
  val pm: PackageManager = context.getPackageManager()
  val numberOfCameras = Camera.getNumberOfCameras()
  val hasCamera = numberOfCameras > 0 && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
  if (!hasCamera) {
    Log.e("CameraXScreen", "No camera found")
    return
  }

  LaunchedEffect(Unit) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner, cameraxSelector, preview, imageCapture, imageAnalysis)
    preview.setSurfaceProvider(previewView.surfaceProvider)
  }
}
