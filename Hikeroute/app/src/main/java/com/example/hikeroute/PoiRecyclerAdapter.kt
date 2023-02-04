package com.example.hikeroute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class PoiRecyclerAdapter(var pois: MutableList<PoiEntity>) : RecyclerView.Adapter<PoiRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_waypoints, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return pois.size
    }

    override fun onBindViewHolder(holder: PoiRecyclerAdapter.ViewHolder, position: Int) {
        holder.poiName.text = "Name: ${pois[position].name}"
        holder.poiDescription.text = "Description: ${pois[position].description}"
        holder.poiLongitude.text = "Longitude: ${pois[position].longitude}"
        holder.poiLatitude.text = "Latitude: ${pois[position].latitude}"
        holder.poiTimestamp.text = "Timestamp: ${pois[position].timestamp}"
        holder.poiPhoto.text = "Photo: ${pois[position].photo}"
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var poiName: TextView
        var poiDescription: TextView
        var poiLongitude: TextView
        var poiLatitude: TextView
        var poiTimestamp: TextView
        var poiPhoto: TextView

        init {
            poiName = itemView.findViewById(R.id.poi_name)
            poiDescription = itemView.findViewById(R.id.poi_description)
            poiLongitude = itemView.findViewById(R.id.poi_longitude)
            poiLatitude = itemView.findViewById(R.id.poi_latitude)
            poiTimestamp = itemView.findViewById(R.id.poi_timestamp)
            poiPhoto = itemView.findViewById(R.id.poi_photo)
        }
    }
}