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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview as Preview1
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.ml.ImageAnalyserBarCode
import com.github.se.polyfit.ui.theme.getGradient
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import com.github.se.polyfit.viewmodel.qrCode.getCameraProvider

@Preview1
@Composable
fun CameraXScreen(barCodeCodeViewModel: BarCodeCodeViewModel = hiltViewModel()) {

  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val pm: PackageManager = context.getPackageManager()
  val numberOfCameras = Camera.getNumberOfCameras()
  val hasCamera = numberOfCameras > 0 && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)

  if (!hasCamera) {
    Log.e("CameraXScreen", "No camera found")
    return
  }

  val lensFacing = CameraSelector.LENS_FACING_BACK
  val preview = Preview.Builder().build()
  val previewView = remember { PreviewView(context) }
  val cameraxSelector = CameraSelector.Builder().build()
  val imageCapture = remember { ImageCapture.Builder().build() }

  val imageAnalysis =
      ImageAnalysis.Builder()
          .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
          .build()

  val imageAnalyserBarCode = ImageAnalyserBarCode(barCodeCodeViewModel::addId)

  imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), imageAnalyserBarCode)

  LaunchedEffect(lensFacing) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner, cameraxSelector, preview, imageCapture, imageAnalysis)
    preview.setSurfaceProvider(previewView.surfaceProvider)
  }

  Box(
      modifier = Modifier.fillMaxWidth().height(30.dp).testTag("BoxCamera"),
      contentAlignment = Alignment.Center) {
        AndroidView(
            { previewView },
            modifier =
                Modifier.border(BorderStroke(1.dp, getGradient(active = true)), RectangleShape)
                    .fillMaxSize(0.9f)
                    .testTag("preview"))
      }
}
