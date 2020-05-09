package plantToTextAPI

import io.fotoapparat.result.BitmapPhoto
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/// TODO: de folosit PlantNET (Cosmin Aftanase)
fun getPlantName(photo: BitmapPhoto?): String {
    return "Taraxacum"
}

/// TODO: De folosit API OCR Ovidiu (Cosmin Aftase)
fun ocrFunction(photo: BitmapPhoto?):String{
    return "Taraxacum"
}

/// TODO: De validat cu API Adrian (Cosmin Aftase)
fun validatePlantLocation(latinName: String, latitude: Double, longitude: Double): Boolean {

    val params = JSONObject()
    params.put("lat", latitude)
    params.put("long", longitude)

    var response = sendPostRequest("https://us-central1-locationip-31d6f.cloudfunctions.net/plants", params)

    return response.contains(latinName)
}

fun sendPostRequest(urlName:String, params: JSONObject): String {
    val mURL = URL(urlName)
    with(mURL.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        doOutput = true;
        doInput = true;
        setRequestProperty("Content-Type", "application/json");

        val wr = OutputStreamWriter(getOutputStream());
        wr.write(params.toString());
        wr.flush();

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()
            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            return response.toString()
        }
    }
}

