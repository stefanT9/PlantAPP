package plantToTextAPI

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import cz.msebera.android.httpclient.HttpResponse
import cz.msebera.android.httpclient.client.HttpClient
import cz.msebera.android.httpclient.client.methods.HttpPost
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder
import cz.msebera.android.httpclient.entity.mime.content.FileBody
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder
import cz.msebera.android.httpclient.util.EntityUtils
import io.fotoapparat.result.BitmapPhoto
import org.json.JSONArray
import org.json.JSONObject
import wikiapi.wikiapi
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/// TODO: Only use results that are more than x% (parametrizat) confident ( Cosmin Aftanase )

fun getPlantName(photo: BitmapPhoto): String? {
    //Call api no. 1
    val plantList1 = apiPlant1(photo.bitmap)

    //Call api no. 2
    //val plantList2 = apiPlant2(photo.bitmap)

    //Prepare variables
    var result1 : Hashtable<String, String>?
    var result2 : Hashtable<String, String>?

    //Return the first result that we can find on wikipedia
    for (i in plantList1.indices){

        //Check if first api returned something good
        result1 = wikiapi(plantList1[i])
        if (result1 != null) {
            return plantList1[i]
        }
    }
    
    /*
    for (i in plantList2.indices) {

        //Check if second api returned something good
        result2 = wikiapi(plantList2[i])
        if (result2 != null) {
            return plantList2[i]
        }
    }
    */
    return null
}

fun ocrFunction(photo: BitmapPhoto):String? {
    var returnedText: String? = null
    val image = FirebaseVisionImage.fromBitmap(photo.bitmap)
    val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
    detector.processImage(image)
        .addOnSuccessListener { firebaseVisionText ->
            returnedText = firebaseVisionText.toString()
            Log.e("Ocr","Ocr recognized: $returnedText !")
        }
        .addOnFailureListener { e ->
            Log.e("Ocr","Ocr Recognition error")
        }
        .result
    return returnedText
}

fun validatePlantLocation(latinName: String, latitude: Double, longitude: Double): Boolean {

    val params = JSONObject()
    params.put("lat", latitude)
    params.put("long", longitude)

    val response = sendPostRequest("https://us-central1-locationip-31d6f.cloudfunctions.net/plants", params, null)

    return response.contains(latinName)
}

///AUXILIARY FUNCTIONS BELOW

fun apiPlant1(bitmap: Bitmap): List<String>{
    //Prepare request
    val base64Img = bitmapToBase64(bitmap)
    val plantIdUrl = "https://api.plant.id/v2/identify"
    val plantIdToken = "UNMFzRR5vGyf7upPyKuKbTiPLvMq7BJTSxpKpOBoGget5HY950"
    val params = JSONObject()
    params.put("images", JSONArray().put(base64Img))
    params.put("modifiers",  JSONArray().put("similar_images"))

    //Call request
    val response = sendPostRequest(plantIdUrl, params, plantIdToken)

    // We only care about the scientific name ( ex "scientific_name": "Rosa")
    var lastIndex = 0
    val searchedText = "\"scientific_name\": "
    val jumpOverText = searchedText.length

    //Get all scientific names
    val names = mutableListOf<String>()
    while(lastIndex != -1) {
        lastIndex = response.indexOf(searchedText, lastIndex)
        if(lastIndex != -1){
            val scientific_name =  response.substring(lastIndex + jumpOverText, lastIndex + jumpOverText + 20).split("\"")[1]
            names.add(scientific_name)
            lastIndex += 1
        }
    }

    //Return the names
    Log.d("api1", names.toString())
    return names
}

fun apiPlant2(bitmap: Bitmap): List<String>{
    //Prepare variables
    val plantNetUrl = "https://my-api.plantnet.org/v2/identify/all?api-key="
    val plantIdToken: String = "2a10vXHb74WyFBZaR6fQYdF6u"

    //Transform bitmap into file body
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    val encodedImage =  Base64.encodeToString(imageBytes, Base64.DEFAULT)
    val fileBody = FileBody(File(encodedImage))

    //Prepare post
    val entity = MultipartEntityBuilder.create().addPart("images", fileBody).addTextBody("organs", "flower").build()
    val request = HttpPost("$plantNetUrl$plantIdToken&include-related-images=true")
    request.entity = entity
    val client: HttpClient = HttpClientBuilder.create().build()

    //Get response
    val httpResponse: HttpResponse
    var response: String
    try {
        httpResponse = client.execute(request)
        response = EntityUtils.toString(httpResponse.entity)
    } catch (e: IOException) {
        response = "error"
    }

    // We only care about the scientific name ( ex "scientificNameWithoutAuthor":"Galanthus nivalis")
    var lastIndex = 0
    var searchedText = "\"scientificNameWithoutAuthor\":"
    var jumpOverText = searchedText.length

    //Get all scientific names
    val names = mutableListOf<String>()
    while(lastIndex != -1) {
        lastIndex = response.indexOf(searchedText, lastIndex)
        if(lastIndex != -1){
            var scientific_name =  response.substring(lastIndex + jumpOverText, lastIndex + jumpOverText + 20).split("\"")[1]
            names.add(scientific_name)
            lastIndex += 1
        }
    }

    //Return the response
    Log.d("api2", names.toString())
    return names
}

fun bitmapToBase64(bitmap: Bitmap): String {
    //Create output stream and compress bitmap
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

    //Encode
    val returnedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

    //Clean up
    outputStream.flush()
    outputStream.close()
    return returnedString
}

fun sendPostRequest(urlName:String, params: JSONObject, token: String?): String {
    val mURL = URL(urlName)
    with(mURL.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        doOutput = true
        doInput = true
        setRequestProperty("Content-Type", "application/json")
        if (token != null)
            setRequestProperty("Api-Key", token)

        val wr = OutputStreamWriter(outputStream)
        wr.write(params.toString())
        wr.flush()

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
