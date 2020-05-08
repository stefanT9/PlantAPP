package plantToTextAPI

import io.fotoapparat.result.BitmapPhoto

/// TODO: de folosit PlantNET (Cosmin Aftanase)
fun getPlantName(photo: BitmapPhoto?): String {
    return "Taraxacum"
}

/// TODO: De folosit API OCR Ovidiu (Cosmin Aftase)
fun ocrFunction(photo: BitmapPhoto?):String{
    return "Taraxacum"
}

/// TODO: De validat cu API Adrian (Cosmin Aftase)
fun validatePlantLocation(latinName:String,latitude:Double,longitude:Double):Boolean
{
    return true
}