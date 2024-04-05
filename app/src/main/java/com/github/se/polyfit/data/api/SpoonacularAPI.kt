import com.github.se.polyfit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request

object SpoonacularApiCaller {
    private val client = OkHttpClient()

    fun searchRecipe(recipe: String): String {
        val request = Request.Builder()
            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=${recipe}")
            .get()
            .addHeader("X-RapidAPI-Key", BuildConfig.X_RapidAPI_Key)
            .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }
}