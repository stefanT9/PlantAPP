package plantToTextAPI

import io.fotoapparat.result.BitmapPhoto

/// TODO: de folosit PlantNET
fun getPlantName(photo: BitmapPhoto?): String {
    return "Taraxacum"
}

/// TODO: De folosit API OCR Ovidiu
fun ocrFunction(photo: BitmapPhoto?):String{
    return "Taraxacum"
}

/// TODO: De validat cu API Adrian
fun validatePlantLocation(latinName:String,latitude:Double,longitude:Double):Boolean
{
    return true
}