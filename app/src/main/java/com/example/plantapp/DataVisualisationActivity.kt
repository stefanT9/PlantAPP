package com.example.proiectuliaics


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
var arr = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        read_json()
    }
    fun read_json()
    {
        var json : String? =null
        try {
            val inputStream:InputStream=assets.open("proiect.json")
            json=inputStream.bufferedReader().use{it.readText()}
            var jsonarr=JSONArray(json)
            var len=jsonarr.length()
            for ( i in 0..jsonarr.length()-1)
            {
                var jsonobj = jsonarr.getJSONObject(i)
                arr.add(jsonobj.getString("kindom"))
            }
            var adpt=ArrayAdapter(this,android.R.layout.simple_list_item_1,arr)
            json_list.adapter=adpt
        }
        catch (e:IOException)
        {

        }

    }
}
