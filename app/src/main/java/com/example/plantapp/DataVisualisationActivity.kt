package com.example.plantapp


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.marginEnd
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_data_visualisation.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


// TODO: add takephoto to the toolbar ( Daniel Bicu )
class DataVisualisationActivity : TopNavViewActivity() {
    var arr = arrayListOf<String>()

    lateinit var descriptionText: String
    lateinit var minimizedDescriptionText: String
    lateinit var latinName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_data_visualisation, mainLayout)

        read_more_anchor.setOnClickListener {
            if (read_more_anchor.text == "Read more") {
                description_text_view.text = descriptionText
                read_more_anchor.text = "Read less"
            } else {

                description_text_view.text = minimizedDescriptionText
                read_more_anchor.text = "Read more"

            }
        }
        val extras = intent.extras
        if (extras != null) {

            val photo: Bitmap?
            photo = null
            val src = extras.getString("photoUrl")

            // TODO: scale the photo that comes from picasso before showing it ( Daniel Bicu )
            Picasso.get().load(src).into(plant_image);

            latinName = extras.getString("latinName")!!
            descriptionText = extras.getString("description")!!
            minimizedDescriptionText = descriptionText.substring(0, 40) + "..."

            plantname.text = latinName
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

                    row.gravity = Gravity.CENTER
                    key.gravity = Gravity.CENTER
                    value.gravity = Gravity.CENTER
                    key.setTextColor(Color.parseColor("#36A961"))
                    value.setTextColor(Color.parseColor("#1D5833"))

                    key.setBackgroundResource(R.drawable.cell_shape)
                    value.setBackgroundResource(R.drawable.cell_shape)



                    row.addView(key)
                    row.addView(value)
                    description_table.addView(row)
                }
            } else {
                println("Erroare afisare date")
            }
        }
    }

    private fun addUrlImageToView(src: String?, view: ImageView) {
        println("----------")
        println(src)
        println("----------")

        val url = URL(src)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input: InputStream = connection.inputStream
        view.setImageBitmap(BitmapFactory.decodeStream(input))
    }

}
