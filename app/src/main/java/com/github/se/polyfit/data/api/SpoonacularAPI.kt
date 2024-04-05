import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object SpoonacularApiCaller {
    private val client = OkHttpClient()

    fun classifyImage() {
        val mediaType =
            "multipart/form-data; boundary=---011000010111000001101001".toMediaTypeOrNull()
        val body = "-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"file\"\r\n\r\n\r\n-----011000010111000001101001--\r\n\r\n"
            .toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/images/classify")
            .post(body)
            .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
            .addHeader("X-RapidAPI-Key", "f965a5ec78mshf84f40be1751299p189ca9jsned81613b719a")
            .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
            .build()

        try {
            val response = client.newCall(request).execute()
            println(response.body?.string())
        } catch (e: Exception) {
            System.err.println("Exception when calling Spoonacular API")
            e.printStackTrace()
        }
    }
}