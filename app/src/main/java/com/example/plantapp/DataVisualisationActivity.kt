package com.example.plantapp


import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import kotlinx.android.synthetic.main.activity_data_visualisation.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import java.security.AccessController.getContext


class DataVisualisationActivity : TopNavViewActivity() {
var arr = arrayListOf<String>()

    lateinit var descriptionText:String
    lateinit var minimizedDescriptionText:String
    lateinit var latinName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_data_visualisation,mainLayout)
        read_more_anchor.setTextColor(Color.parseColor("#1D5833"))


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
        read_more_anchor.setTextSize(15F)


        val extras = intent.extras
        if (extras != null) {

            latinName = extras.getString("latinName")!!
            descriptionText = extras.getString("description")!!
            minimizedDescriptionText=descriptionText.substring(0,146)+"..."

            plantname.text=latinName
            description_text_view.text = minimizedDescriptionText

            println(extras.get("table"))

            description_table.setPadding(31,50,31,20)
           val table = extras.getString("table")?.split(',')
            if (table != null) {

                for (i in table.indices step 2) {
                    val row = TableRow(this)
                    val key = TextView(this)
                    val value = TextView(this)
                    key.text = table[i]
                    value.text = table[i + 1]
                    row.setPadding(-1,30,10,10)

                    /// TODO: Add properties to make this look like it looks in design (Alexandra Ciocoiu)
                    row.gravity = Gravity.CENTER
                    key.gravity = Gravity.LEFT
                    value.gravity = Gravity.CENTER
                    key.setTextColor(Color.parseColor("#36A961"))
                    value.setTextColor(Color.parseColor("#1D5833"))

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

