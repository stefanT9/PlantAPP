/*package com.example.plantapp


import android.os.Bundle
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data_visualisation.*


class DataVisualisationActivity : AppCompatActivity() {
    var arr = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_visualisation)

        val extras = intent.extras
        if (extras != null) {
            //val description = extras.getString("description")
            //val table = extras.getString("table")?.split(',')
            val description ="description"
            val table = extras.getString("table,dada,saasa,la")?.split(',')
            if (table != null) {
                for (i in 0..table.size - 1 step 2) {
                    val row = TableRow(this)
                    val key = TextView(this)
                    val value = TextView(this)
                    key.setTextSize(15);
                    value.setTextSize(15);
                    key.setGravity(Gravity.CENTER);
                    value.setGravity(Gravity.CENTER);
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
}*/