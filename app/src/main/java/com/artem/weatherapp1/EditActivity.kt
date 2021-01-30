package com.artem.weatherapp1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class EditActivity : AppCompatActivity() {
    lateinit var cityAdapter: CityListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_cloud_24)

        var cityInput: SearchView = findViewById(R.id.search)
        var cityList: RecyclerView = findViewById(R.id.cityList)

        val fileName = "citylist.json"
        var fileContent = application.assets.open(fileName).bufferedReader().use{
            it.readText()
        }

        val jsonData = JSONArray(fileContent)
        var data: ArrayList<CityData> = ArrayList<CityData>()

        for (i in 0..jsonData.length()-1) {
            var cityName = jsonData.getJSONObject(i).get("name").toString()
            var cityId = jsonData.getJSONObject(i).get("id").toString()
            var cityLon = jsonData.getJSONObject(i).getJSONObject("coord").get("lon").toString()
            var cityLat = jsonData.getJSONObject(i).getJSONObject("coord").get("lat").toString()

            var cityDelegate: CityData = CityData()
            cityDelegate.cityName = cityName
            cityDelegate.cityLon = cityLon
            cityDelegate.cityLat = cityLat
            cityDelegate.cityId = cityId
            data.add(cityDelegate)
        }

        cityInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cityAdapter.filter.filter(newText)
                return false
            }
        })

        cityAdapter = CityListAdapter(data)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        cityList.layoutManager = llm
        cityList.adapter = cityAdapter
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