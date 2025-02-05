package plantToTextAPI

import Util.getLatCoordinate
import Util.getLongCoordinate
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.plantapp.PhotoTakenActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import cz.msebera.android.httpclient.HttpResponse
import cz.msebera.android.httpclient.client.HttpClient
import cz.msebera.android.httpclient.client.methods.HttpPost
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder
import cz.msebera.android.httpclient.entity.mime.content.FileBody
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder
import cz.msebera.android.httpclient.util.EntityUtils
import io.fotoapparat.result.BitmapPhoto
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


var MIN_CONFIDENCE_LEVEL = 0

class PlantTask1(callback: OnTaskEventListener<String>) : AsyncTask<BitmapPhoto, Int?, String?>() {

    private var mCallBack: OnTaskEventListener<String> = callback

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d("PlantTask1", "PlantTask1 started")
    }

    override fun doInBackground(vararg params: BitmapPhoto): String? {
        //Prepare variables
        val bitmap = params[0].bitmap
        var result1 : Hashtable<String, String>?

        //Call api no. 1
        val plantList1 = apiPlant1(bitmap)
        Log.d("PlantTask1", "PlantTask1 found $plantList1")

        //Check if first api returned something good
        for (plant in plantList1){
            if (validatePlantLocation(plant)) {
                return plant
            }
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result);
        if (result != null) {
            mCallBack?.onSuccess(result);
        } else {
            mCallBack?.onFailure(Exception("Failed to recognize plant"));
        }
        Log.d("PlantTask1", "PlantTask1 finished!")
    }
}

class PlantTask2(callback: OnTaskEventListener<String>) : AsyncTask<BitmapPhoto, Int?, String?>() {

    private var mCallBack: OnTaskEventListener<String> = callback

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d("PlantTask2", "PlantTask2 started")
    }

    override fun doInBackground(vararg params: BitmapPhoto): String? {
        //Prepare variables
        val bitmap = params[0].bitmap
        var result2 : Hashtable<String, String>?

        //Call api no. 2
        val plantList2 = apiPlant2(bitmap)
        Log.d("PlantTask2", "PlantTask2 found $plantList2")

        //Check if second api returned something good
        for (plant in plantList2) {
            if (validatePlantLocation(plant)) {
                return plant
            }
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result);
        if (result != null) {
            mCallBack?.onSuccess(result);
        } else {
            mCallBack?.onFailure(Exception("Failed to recognize plant"));
        }
        Log.d("PlantTask2", "PlantTask2 finished!")
    }
}

class OcrTask(callback: OnTaskEventListener<String>) : AsyncTask<BitmapPhoto, Int?, String?>() {
    private var mCallBack: OnTaskEventListener<String> = callback

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d("OcrTask", "OcrTask started")
    }

    override fun doInBackground(vararg params: BitmapPhoto): String? {
        //Get bitmap from parameters
        val bitmap = params[0].bitmap
        val plantList = getPlantByLocation()

        //Make all steps from firebase api
        var returnedText: String? = null
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val task = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                returnedText = firebaseVisionText.text
                Log.d("OcrTask","Ocr recognized: $returnedText !")

                val allText = returnedText!!.split("\n")

                for (text in allText){
                    //Check if text is actually plant name
                    if (plantList.contains(text))
                        mCallBack.onSuccess(text);
                }

                mCallBack.onFailure(Exception("No plant found!"))
            }
            .addOnFailureListener { e ->
                Log.e("OcrTask", "Ocr Recognition error")
                mCallBack.onFailure(Exception("No plant found!"))
            }
        return null;
    }
}


fun validatePlantLocation(latinName: String): Boolean {
    //Preparing post parameters
    val params = JSONObject()
    params.put("lat", getLatCoordinate())
    params.put("long", getLongCoordinate())

    Log.d("validatePlantLocation", "your location is ${getLatCoordinate()} ${getLongCoordinate()}")

    //Making post request
    val response = sendPostRequest("https://us-central1-locationip-31d6f.cloudfunctions.net/plants", params, null)

    Log.d("validatePlantLocation", "Checking if $latinName is in $response")
    return response.contains(latinName)
}

fun getPlantByLocation(): String {
    //Preparing post parameters
    val params = JSONObject()
    params.put("lat", getLatCoordinate())
    params.put("long", getLongCoordinate())

    //Making post request
    return sendPostRequest("https://us-central1-locationip-31d6f.cloudfunctions.net/plants", params, null)
}

