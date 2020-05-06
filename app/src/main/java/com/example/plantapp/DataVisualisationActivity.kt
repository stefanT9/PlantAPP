package com.example.plantapp


import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data_visualisation.*


class DataVisualisationActivity : TopNavViewActivity() {
var arr = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_visualisation)

        val extras = intent.extras
        if (extras != null) {
            val description = extras.getString("description")
            val table = extras.getString("table")?.split(',')
            if (table != null) {
                for (i in 0..table.size - 1 step 2) {
                    val row = TableRow(this)
                    val key = TextView(this)
                    val value = TextView(this)
                    key.text = table[i]
                    value.text = table[i + 1]
                    row.addView(key)
                    row.addView(value)
                    description_table.addView(row)
                }
                description_text_view.text = description
            } else {
                println("Erroare afisare date")
            }
        }
    }
}