import android.annotation.SuppressLint
import android.app.DownloadManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mohit.whetherapp.R
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // weather url to get JSON
    var weatherUrl = ""

    // api id for url
    var apiId = "b2e0579cfb924b198f8f3e786c5399c7"

    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        button = findViewById(R.id.btVar1)

        button.setOnClickListener {
            obtainLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                weatherUrl = "https://api.weatherbit.io/v2.0/current?" +
                        "lat=" + location?.latitude + "&lon=" + location?.longitude +
                        "&key=" + apiId
                getTemp()
            }
    }

    private fun getTemp() {
        val queue = Volley.newRequestQueue(this)
        val url: String = weatherUrl

        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                val obj = JSONObject(response)
                val arr = obj.getJSONArray("data")
                val obj2 = arr.getJSONObject(0)

                val temp = obj2.getString("temp")
                val cityName = obj2.getString("city_name")

                textView.text = "$temp deg Celsius in $cityName"
            },
            Response.ErrorListener { textView.text = "That didn't work!" })

        queue.add(stringReq)
    }
}
