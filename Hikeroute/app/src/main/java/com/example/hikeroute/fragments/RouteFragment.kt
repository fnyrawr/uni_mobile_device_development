package com.example.hikeroute.fragments

import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hikeroute.MainActivity
import com.example.hikeroute.R
import java.text.SimpleDateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
    private val timer = Timer()
    private var fragmentActive = false
    private var lastLocation: Location = Location("dummyprovider")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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