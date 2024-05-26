package com.github.se.polyfit.viewmodel.qrCode

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class BarCodeCodeViewModelTest {

  @get:Rule var rule: TestRule = InstantTaskExecutorRule()

  private val viewModel = BarCodeCodeViewModel()

  @BeforeTest
  fun setup() {
    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0
  }

  @AfterTest
  fun teardown() {
    unmockkAll()
  }

  @Test
  fun `addId adds id to list when id is valid and not in list`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    assertEquals(listOf("123456"), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id is null`() {
    viewModel.addId(null)
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id is already in list`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    assertEquals(listOf("123456"), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id length is not in 6 to 13`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345")
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId adds id to the top of the list when id is valid and not in list`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("789012")
    }
    assertEquals(listOf("789012", "123456"), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id is empty`() {

    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("")
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id length is more than 13`() {
    var s = ""
    for (i in 1..MAX_BARCODE_LENGTH + 1) {
      s += "1"
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId(s)
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id length is less than 6`() {
    var s = ""
    for (i in 1 ..< MIN_BARCODE_LENGTH) {
      s += "1"
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId(s)
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not added enough time`() {

    for (i in 1 ..< REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345678")
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId null and then add same value`() {

    for (i in 1 ..< REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345678")
    }
    viewModel.addId(null)
    viewModel.addId("12345678")
    assertEquals(listOf("12345678"), viewModel.listId.value)
  }
}

/*
class ContextExtensionsTest {

  private val context = mockk<Context>()
  private val cameraProvider = mockk<MockKMatcherScope.DynamicCall>()
  private val listenableFuture = mockk<ListenableFuture<ProcessCameraProvider>>(relaxed = true)

  @Before
  fun setup() {

    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0

    mockkStatic(ProcessCameraProvider::class)
    every { ProcessCameraProvider.getInstance(context) } returns listenableFuture
    every { cameraProvider.get(any()) } returns cameraProvider

    mockkStatic(CameraX::class)
  }

  @After
  fun teardown() {
    unmockkAll()
  }

  @Test
  fun `getCameraProvider returns camera provider`(){
    runBlocking {
      val result = context.getCameraProvider()
      Log.v("ContextExtensionsTest", "getCameraProvider: $result")
    }
  }
}
 */
