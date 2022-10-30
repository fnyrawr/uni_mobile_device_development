package com.example.gps_tripmanager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity(), LocationListener {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private lateinit var recyclerViewWaypoints: RecyclerView

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

        recyclerViewWaypoints = findViewById<RecyclerView>(R.id.recyclerView_trackpoints)
        layoutManager = LinearLayoutManager(this)
        recyclerViewWaypoints.layoutManager = layoutManager
        adapter = RecyclerAdapter(trackpoints)
        recyclerViewWaypoints.adapter = adapter

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
            (adapter as RecyclerAdapter).notifyDataSetChanged()
        }

        var buttonSaveCSV = findViewById<Button>(R.id.buttonSaveCSV)
        buttonSaveCSV.setOnClickListener {
            saveData()
        }

        var buttonSaveGPX = findViewById<Button>(R.id.buttonSaveGPX)
        buttonSaveGPX.setOnClickListener {
            saveData(true)
        }

        var buttonLoadCSV = findViewById<Button>(R.id.buttonLoadCSV)
        buttonLoadCSV.setOnClickListener {
            loadCSV()
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
            findViewById<TextView>(R.id.textViewRecordCount).setText("${trackpoints.size} records")
            (adapter as RecyclerAdapter).notifyItemInserted(trackpoints.size-1)
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

    private fun loadCSV() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/*"
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        trackpoints.clear()
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.let { intent ->
                val lines = ArrayList(intent.data?.let { readCSV(it) })
                var firstLine = true
                lines.forEach { line ->
                    val s = line.split(",")
                    if(!firstLine) {
                        var point = Location("GPS")
                        point.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s[0]).time
                        point.longitude = s[1].toDouble()
                        point.latitude = s[2].toDouble()
                        point.altitude = s[3].toDouble()
                        point.speed = s[4].toFloat()
                        trackpoints.add(point)
                    }
                    else {
                        firstLine = false
                    }
                }
            }
        }
        adapter?.notifyDataSetChanged()
    }

    @Throws(IOException::class)
    fun readCSV(uri: Uri): List<String> {
        val csvFile = contentResolver.openInputStream(uri)
        val isr = InputStreamReader(csvFile)
        return BufferedReader(isr).readLines()
    }

    companion object {
        const val READ_REQUEST_CODE = 123
    }
}