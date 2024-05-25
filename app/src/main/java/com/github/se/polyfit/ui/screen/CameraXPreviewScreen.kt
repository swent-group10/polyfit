package com.github.se.polyfit.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.ui.tooling.preview.Preview as Preview1

@Preview1
@Composable
fun CameraPreviewScreen() {

  val lensFacing = CameraSelector.LENS_FACING_BACK
  val lifecycleOwner = LocalLifecycleOwner.current
  val context = LocalContext.current
  val preview = Preview.Builder().build()
  val previewView = remember {
    PreviewView(context)
  }
  val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
  val imageCapture = remember {
    ImageCapture.Builder().build()
  }


  val imageAnalysis = ImageAnalysis.Builder()
          .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
          .build()


  // Or, to specify the formats to recognize:
  // val scanner = BarcodeScanning.getClient(options)

  val yourImageAnalyzer = YourImageAnalyzer()

  imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), yourImageAnalyzer)


  LaunchedEffect(lensFacing) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture, imageAnalysis)
    preview.setSurfaceProvider(previewView.surfaceProvider)
  }



  AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
}





private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
      ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener(
            { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
      }
    }


private class YourImageAnalyzer : ImageAnalysis.Analyzer {

  val options = BarcodeScannerOptions.Builder()
          .setBarcodeFormats(
                  Barcode.FORMAT_CODE_128,
                  Barcode.FORMAT_CODE_39,
                  Barcode.FORMAT_CODE_93,
                  Barcode.FORMAT_CODABAR,
                  Barcode.FORMAT_EAN_13,
                  Barcode.FORMAT_EAN_8,
                  Barcode.FORMAT_ITF,
                  Barcode.FORMAT_UPC_A,
                  Barcode.FORMAT_UPC_E,
                  Barcode.FORMAT_PDF417,
                  Barcode.FORMAT_QR_CODE,
                  Barcode.FORMAT_AZTEC)
          .enableAllPotentialBarcodes()
          .build()

  val scanner = BarcodeScanning.getClient(options)

  @OptIn(ExperimentalGetImage::class)
  override fun analyze(imageProxy: ImageProxy) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
      val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
      // Pass image to an ML Kit Vision API

      scanner.process(image)
              .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                  Log.d("Barcode", "Value: ${barcode.displayValue}")
                }
              }
              .addOnFailureListener {
                Log.e("Barcode", "Error processing image", it)
              }
              .addOnCompleteListener {
                imageProxy.close()
              }
    }
  }
}