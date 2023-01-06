package com.example.hikeroute.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                buttonStartStop.setText("Stop\nTracking")
                textViewRecording.setText("now tracking data")
            }
            else {
                tracking = false
                buttonStartStop.setText("Start\nTracking")
                textViewRecording.setText("")
            }
        }

        var buttonDiscard = view.findViewById<Button>(R.id.buttonDiscard)
        buttonDiscard.setOnClickListener {
            mainActivity.waypoints.clear()
            view.findViewById<TextView>(R.id.textViewRecordCount).setText("${mainActivity.waypoints.count()} records")
            mainActivity.viewPagerAdapter.notifyDataSetChanged()
        }

        return view
    }

    fun updateLocation(location: Location) {
        var view = this.view
        val mainActivity = activity as MainActivity

        // show current coordinates
        var textViewLongitude = view?.findViewById<TextView>(R.id.textViewLongitude)
        textViewLongitude?.setText(location.longitude.toString())
        var textViewLatitude = view?.findViewById<TextView>(R.id.textViewLatitude)
        textViewLatitude?.setText(location.latitude.toString())
        var textViewHeight = view?.findViewById<TextView>(R.id.textViewHeight)
        textViewHeight?.setText(location.altitude.toString() + " m")
        var textViewSpeed = view?.findViewById<TextView>(R.id.textViewSpeed)
        var speed = location.speed * 3.6 // convert to km/h
        textViewSpeed?.setText(String.format("%.2f km/h", speed))
        var textViewTimestamp = view?.findViewById<TextView>(R.id.textViewTimestamp)
        textViewTimestamp?.setText(simpleDateFormat.format(location.time))

        // add tracked data to list
        if(tracking) {
            mainActivity.waypoints.add(location)
            view?.findViewById<TextView>(R.id.textViewRecordCount)?.setText("${mainActivity.waypoints.size} records")
            mainActivity.viewPagerAdapter.notifyItemInserted(mainActivity.waypoints.size-1)
        }
    }
}