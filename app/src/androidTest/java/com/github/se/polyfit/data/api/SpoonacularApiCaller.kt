import android.util.Log
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

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
    @Test
    fun test_actual_request() {
        val response = SpoonacularApiCaller.searchRecipe("pasta")
        Log.e(
            "SpoonacularApiCallerTest",
            "test actual request: response = $response"
        )
        println(response)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}