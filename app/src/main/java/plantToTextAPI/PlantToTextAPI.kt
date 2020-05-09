package plantToTextAPI

import android.graphics.Bitmap
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import io.fotoapparat.result.BitmapPhoto
import org.json.JSONArray
import org.json.JSONObject
import wikiapi.wikiapi
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/// TODO: de folosit(Cosmin Aftanase)
fun getPlantName(photo: BitmapPhoto): String {
    //Call api
    var plantList1 = apiPlant1(encodeToBitmap(photo.bitmap))

    //Return the first result that we can find on wikipedia
    var i = 0
    var result : Hashtable<String, String>?

    while (i < plantList1.size) {
        result = wikiapi(plantList1[i])
        if (result != null)
            break;
        i++
    }
    return plantList1[i]

    //TODO: maybe ADD SECOND API CALL (PlantNET)
}


/// TODO: De folosit API OCR Ovidiu (Cosmin Aftase)
fun ocrFunction(photo: BitmapPhoto):String {
    var returnedText: String = "error";
    val image = FirebaseVisionImage.fromBitmap(photo.bitmap)
    val detector = FirebaseVision.getInstance().cloudTextRecognizer
    val result = detector.processImage(image)
        .addOnSuccessListener { firebaseVisionText ->
            returnedText = firebaseVisionText.toString()
        }
        .addOnFailureListener { e -> print("Recognition error") }
    return returnedText;
}


/// TODO: De validat cu API Adrian (Cosmin Aftase)
fun validatePlantLocation(latinName: String, latitude: Double, longitude: Double): Boolean {

    val params = JSONObject()
    params.put("lat", latitude)
    params.put("long", longitude)

    var response = sendPostRequest("https://us-central1-locationip-31d6f.cloudfunctions.net/plants", params, null)

    return response.contains(latinName)
}


//TODO: fix import error for "Base64.encodeToString(b, Base64.DEFAULT)"
fun encodeToBitmap(bitmap: Bitmap): String? {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}


fun apiPlant1(base64Img: String): List<String>{
    //Prepare request
    val plantIdUrl = "https://api.plant.id/v2/identify"
    val plantIdToken = "UNMFzRR5vGyf7upPyKuKbTiPLvMq7BJTSxpKpOBoGget5HY950"
    val params = JSONObject()
    params.put("images", JSONArray().put(base64Img))
    params.put("modifiers",  JSONArray().put("similar_images"))

    //Call request
    val response = sendPostRequest(plantIdUrl, params, plantIdToken)

    // We only care about the scientific name ( ex "scientific_name": "Rosa")
    var lastIndex = 0
    var searchedText = "\"scientific_name\": "
    var jumpOverText = searchedText.length

    //Get all scientific names
    val names = mutableListOf<String>()
    while(lastIndex != -1) {
        lastIndex = response.indexOf(searchedText, lastIndex);
        if(lastIndex != -1){
            var scientific_name =  response.substring(lastIndex + jumpOverText, lastIndex + jumpOverText + 20).split("\"")[1]
            names.add(scientific_name)
            lastIndex += 1;
        }
    }

    //Return the names
    return names
}


fun sendPostRequest(urlName:String, params: JSONObject, token: String?): String {
    val mURL = URL(urlName)
    with(mURL.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        doOutput = true;
        doInput = true;
        setRequestProperty("Content-Type", "application/json");
        if (token != null)
            setRequestProperty("Api-Key", token);

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
