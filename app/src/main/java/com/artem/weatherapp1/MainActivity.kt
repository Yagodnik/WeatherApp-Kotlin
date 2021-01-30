package com.artem.weatherapp1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_cloud_24)

        sharedPref = applicationContext.getSharedPreferences("CityData", Context.MODE_PRIVATE)!!

        var map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        map.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_city_list -> {
            val intent = Intent(this, CityListActivity::class.java).apply {
                putExtra("com.artem.weatherapp1", "")
            }
            startActivity(intent)
            true
        }

        R.id.action_add_city -> {
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("com.artem.weatherapp1", "")
            }
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    inner class WeatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {
            var cityId:String? = params[0]
            var response: String?

            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?id=$cityId&appid=KEY").readText(Charsets.UTF_8)
            } catch (e: Exception) {
                response = null
            }

            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            handleData(result)
        }
    }

    fun handleData(data: String?) {
        try {
            val jsonData = JSONObject(data)
            val main = jsonData.getJSONObject("main")
            val coords = jsonData.getJSONObject("coord")

            val position = LatLng(coords.getDouble("lat"), coords.getDouble("lon"))
            mMap.addMarker(
                MarkerOptions().position(position).title(
                    jsonData.getString("name") + " Temperature " + (main.getDouble("temp") - 273.15).toInt()
                        .toString() + "*"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addCity(cityName: String, cityLat: String, cityLon: String, cityId: String) {
        val dataFormat = "$cityName $cityLat $cityLon $cityId";

        var set: Set<String> = sharedPref?.getStringSet("cityData", setOf()) as Set<String>

        if (!set.contains(dataFormat)) {
            var list = set.toMutableList()
            list.add(dataFormat)

            set = list.toSet()
        }

        sharedPref.edit {
            putStringSet("cityData", set)
            commit()
        }
    }

    fun addMarkers() {
        var set: Set<String> = sharedPref?.getStringSet("cityData", setOf()) as Set<String>
        sharedPref.edit().putStringSet("cityData", setOf())

        if (set.isEmpty()) {
            Log.i("test", "Empty Set!")
            return
        }

        set.forEach {
            var values = it.split(" ").toTypedArray()
            Log.i("test", "Name = " + values[0])
            Log.i("test", "Lat = " + values[1])
            Log.i("test", "Lon = " + values[2])
            Log.i("test", "Id = " + values[3])

            WeatherTask().execute(values[3])
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
//        addCity("Saint-Petersburg", "59.937500", "30.308611", "536203")
//        addCity("Sasovo", "54.353691", "41.919861", "461699")
        addMarkers()

        mMap = googleMap
    }
}
