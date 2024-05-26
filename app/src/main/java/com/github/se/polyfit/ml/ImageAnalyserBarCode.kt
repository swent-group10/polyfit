package com.github.se.polyfit.ml

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class ImageAnalyserBarCode(val addMeal: (id: String?) -> Unit) : ImageAnalysis.Analyzer {

  private val options =
      BarcodeScannerOptions.Builder()
          .setBarcodeFormats(
              Barcode.FORMAT_EAN_8,
              Barcode.FORMAT_EAN_13,
              Barcode.FORMAT_UPC_A,
              Barcode.FORMAT_UPC_E,
          )
          .build()

  private val scanner = BarcodeScanning.getClient(options)

  @OptIn(ExperimentalGetImage::class)
  override fun analyze(imageProxy: ImageProxy) {
    Log.i("ImageAnalyser", "Image received1")
    val mediaImage = imageProxy.getImage()
    Log.i("ImageAnalyser", "Image received2 $mediaImage")
    if (mediaImage != null) {
      val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
      Log.i("ImageAnalyser", "Image received3")
      // Pass image to the ML Kit Vision API
      scanner
          .process(image)
          .addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
              Log.i("ImageAnalyser", "format ${barcode.format} value ${barcode.displayValue}")
              addMeal(barcode.displayValue)
            }
          }
          .addOnFailureListener { Log.e("ImageAnalyser", "Error processing image", it) }
          .addOnCompleteListener { imageProxy.close() }
    }
    Log.i("ImageAnalyser", "Image end")
  }
}
