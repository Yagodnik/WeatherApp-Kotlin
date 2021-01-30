package com.artem.weatherapp1

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class CityListAdapter2(private val dataSet: ArrayList<CityData>) :
    RecyclerView.Adapter<CityListAdapter2.ViewHolder>() {

    lateinit var mcontext: Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val titleView: TextView
        var removeButton: ImageButton

        init {
            titleView = view.findViewById(R.id.title)
            removeButton = view.findViewById(R.id.removeButton)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.city_list_item2, viewGroup, false)
        mcontext = viewGroup.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var sharedPref = mcontext.getSharedPreferences("CityData", Context.MODE_PRIVATE)!!

        viewHolder.titleView.text = dataSet[position].cityName

        viewHolder.removeButton.setOnClickListener {
            var set: Set<String> = sharedPref?.getStringSet("cityData", setOf()) as Set<String>
            var array = set.toMutableList()
            var query = dataSet[position].cityName + " " + dataSet[position].cityLat + " " +
                    dataSet[position].cityLon + " " + dataSet[position].cityId


//            array.forEachIndexed { index, s ->
//                if (s == (query.toString())) {
//                    array.removeAt(index)
//                    return@forEachIndexed
//                }
//            }

            val iterator = array.iterator()
            while(iterator.hasNext()){
                val item = iterator.next()
                if(item == query){
                    iterator.remove()
                    break
                }
            }

            sharedPref?.edit().putStringSet("cityData", array.toSet()).commit()

            var intent = Intent(mcontext, MainActivity::class.java).apply {
                putExtra("com.artem.weatherapp1", "")
            }

            mcontext.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size
}