///AUXILIARY FUNCTIONS BELOW

fun apiPlant1(bitmap: Bitmap): List<String>{
    //Prepare request
    val base64Img = bitmapToBase64(bitmap)
    val plantIdUrl = "https://api.plant.id/v2/identify"
    val plantIdToken = "QrM9R3K2gsLjdTYLwreOA7xIvCI46CqspB777vG9Jkidpq2Us2"
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
        lastIndex = response.indexOf(searchedText, lastIndex)
        if(lastIndex != -1){
            var scientific_name =  response.substring(lastIndex + jumpOverText, lastIndex + jumpOverText + 20).split("\"")[1]
            names.add(scientific_name)
            lastIndex += 1
        }
    }

    // We only get each confidence number ( ex "probability": 0.09567288713602976)
    lastIndex = 0
    var searchedText2 = "\"probability\": "
    jumpOverText = searchedText2.length

    //Get all confidence numbers
    val confidence = mutableListOf<Float>()
    while(lastIndex != -1) {
        lastIndex = response.indexOf(searchedText2, lastIndex)
        if(lastIndex != -1){
            var confidenceNo =  response.substring(lastIndex + jumpOverText, lastIndex + jumpOverText + 20).split(",")[0]
            confidence.add(confidenceNo.toFloat())
            lastIndex += 1
        }
    }

    val finalNames = mutableListOf<String>()
    for (i in confidence.indices)
    {
        if (confidence.get(i) < MIN_CONFIDENCE_LEVEL)
            break
        finalNames.add(names.get(i))
    }

    //Return the names
    Log.d("api1", finalNames.toString())
    return finalNames
}

fun apiPlant2(bitmap: Bitmap): List<String>{
    //Prepare variables
    val plantNetUrl = "https://my-api.plantnet.org/v2/identify/all?api-key="
    val plantIdToken: String = "2a10vXHb74WyFBZaR6fQYdF6u"

    //Transform bitmap into file body
    val encodedImage =  bitmapToBase64(bitmap)
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
    } catch (e: Exception) {
        Log.e("PlantTask", "API for url $plantNetUrl$plantIdToken&include-related-images=true couldn't get a positive response!")
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

    // We only get each confidence number ( ex "probability": 0.09567288713602976)
    lastIndex = 0
    var searchedText2 = "\"score\":"
    jumpOverText = searchedText2.length

    //Get all confidence numbers
    val confidence = mutableListOf<Float>()
    while(lastIndex != -1) {
        lastIndex = response.indexOf(searchedText2, lastIndex)
        if(lastIndex != -1){
            var confidenceNo =  response.substring(lastIndex + jumpOverText, lastIndex + jumpOverText + 20).split(",")[0]
            confidence.add(confidenceNo.toFloat())
            lastIndex += 1
        }
    }

    val finalNames = mutableListOf<String>()
    for (i in confidence.indices)
    {
        if (confidence.get(i) < MIN_CONFIDENCE_LEVEL)
            break
        finalNames.add(names.get(i))
    }

    //Return the names
    Log.d("api2", finalNames.toString())
    return finalNames
}

fun bitmapToBase64(bitmap: Bitmap): String {

    val bitmapSize = bitmap.byteCount
    var resizedBitmap: Bitmap? = null
    var width = bitmap.width
    var height = bitmap.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = (bitmapSize * 0.5).toInt()
        height = (width / bitmapRatio).toInt()
    } else {
        height = (bitmapSize * 0.5).toInt()
        width = (height * bitmapRatio).toInt()
    }
    try {
        resizedBitmap =  Bitmap.createScaledBitmap(
            bitmap,
            600,
            600,
            false
        )
    }
    catch (e:Exception){
        return ""
    }
    //Create output stream and compress bitmap
    val outputStream = ByteArrayOutputStream()
    if (resizedBitmap != null) {
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        //Encode
        val returnedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        //Clean up
        outputStream.flush()
        outputStream.close()
        return returnedString
    } else {
        return "";
    }
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

        var ret:String = ""
        try {
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                ret = response.toString()
            }
        }
        catch(e:Exception){
            Log.e("PlantTask","API for url $urlName couldn't get a positive response!")
            System.err.println(e)
        }
        return ret
    }
}
