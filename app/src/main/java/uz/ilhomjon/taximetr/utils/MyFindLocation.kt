package uz.ilhomjon.taximetr.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task

private const val TAG = "MyFindLocation"

class MyFindLocation(var context: Context) {
    val REQUEST_CODE_PERMISSION = 1000
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val locationLiveData = MutableLiveData<Location>()
    }

    var beforeLocation:Location? = null
    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            if (location == null) {
                return
            }
            for (location1: Location in location.locations) {
                Log.d(TAG, "onLocationResult: ${location.toString()}")
                locationLiveData.postValue(location1)
                MyData.locationLiveData.postValue(location1)

                MyData.umumiyKm += hisobla(location1)
                if (beforeLocation!=null){
                    MyData.tezlikLiveData.postValue(MyData.distance(beforeLocation!!.latitude, beforeLocation!!.longitude, location1.latitude, location1.longitude))
                }
                beforeLocation = location1
                MyData.umumiyKmLiveData.postValue(MyData.umumiyKm)
            }
        }
    }

    fun hisobla(loc: Location):Double{
        var summa = 0.0
        if (!MyData.isWait) {
            Log.d(TAG, "hisobla: $summa")
            if (beforeLocation != null) {
                summa = (MyData.distance(
                    beforeLocation!!.latitude,
                    beforeLocation!!.longitude,
                    loc.latitude,
                    loc.longitude
                )/1000)
                Log.d(TAG, "hisobl2a: $summa")
            }
        }else{

        }
        return summa
    }

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create()
        locationRequest.setInterval(1000)
        locationRequest.setFastestInterval(1000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        checkSettingsAndStartUpdates()
    }

    fun checkSettingsAndStartUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        val client = LocationServices.getSettingsClient(context)
        val locationSettingsResponseTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(request)
        locationSettingsResponseTask.addOnSuccessListener {
            //Settings of device are satisfied and we can start location updates
            startLocationUpdates()
        }
        locationSettingsResponseTask.addOnFailureListener {
            Log.d(TAG, "checkSettingsAndStartUpdates: Error")
            Toast.makeText(
                context,
                "Xatolik \ncheckSettingsAndStartUpdates",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}