package com.github.se.polyfit.ml

import android.graphics.ImageFormat
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import java.lang.Thread.sleep
import kotlin.Exception

class ImageAnalyserBarCodeTest {

  @org.junit.Test
  fun findTheBarCode() {

    // Mock the ImageProxy and Image
    val mockImageProxy = mockk<ImageProxy>(relaxed = true)
    val mockImage = mockk<Image>(relaxed = true)
    every { mockImageProxy.getImage() } returns mockImage
    every { mockImageProxy.imageInfo } returns mockk(relaxed = true)
    every { mockImageProxy.close() } returns Unit

    val inputImage = mockk<InputImage>(relaxed = true)
    every { inputImage.width } returns 2268
    every { inputImage.height } returns 4032
    every { inputImage.rotationDegrees } returns 0
    every { inputImage.format } returns ImageFormat.JPEG
    every { inputImage.planes } returns emptyArray()

    mockkStatic(InputImage::class)
    every { InputImage.fromMediaImage(any(), any()) } returns inputImage

    val mockImageInfo = mockk<ImageInfo>(relaxed = true)
    every { mockImageProxy.getImage() } returns mockImage
    every { mockImageProxy.imageInfo } returns mockImageInfo
    every { mockImageInfo.rotationDegrees } returns 0
    every { mockImage.format } returns ImageFormat.JPEG
    every { mockImage.height } returns 4032
    every { mockImage.width } returns 2268
    every { mockImage.close() } returns Unit
    every { mockImage.planes } returns emptyArray()

    val mockBarcode = mockk<Barcode>(relaxed = true)
    val barcodeList = listOf(mockBarcode) // Create a list of Barcodes
    val successfulTask = Tasks.forResult(barcodeList) // Create a successful Task

    mockkStatic(BarcodeScanning::class)
    every { BarcodeScanning.getClient(any()) } returns
        mockk<BarcodeScanner> { every { process(any<InputImage>()) } returns successfulTask }

    mockkStatic(InputImage::class)
    every { InputImage.fromMediaImage(any(), any()) } returns mockk()

    // Mock the Barcode
    every { mockBarcode.displayValue } returns "1234567890128"

    // Mock the addMeal function
    val addMeal: (String?) -> Unit = mockk(relaxed = true)

    // Create the ImageAnalyserBarCode
    val imageAnalyserBarCode = ImageAnalyserBarCode(addMeal)

    // Call analyze
    imageAnalyserBarCode.analyze(mockImageProxy)

    // Verify that addMeal was called with the correct barcode
    sleep(2000)
    verify { addMeal("1234567890128") }
  }

  @org.junit.Test
  fun doesntFindTheBarCode() {

    // Mock the ImageProxy and Image
    val mockImageProxy = mockk<ImageProxy>(relaxed = true)
    val mockImage = mockk<Image>(relaxed = true)
    val inputImage = mockk<InputImage>(relaxed = true)
    val mockImageInfo = mockk<ImageInfo>(relaxed = true)

    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0

    mockkStatic(InputImage::class)
    every { InputImage.fromMediaImage(any(), any()) } returns inputImage

    val exceptionTask =
        Tasks.forException<List<Barcode>>(Exception("Task failed")) // Create a failure Task

    mockkStatic(BarcodeScanning::class)
    every { BarcodeScanning.getClient(any()) } returns
        mockk<BarcodeScanner> { every { process(any<InputImage>()) } returns exceptionTask }

    mockkStatic(InputImage::class)
    every { InputImage.fromMediaImage(any(), any()) } returns mockk()

    every { mockImageProxy.getImage() } returns mockImage
    every { mockImageProxy.imageInfo } returns mockk(relaxed = true)
    every { mockImageProxy.close() } returns Unit
    every { mockImageProxy.imageInfo } returns mockImageInfo

    every { inputImage.width } returns 2268
    every { inputImage.height } returns 4032
    every { inputImage.rotationDegrees } returns 0
    every { inputImage.format } returns ImageFormat.JPEG
    every { inputImage.planes } returns emptyArray()

    every { mockImageInfo.rotationDegrees } returns 0

    every { mockImage.format } returns ImageFormat.JPEG
    every { mockImage.height } returns 4032
    every { mockImage.width } returns 2268
    every { mockImage.close() } returns Unit
    every { mockImage.planes } returns emptyArray()

    // Mock the addMeal function
    val addMeal: (String?) -> Unit = mockk(relaxed = true)

    // Create the ImageAnalyserBarCode
    val imageAnalyserBarCode = ImageAnalyserBarCode(addMeal)

    // Call analyze
    imageAnalyserBarCode.analyze(mockImageProxy)

    sleep(2000)
    // Verify that addMeal was called with the correct barcode
    verify(exactly = 0) { addMeal(any()) }
    verify { Log.e("ImageAnalyser", "Error processing image", any()) }
  }
}
