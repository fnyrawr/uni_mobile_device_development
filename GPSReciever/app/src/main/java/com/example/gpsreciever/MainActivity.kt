package com.example.gpsreciever

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

/*
* Reference how to get GPS Location in Kotlin:
* https://www.tutorialspoint.com/how-to-get-the-current-gps-location-programmatically-on-android-using-kotlin
* */

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "GPS Reciever"

        val switchProvider = findViewById<Switch>(R.id.switchProvider)

        switchProvider.setOnCheckedChangeListener { _, isChecked ->
            getLocation(isChecked)
        }

        getLocation(switchProvider.isChecked)
    }

    private fun getLocation(useGPS: Boolean) {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // ask for permission if not granted yet
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        if(useGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 1f, this)
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 1f, this)
        }

    }

    override fun onLocationChanged(location: Location) {
        var textViewLatitude = findViewById<TextView>(R.id.textViewLatitude)
        textViewLatitude.setText(location.latitude.toString())
        var textViewLongitude = findViewById<TextView>(R.id.textViewLongitude)
        textViewLongitude.setText(location.longitude.toString())
        var textViewHeight = findViewById<TextView>(R.id.textViewHeight)
        textViewHeight.setText(location.altitude.toString() + " m")
        var textViewSpeed = findViewById<TextView>(R.id.textViewSpeed)
        var speed = location.speed * 3.6 // convert to km/h
        textViewSpeed.setText(speed.toString() + " km/h")
        var textViewProvider = findViewById<TextView>(R.id.textViewProvider)
        textViewProvider.setText(location.provider.toString())
        var textViewTime = findViewById<TextView>(R.id.textViewTime)
        textViewTime.setText(Date(location.time).toString())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}