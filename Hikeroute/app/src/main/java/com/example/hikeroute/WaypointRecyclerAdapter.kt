package com.example.hikeroute

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeroute.fragments.AddPoiFragment
import java.text.SimpleDateFormat
import java.util.*


internal class WaypointRecyclerAdapter(var waypoints: MutableList<Location>) : RecyclerView.Adapter<WaypointRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaypointRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_waypoints, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return waypoints.size
    }

    override fun onBindViewHolder(holder: WaypointRecyclerAdapter.ViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        val mainActivity = holder.itemView.context as MainActivity

        holder.waypointIndex.text = "Waypoint " + (position + 1).toString()
        holder.waypointLongitude.text = "Longitude: " + String.format("%.7f",waypoints[position].longitude)
        holder.waypointLatitude.text = "Latitude: " + String.format("%.7f",waypoints[position].latitude)
        holder.waypointHeight.text = "Height: " + "${waypoints[position].altitude.toString()} m"
        holder.waypointSpeed.text = "Speed: " + String.format("%.1f km/h", waypoints[position].speed * 3.6)
        holder.waypointTimestamp.text = "Timestamp: " + simpleDateFormat.format(waypoints[position].time)

        // create poi on item click
        val waypoint = waypoints[position]
        holder.itemView.setOnClickListener {
            if (waypoint != null) {
                // ToDo: open AddPoiFragment here (code below not working)
                mainActivity.supportFragmentManager.beginTransaction().replace(R.id.fragmentSavedRoutes, AddPoiFragment()).commit()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var waypointIndex: TextView
        var waypointLongitude: TextView
        var waypointLatitude: TextView
        var waypointHeight: TextView
        var waypointSpeed: TextView
        var waypointTimestamp: TextView

        init {
            waypointIndex = itemView.findViewById(R.id.waypoint_index)
            waypointLongitude = itemView.findViewById(R.id.waypoint_longitude)
            waypointLatitude = itemView.findViewById(R.id.waypoint_latitude)
            waypointHeight = itemView.findViewById(R.id.waypoint_height)
            waypointSpeed = itemView.findViewById(R.id.waypoint_speed)
            waypointTimestamp = itemView.findViewById(R.id.waypoint_timestamp)
        }
    }


}