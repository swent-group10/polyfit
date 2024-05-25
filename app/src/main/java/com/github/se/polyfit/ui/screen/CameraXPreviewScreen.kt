package com.github.se.polyfit.ui.screen

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
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

  val qrCodeAnalyzer = QrCodeAnalyzer { qrCodes ->
    qrCodes.forEach {
      Log.d("CameraPreviewScreen", "qrCode: ${it.displayValue}")
    }
  }

  imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), qrCodeAnalyzer)


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



class QrCodeAnalyzer(
        private val onQrCodesDetected: (qrCodes: List<FirebaseVisionBarcode>) -> Unit
) : ImageAnalysis.Analyzer {

  @OptIn(ExperimentalGetImage::class)
  override fun analyze(image: ImageProxy) {
    Log.i("QrCodeAnalyzer", "analyze")
    val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            // We want to only detect QR codes.
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build()

    val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)


    val rotation = image.imageInfo.rotationDegrees
    val visionImage = FirebaseVisionImage.fromMediaImage(image.image!!, rotation)

    detector.detectInImage(visionImage)
            .addOnSuccessListener { barcodes ->
              Log.d("QrCodeAnalyzer", "barcodes: $barcodes")
              onQrCodesDetected(barcodes)
            }
            .addOnFailureListener {
              Log.e("QrCodeAnalyzer", "something went wrong", it)
            }
            .addOnCompleteListener {
              image.close()
            }
  }
}
