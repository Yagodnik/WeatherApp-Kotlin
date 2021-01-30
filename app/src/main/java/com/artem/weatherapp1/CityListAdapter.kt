package com.artem.weatherapp1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class CityListAdapter(private val dataSet: ArrayList<CityData>) :
    RecyclerView.Adapter<CityListAdapter.ViewHolder>(), Filterable {

    var cityFilterList: ArrayList<CityData> = dataSet
    lateinit var mcontext: Context


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView

        init {
            titleView = view.findViewById(R.id.title)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    cityFilterList = dataSet
                } else {
                    val resultList = ArrayList<CityData>()
                    for (row in dataSet) {
                        if (row.cityName.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    cityFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = cityFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                cityFilterList = results?.values as ArrayList<CityData>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.city_list_item, viewGroup, false)
        mcontext = viewGroup.context
        return ViewHolder(view)
    }

    private fun addCity(cityName: String, cityLat: String, cityLon: String, cityId: String) {
        var sharedPref = mcontext.getSharedPreferences("CityData", Context.MODE_PRIVATE)!!

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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleView.text = cityFilterList[position].cityName

        viewHolder.titleView.setOnClickListener { view ->
            addCity(cityFilterList[position].cityName,  cityFilterList[position].cityLat,
                                  cityFilterList[position].cityLon,  cityFilterList[position].cityId)

            var intent = Intent(mcontext, MainActivity::class.java).apply {
                putExtra("com.artem.weatherapp1", "")
            }

            mcontext.startActivity(intent)
        }
    }

    override fun getItemCount() = cityFilterList.size
}
