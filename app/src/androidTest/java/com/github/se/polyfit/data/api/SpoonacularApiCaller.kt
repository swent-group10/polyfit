import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

class SpoonacularApiCallerTest {

  private lateinit var mockWebServer: MockWebServer

  @Before
  fun setUp() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
  }

  //    @Test
  //    fun `searchRecipe returns expected result`() {
  //        // Arrange
  //        val expectedResponse = "{\"recipes\":[]}"
  //        mockWebServer.enqueue(MockResponse().setBody(expectedResponse))
  //
  //        // Act
  //        val actualResponse = SpoonacularApiCaller.searchRecipe("pasta")
  //
  //        // Assert
  //        assertEquals(expectedResponse, actualResponse)
  //    }
  //    @Test
  //    fun test_actual_request() {
  //        val response = SpoonacularApiCaller.searchRecipe("pasta")
  //        Log.e(
  //            "SpoonacularApiCallerTest",
  //            "test actual request: response = $response"
  //        )
  //        println(response)
  //    }

  //    @Test
  //    fun imageClassificationTest() {
  //        val context = InstrumentationRegistry.getInstrumentation().targetContext
  //
  //        // Load the drawable
  //        val drawable = ResourcesCompat.getDrawable(
  //            context.resources,
  //            com.github.se.polyfit.test.R.drawable.cheesecake,
  //            null
  //        )
  //
  //        // Create a bitmap
  //        val bitmap = Bitmap.createBitmap(
  //            drawable!!.intrinsicWidth,
  //            drawable.intrinsicHeight,
  //            Bitmap.Config.ARGB_8888
  //        )
  //
  //        // Create a canvas to draw the drawable onto
  //        val canvas = Canvas(bitmap)
  //
  //        // Draw the drawable onto the canvas
  //        drawable.setBounds(0, 0, canvas.width, canvas.height)
  //        drawable.draw(canvas)
  //
  //        // Create a temporary file
  //        val file = File.createTempFile("image", ".jpg", context.cacheDir)
  //
  //        // Save the bitmap to the file
  //        FileOutputStream(file).use { out ->
  //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
  //        }
  //
  //        // Now you can use the file with your API
  //        val response = SpoonacularApiCaller.imageAnalysis(file)
  //        Log.e("SpoonacularApiCallerTest", "imageClassificationTest: response = $response")
  //        println(response)
  //    }

  //    @Test
  //    fun imageClassificationTest() {
  //        // Context of the app under test.
  //        val inputStream =
  //            InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")
  //
  //        inputStream.available()
  //
  //        val file = File.createTempFile("image", ".jpg")
  //
  //        file.outputStream().use { outputStream ->
  //            inputStream.copyTo(outputStream)
  //        }
  //
  //        //call spoonacular api
  //        val response = SpoonacularApiCaller.imageClassification(file.absolutePath)
  //        println(response)
  //        Log.i(
  //            "SpoonacularApiCallerTest",
  //            "imageClassificationTest: response = $response"
  //        )
  //
  //    }

  @After
  fun tearDown() {
    mockWebServer.shutdown()
  }
}
