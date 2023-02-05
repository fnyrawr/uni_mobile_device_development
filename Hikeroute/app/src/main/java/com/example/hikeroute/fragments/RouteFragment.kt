package com.example.hikeroute.fragments

import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hikeroute.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [RouteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RouteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var tracking: Boolean = false
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val gpxTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    private val timer = Timer()
    private var fragmentActive = false
    private var lastLocation: Location = Location("dummyprovider")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_route, container, false)
        val mainActivity = activity as MainActivity

        var buttonStartStop = view.findViewById<Button>(R.id.buttonStartStop)
        var textViewRecording = view.findViewById<TextView>(R.id.textViewTracking)
        buttonStartStop.setOnClickListener {
            if(!tracking) {
                mainActivity.waypoints.clear()
                mainActivity.pois.clear()
                mainActivity.routeId = 0
                tracking = true
                buttonStartStop.text = "Stop\nTracking"
                textViewRecording.text = "now tracking data"
            }
            else {
                tracking = false
                buttonStartStop.text = "Start\nTracking"
                textViewRecording.text = ""
            }
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                if(mainActivity.currentLocation.provider != "dummyprovider" && fragmentActive)
                    updateLocation(mainActivity.currentLocation)
            }
        }, 0, 1000)

        var buttonDiscard = view.findViewById<Button>(R.id.buttonDiscard)
        buttonDiscard.setOnClickListener {
            mainActivity.waypoints.clear()
            mainActivity.routeId = 0
        }

        var buttonSave = view.findViewById<Button>(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveRoute()
        }

        return view
    }

    fun updateLocation(location: Location) {
        var view = this.view
        val mainActivity = activity as MainActivity

        // show current coordinates
        var textViewLongitude = view?.findViewById<TextView>(R.id.textViewLongitude)
        textViewLongitude?.text = location.longitude.toString()
        var textViewLatitude = view?.findViewById<TextView>(R.id.textViewLatitude)
        textViewLatitude?.text = location.latitude.toString()
        var textViewHeight = view?.findViewById<TextView>(R.id.textViewHeight)
        textViewHeight?.text = location.altitude.toString() + " m"
        var textViewSpeed = view?.findViewById<TextView>(R.id.textViewSpeed)
        var speed = location.speed * 3.6 // convert to km/h
        textViewSpeed?.text = String.format("%.2f km/h", speed)
        var textViewTimestamp = view?.findViewById<TextView>(R.id.textViewTimestamp)
        textViewTimestamp?.text = simpleDateFormat.format(location.time)

        // add tracked data to list
        if(tracking && lastLocation.provider != "dummyprovider" && location != lastLocation) {
            mainActivity.waypoints.add(location)
        }

        lastLocation = location
    }

    fun saveRoute() {
        // should only work if not tracking && route name given in TextEdit
        // saving Route as GPX and into Room DB
        var view = this.view
        val mainActivity = activity as MainActivity

        val routename = view?.findViewById<EditText>(R.id.inputRouteName)?.text.toString().trim()
        val waypoints = mainActivity.waypoints
        if(waypoints.size == 0) {
            Toast.makeText(activity, "Your route does not contain any waypoints", Toast.LENGTH_SHORT).show()
            return
        }
        val routeBegin = simpleDateFormat.format(waypoints[0].time)
        val routeEnd = simpleDateFormat.format(waypoints[waypoints.size-1].time)
        val timeDiff = waypoints[waypoints.size-1].time - waypoints[0].time
        val totalSecs = timeDiff / 1000
        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60
        val seconds = totalSecs % 60
        val routeDuration = "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"

        if(!tracking && routename != "") {
            // save gpx file first
            val gpxFile = saveGPXdata(routename, waypoints)

            subscribeOnBackground {
                // get database instance (singleton for performance reasons)
                var appDatabase = AppDatabase.getInstance(mainActivity)
                // save route
                val routeID = appDatabase.routeDao().insert(
                    RouteEntity(
                        routeName = routename,
                        begin = routeBegin,
                        end = routeEnd,
                        duration = routeDuration,
                        gpx = gpxFile
                    )
                )
                // save waypoints
                for ((i, waypoint) in waypoints.withIndex()) {
                    appDatabase.waypointDao().insert(
                        WaypointEntity(
                            routeId = routeID,
                            index = i,
                            latitude = waypoint.latitude,
                            longitude = waypoint.longitude,
                            height = waypoint.altitude,
                            speed = waypoint.speed,
                            timestamp = waypoint.time,
                            provider = waypoint.provider
                        )
                    )
                }
                mainActivity.routes = appDatabase.routeDao().getAllRoutes()
            }
            Toast.makeText(activity, "Route saved", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(activity, "You cannot save while tracking and without a name for the route", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createGPXdata(waypoints: MutableList<Location>): String {
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
        waypoints.forEach { point ->
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

    private fun saveGPXdata(routeName: String, waypoints: MutableList<Location>): String {
        var filename = "$routeName " + simpleDateFormat.format(Calendar.getInstance().timeInMillis) + ".gpx"
        filename = filename.replace(':','.').replace(' ','_')
        var data = createGPXdata(waypoints)

        try {
            val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath, "MA_GPS-Tracker")
            if(!folder.exists()) folder.mkdirs()
            File(folder, filename).createNewFile()
            File(folder, filename).bufferedWriter().use {
                    out -> out.write(data)
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
        }
        return filename
    }

    override fun onResume() {
        super.onResume()
        fragmentActive = true
    }

    override fun onPause() {
        super.onPause()
        fragmentActive = false
        tracking = false
    }
}