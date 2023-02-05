package com.example.hikeroute.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.hikeroute.MainActivity
import com.example.hikeroute.R
import com.example.hikeroute.LatLng

/**
 * A simple [Fragment] subclass.
 * Use the [VisualFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisualFragment : Fragment() {
    private var waypoints: MutableList<Location> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_visual, container, false)
        return view
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
        var imageView = this.view?.findViewById<ImageView>(R.id.imageView)
        val bitmap = imageView?.let { Bitmap.createBitmap(it.width, imageView.height, Bitmap.Config.ARGB_8888) }
        val canvas = bitmap?.let { Canvas(it) }
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        paint.isAntiAlias = true
        imageView?.setImageBitmap(bitmap)
        val width = bitmap?.width
        val height = bitmap?.height

        // draw 5 x 5 grid
        var x = 1
        var y = 1
        while(x < 5) {
            while(y < 5) {
                canvas?.drawLine((y* height!! /5.0).toFloat(), 0.0f, (y*height/ 5.0).toFloat(), height.toFloat(), paint)
                y++
            }
            canvas?.drawLine(0.0f, (x* width!! /5.0).toFloat(), width.toFloat(), (x*width/ 5.0).toFloat(), paint)
            x++
        }

        // set max and min longitude and latitude for appropriate scaling
        for(waypoint in waypoints) {
            if(waypoint.longitude < minLon) minLon = waypoint.longitude
            if(waypoint.longitude > maxLon) maxLon = waypoint.longitude
            if(waypoint.latitude < minLat) minLat = waypoint.latitude
            if(waypoint.latitude > maxLat) maxLat = waypoint.latitude
        }

        // set display units
        var min_utms = getUTM(minLat, minLon).split(" ")
        var max_utms = getUTM(maxLat, maxLon).split(" ")
        var deltaLon = max_utms[2].toDouble() - min_utms[2].toDouble()
        var deltaLat = max_utms[1].toDouble() - min_utms[1].toDouble()
        var geoSize = Math.max(deltaLon, deltaLat)

        var xPrev = 0.0
        var yPrev = 0.0
        context?.let { ContextCompat.getColor(it, R.color.blue_700) }?.let { paint.color = it }
        paint.strokeWidth = 5F

        for(waypoint in waypoints) {
            var split_utms = getUTM(waypoint.latitude, waypoint.longitude).split(" ")
            var y = height?.minus(((split_utms[2].toDouble() - min_utms[2].toDouble()) * height) / geoSize)
            var x = (((split_utms[1].toDouble() - min_utms[1].toDouble()) * width!!) / geoSize)

            if(xPrev == 0.0) {
                if (y != null) {
                    canvas?.drawLine(x.toFloat(), y.toFloat(), x.toFloat(), y.toFloat(), paint)
                }
            }
            else {
                if (y != null) {
                    canvas?.drawLine(xPrev.toFloat(), yPrev.toFloat(), x.toFloat(), y.toFloat(), paint)
                }
            }

            xPrev = x
            if (y != null) {
                yPrev = y
            }
        }

        imageView?.setImageBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = activity as MainActivity
        waypoints = mainActivity.waypoints
        drawRoute()
    }
}