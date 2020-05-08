package com.example.plantapp


import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_data_visualisation.*
import kotlinx.android.synthetic.main.activity_top_nav.*


class DataVisualisationActivity : TopNavViewActivity() {
var arr = arrayListOf<String>()

    lateinit var descriptionText:String
    lateinit var minimizedDescriptionText:String
    lateinit var latinName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_data_visualisation,mainLayout)
        setUpToolbar()

        read_more_anchor.setOnClickListener {
            if (read_more_anchor.text == "Read more") {
                description_text_view.text = descriptionText
                read_more_anchor.text="Read less"
            }
            else
            {
                description_text_view.text = minimizedDescriptionText
                read_more_anchor.text="Read more"

            }
        }
        val extras = intent.extras
        if (extras != null) {

            latinName = extras.getString("latinName")!!
            descriptionText = extras.getString("description")!!
            minimizedDescriptionText=descriptionText.substring(0,40)+"..."

            plantname.text=latinName
            description_text_view.text = minimizedDescriptionText

            println(extras.get("table"))

            val table = extras.getString("table")?.split(',')
            if (table != null) {

                for (i in table.indices step 2) {
                    val row = TableRow(this)
                    val key = TextView(this)
                    val value = TextView(this)
                    key.text = table[i]
                    value.text = table[i + 1]
                    row.addView(key)
                    row.addView(value)
                    description_table.addView(row)
                }
            } else {
                println("Erroare afisare date")
            }
        }
    }
}