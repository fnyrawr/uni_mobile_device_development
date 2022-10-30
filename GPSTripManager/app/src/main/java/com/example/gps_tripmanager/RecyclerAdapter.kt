package com.example.gps_tripmanager

import android.annotation.SuppressLint
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter(var trackpoints: MutableList<Location>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_waypoints, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return trackpoints.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        holder.itemNumber.text = (position + 1).toString()
        holder.itemLongitude.text = String.format("%.7f",trackpoints[position].longitude)
        holder.itemLatitude.text = String.format("%.7f",trackpoints[position].latitude)
        holder.itemHeight.text = "${trackpoints[position].altitude.toString()} m"
        holder.itemSpeed.text = String.format("%.1f km/h", trackpoints[position].speed * 3.6)
        holder.itemTimestamp.text = simpleDateFormat.format(trackpoints[position].time)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemNumber: TextView
        var itemLongitude: TextView
        var itemLatitude: TextView
        var itemHeight: TextView
        var itemSpeed: TextView
        var itemTimestamp: TextView

        init {
            itemNumber = itemView.findViewById(R.id.item_number)
            itemLongitude = itemView.findViewById(R.id.item_longitude)
            itemLatitude = itemView.findViewById(R.id.item_latitude)
            itemHeight = itemView.findViewById(R.id.item_height)
            itemSpeed = itemView.findViewById(R.id.item_speed)
            itemTimestamp = itemView.findViewById(R.id.item_timestamp)
        }
    }
}