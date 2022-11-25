package com.example.gps_tracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val gpxTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    private val trackpoints: MutableList<Location> = ArrayList()
    private lateinit var filename: String
    private var tracking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonStartStop = findViewById<Button>(R.id.buttonStartStop)
        var textViewRecording = findViewById<TextView>(R.id.textViewTracking)
        buttonStartStop.setOnClickListener {
            if(!tracking) {
                tracking = true
                buttonStartStop.setText("Stop\nTracking")
                textViewRecording.setText("now tracking data")
            }
            else {
                tracking = false
                buttonStartStop.setText("Start\nTracking")
                textViewRecording.setText("")
            }
        }

        var buttonDiscard = findViewById<Button>(R.id.buttonDiscard)
        buttonDiscard.setOnClickListener {
            trackpoints.clear()
            findViewById<TextView>(R.id.textViewRecordCount).setText("${trackpoints.count()} records")
        }

        var buttonSaveCSV = findViewById<Button>(R.id.buttonSaveCSV)
        buttonSaveCSV.setOnClickListener {
            saveData()
        }

        var buttonSaveGPX = findViewById<Button>(R.id.buttonSaveGPX)
        buttonSaveGPX.setOnClickListener {
            saveData(true)
        }

        var buttonVisualize = findViewById<Button>(R.id.buttonVisualize)
        buttonVisualize.setOnClickListener {
            if(trackpoints.size > 0) drawRoute()
        }

        getLocation()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // ask for location and storage modifying permissions if not granted yet
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ), locationPermissionCode
            )
        }

        // ask for storage manager permission to be able to create files in external storage
        // only needed for Android 11 and above
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getpermission = Intent()
                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }

        // initialize GPS locationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 1f, this)
    }

    override fun onLocationChanged(location: Location) {
        // show current coordinates
        var textViewLongitude = findViewById<TextView>(R.id.textViewLongitude)
        textViewLongitude.setText(location.longitude.toString())
        var textViewLatitude = findViewById<TextView>(R.id.textViewLatitude)
        textViewLatitude.setText(location.latitude.toString())
        var textViewHeight = findViewById<TextView>(R.id.textViewHeight)
        textViewHeight.setText(location.altitude.toString() + " m")
        var textViewSpeed = findViewById<TextView>(R.id.textViewSpeed)
        var speed = location.speed * 3.6 // convert to km/h
        textViewSpeed.setText(String.format("%.2f km/h", speed))
        var textViewTimestamp = findViewById<TextView>(R.id.textViewTimestamp)
        textViewTimestamp.setText(simpleDateFormat.format(location.time))

        // add tracked data to list
        if(tracking) {
            trackpoints.add(location)
            findViewById<TextView>(R.id.textViewRecordCount).setText("${trackpoints.count()} records")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            }
            else {
                Toast.makeText(this, "required permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createCSVdata(): String {
        val data = java.lang.StringBuilder()
        // init csv columns
        data.append("Timestamp,Longitude,Latitude,Height,Speed")
        // append trackpoint rows
        trackpoints.forEach { point ->
            data.append(
                "\n${simpleDateFormat.format(point.time)},${point.longitude},${point.latitude},${point.altitude},${point.speed}"
            )
        }
        return data.toString()
    }

    private fun createGPXdata(): String {
        val data = java.lang.StringBuilder()
        // init gpx structure
        data.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" +
                "<gpx version=\"1.1\" creator=\"MA UE3 GPS-Tracker\">\n" +
                "  <metadata>\n" +
                "    <time>${gpxTimeFormat.format(Calendar.getInstance().timeInMillis)}</time>\n" +
                "  </metadata>\n" +
                "  <trk>\n" +
                "    <name>Recorded GPX Data</name>\n" +
                "    <trkseg>")
        // add trackpoint data
        trackpoints.forEach { point ->
            data.append(
                "\n      <trkpt lat=\"${point.latitude}\" lon=\"${point.longitude}\">\n" +
                        "        <ele>${point.altitude}</ele>\n" +
                        "        <time>${gpxTimeFormat.format(point.time)}</time>\n" +
                        "        <speed>${point.speed}</speed>\n" +
                        "      </trkpt>"
            )
        }
        // close gpx structure
        data.append("\n    </trkseg>\n  </trk>\n</gpx>")
        return data.toString()
    }

    private fun saveData(useGPX: Boolean = false) {
        filename = "TrackedRoute " + simpleDateFormat.format(Calendar.getInstance().timeInMillis)
        filename += if(useGPX) ".gpx" else ".csv"
        filename = filename.replace(':','.').replace(' ','_')

        var data = if(useGPX) createGPXdata() else createCSVdata()

        try {
            val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath, "MA_GPS-Tracker")
            if(!folder.exists()) folder.mkdirs()
            Log.i("Filename", File(folder, filename).toString())
            File(folder, filename).createNewFile()
            File(folder, filename).bufferedWriter().use {
                    out -> out.write(data)
            }
            Toast.makeText(this, "Saved to ${File(folder, filename)}", Toast.LENGTH_SHORT).show()
        }
        catch(e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "ERROR: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUTM(latitude: Double, longitude: Double) : String {
        val ll4 = LatLng(latitude, longitude)
        val utm2 = ll4.toUTMRef()
        return utm2.toString()
    }

    private fun drawRoute() {
        // init
        var minLon: Double = 181.0
        var maxLon: Double = -181.0
        var minLat: Double = 91.0
        var maxLat: Double = -91.0
        var imageView = findViewById<ImageView>(R.id.imageView)
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        paint.isAntiAlias = true
        imageView.setImageBitmap(bitmap)
        val width = bitmap.width
        val height = bitmap.height

        // draw 5 x 5 grid
        var x = 1
        var y = 1
        while(x < 5) {
            while(y < 5) {
                canvas.drawLine((y*height/5.0).toFloat(), 0.0f, (y*height/ 5.0).toFloat(), height.toFloat(), paint)
                y++
            }
            canvas.drawLine(0.0f, (x*width/5.0).toFloat(), width.toFloat(), (x*width/ 5.0).toFloat(), paint)
            x++
        }

        // set max and min longitude and latitude for appropriate scaling
        for(trackpoint in trackpoints) {
            if(trackpoint.longitude < minLon) minLon = trackpoint.longitude
            if(trackpoint.longitude > maxLon) maxLon = trackpoint.longitude
            if(trackpoint.latitude < minLat) minLat = trackpoint.latitude
            if(trackpoint.latitude > maxLat) maxLat = trackpoint.latitude
        }

        // set display units

        var min_utms = getUTM(minLat, minLon).split(" ")
        var max_utms = getUTM(maxLat, maxLon).split(" ")
        var deltaLon = max_utms[2].toDouble() - min_utms[2].toDouble()
        var deltaLat = max_utms[1].toDouble() - min_utms[1].toDouble()
        var geoSize = Math.max(deltaLon, deltaLat)

        var xPrev = 0.0
        var yPrev = 0.0

        for(trackpoint in trackpoints) {
            var split_utms = getUTM(trackpoint.latitude, trackpoint.longitude).split(" ")
            var y = height - ((split_utms[2].toDouble() - min_utms[2].toDouble()) * height) / geoSize
            var x = (((split_utms[1].toDouble() - min_utms[1].toDouble()) * width) / geoSize)

            if(xPrev == 0.0) {
                canvas.drawLine(x.toFloat(), y.toFloat(), x.toFloat(), y.toFloat(), paint)
            }
            else {
                canvas.drawLine(xPrev.toFloat(), yPrev.toFloat(), x.toFloat(), y.toFloat(), paint)
            }

            xPrev = x
            yPrev = y
        }

        imageView.setImageBitmap(bitmap)
    }
}