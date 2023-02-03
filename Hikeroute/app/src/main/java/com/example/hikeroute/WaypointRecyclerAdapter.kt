package com.example.hikeroute

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        holder.waypointIndex.text = (position + 1).toString()
        holder.waypointLongitude.text = String.format("%.7f",waypoints[position].longitude)
        holder.waypointLatitude.text = String.format("%.7f",waypoints[position].latitude)
        holder.waypointHeight.text = "${waypoints[position].altitude.toString()} m"
        holder.waypointSpeed.text = String.format("%.1f km/h", waypoints[position].speed * 3.6)
        holder.waypointTimestamp.text = simpleDateFormat.format(waypoints[position].time)
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