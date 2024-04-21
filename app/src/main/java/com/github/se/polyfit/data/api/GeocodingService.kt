import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class GeocodingService {

  private val client = OkHttpClient()
  private val userAgent = "PolyFit/1.0 (polyfit1234@gmail.com)"

  fun searchGeocode(location: String, callback: (Pair<Double, Double>?) -> Unit) {
    val encodedLocation = location.replace(" ", "+")
    val url = "https://nominatim.openstreetmap.org/search?q=$encodedLocation&format=json&limit=1"

    val request = Request.Builder().url(url).header("User-Agent", userAgent).build()

    client
        .newCall(request)
        .enqueue(
            object : okhttp3.Callback {
              override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                callback(null)
              }

              override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                  if (!response.isSuccessful) {
                    callback(null)
                    return
                  }

                  val jsonData = response.body?.string()
                  val jsonArray = JSONArray(jsonData)
                  if (jsonArray.length() > 0) {
                    val jsonObject = jsonArray.getJSONObject(0)
                    val lat = jsonObject.getDouble("lat")
                    val lon = jsonObject.getDouble("lon")
                    callback(Pair(lat, lon))
                  } else {
                    callback(null)
                  }
                }
              }
            })
  }

  fun reverseGeocode(latitude: Double, longitude: Double, callback: (String?) -> Unit) {
    val url =
        "https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude&addressdetails=1"

    val request = Request.Builder().url(url).header("User-Agent", userAgent).build()

    client
        .newCall(request)
        .enqueue(
            object : okhttp3.Callback {
              override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                callback(null)
              }

              override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                  if (!response.isSuccessful) {
                    callback(null)
                    return
                  }

                  val jsonData = response.body?.string()
                  jsonData?.let {
                    val jsonObject = JSONObject(it)
                    val displayName = jsonObject.getString("display_name")
                    callback(displayName)
                  } ?: callback(null)
                }
              }
            })
  }
}
