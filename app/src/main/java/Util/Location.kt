package Util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.plantapp.R
import com.google.android.gms.location.*

lateinit var activity: Activity
lateinit var context: Context
lateinit var mFusedLocationClient: FusedLocationProviderClient
var location_lat: Double = 0.0
var location_long: Double = 0.0

fun initializeLocationData(act: Activity, con: Context, fusedLoc: FusedLocationProviderClient){
    activity = act
    context = con
    mFusedLocationClient = fusedLoc
}

fun getLatCoordinate(): Double {
    return location_lat
}

fun getLongCoordinate(): Double {
    return location_long
}

@SuppressLint("MissingPermission")
fun getLastLocation() {
    if (checkPermissions()) {
        if (isLocationEnabled()) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
                var location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    location_lat = location.latitude
                    location_long = location.longitude
                }
            }
        } else {
            Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
        }
    } else {
        requestPermissions()
    }
}

@SuppressLint("MissingPermission")
fun requestNewLocationData() {
    var mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 0
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    mFusedLocationClient!!.requestLocationUpdates(
        mLocationRequest, mLocationCallback,
        Looper.myLooper()
    )
}

val mLocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        var mLastLocation: Location = locationResult.lastLocation
        location_lat= mLastLocation.latitude
        location_long = mLastLocation.longitude
    }
}

fun requestPermissions() {
    ActivityCompat.requestPermissions(activity,
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 200)
}

fun checkPermissions(): Boolean {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
        return true
    }
    return false
}

fun isLocationEnabled(): Boolean {
    var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}
