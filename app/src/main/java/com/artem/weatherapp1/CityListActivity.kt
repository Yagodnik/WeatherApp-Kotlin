package com.artem.weatherapp1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CityListActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        var toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_cloud_24)

        var cityList: RecyclerView = findViewById(R.id.cityList)
        var emptyView: TextView = findViewById(R.id.emptyView)
        var data: ArrayList<CityData> = ArrayList<CityData>()

        sharedPref = applicationContext.getSharedPreferences("CityData", Context.MODE_PRIVATE)!!
        var set: Set<String> = sharedPref?.getStringSet("cityData", setOf()) as Set<String>

        if (set.isEmpty()) {
            Log.i("test", "Empty Set!")
            return
        }

        set.forEach {
            var values = it.split(" ").toTypedArray()
            var cityDelegate: CityData = CityData()
            cityDelegate.cityName = values[0]
            cityDelegate.cityLat = values[1]
            cityDelegate.cityLon = values[2]
            cityDelegate.cityId = values[3]
            Log.i("test", "Name = " + values[0])
            Log.i("test", "Lat = " + values[1])
            Log.i("test", "Lon = " + values[2])
            Log.i("test", "Id = " + values[3])

            data.add(cityDelegate)
        }

        var listAdapter: CityListAdapter2 = CityListAdapter2(data)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        cityList.layoutManager = llm
        cityList.adapter = listAdapter

        if (set.isEmpty()) {
            cityList.visibility = View.GONE;
            emptyView.visibility = View.VISIBLE;
        } else {
            cityList.visibility = View.VISIBLE;
            emptyView.visibility = View.GONE;
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}