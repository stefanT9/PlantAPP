package com.example.plantapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class DataVisualisationActivity : AppCompatActivity() {
var arr = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_visualisation)
        read_json()
    }
    fun read_json()
    {
        try {
            val inputStream:InputStream=assets.open("proiect.json")
            var json=inputStream.bufferedReader().use{it.readText()}
            val jsonarr=JSONArray(json)
            var len=jsonarr.length()
            for ( i in 0 until jsonarr.length())
            {
                var jsonobj = jsonarr.getJSONObject(i)
                arr.add(jsonobj.getString("kindom"))
            }
            var adpt=ArrayAdapter(this,android.R.layout.simple_list_item_1,arr)
            ///json_list.adapter=adpt
        }
        catch (e:IOException)
        {

        }

    }
}
