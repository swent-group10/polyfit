package com.github.se.polyfit.ml

import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.R
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.Exception

class ImageProxyWrapper(private val image: Image?) : ImageProxy {

  override fun close() {
    image?.close()
  }

  override fun getCropRect(): Rect {
    TODO("Not yet implemented")
  }

  override fun setCropRect(rect: Rect?) {
    TODO("Not yet implemented")
  }

  override fun getFormat(): Int {
    return ImageFormat.JPEG
  }

  override fun getHeight(): Int {
    return 1920
  }

  override fun getWidth(): Int {
    return 1080
  }

  override fun getPlanes(): Array<ImageProxy.PlaneProxy> {
    return listOf(
            mockk<ImageProxy.PlaneProxy>(
                mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true)))
        .toTypedArray()
  }

  override fun getImageInfo(): ImageInfo {
    return mockk(relaxed = true)
  }

  override fun getImage(): Image? {
    Log.i("ImageAnalyser", "getImage $image")
    return image
  }
}

class ImageAnalyserBarCodeTest {

  @org.junit.Test
  fun findTheBarCode() {

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.barcode1)
    val image = InputImage.fromBitmap(bitmap, 0)

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
    every { InputImage.fromMediaImage(any(), any()) } returns image

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
    verify { addMeal("1234567890128") }
  }

  @org.junit.Test
  fun doesntFindTheBarCode() {

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.barcode1)
    val image = InputImage.fromBitmap(bitmap, 0)

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
    every { InputImage.fromMediaImage(any(), any()) } returns image

    val mockImageInfo = mockk<ImageInfo>(relaxed = true)
    every { mockImageProxy.getImage() } returns mockImage
    every { mockImageProxy.imageInfo } returns mockImageInfo
    every { mockImageInfo.rotationDegrees } returns 0
    every { mockImage.format } returns ImageFormat.JPEG
    every { mockImage.height } returns 4032
    every { mockImage.width } returns 2268
    every { mockImage.close() } returns Unit
    every { mockImage.planes } returns emptyArray()

    val exceptionTask =
        Tasks.forException<List<Barcode>>(Exception("Task failed")) // Create a failure Task

    mockkStatic(BarcodeScanning::class)
    every { BarcodeScanning.getClient(any()) } returns
        mockk<BarcodeScanner> { every { process(any<InputImage>()) } returns exceptionTask }

    mockkStatic(InputImage::class)
    every { InputImage.fromMediaImage(any(), any()) } returns mockk()

    // Mock the addMeal function
    val addMeal: (String?) -> Unit = mockk(relaxed = true)

    // Create the ImageAnalyserBarCode
    val imageAnalyserBarCode = ImageAnalyserBarCode(addMeal)

    // Call analyze
    imageAnalyserBarCode.analyze(mockImageProxy)

    // Verify that addMeal was called with the correct barcode
    verify(exactly = 0) { addMeal(any()) }
  }
}
