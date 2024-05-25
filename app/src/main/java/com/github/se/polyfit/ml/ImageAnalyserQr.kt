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

class ImageAnalyserQr(val addMeal: (id: String?) -> Unit) : ImageAnalysis.Analyzer {

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
                  addMeal(barcode.displayValue)
                  barcode.displayValue?.let {barcodeId ->
                    if (barcodeId.isNotEmpty()){
                      Log.d("Barcode", "Value: $barcodeId")

                    }
                  }
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