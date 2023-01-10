package com.example.hikeroute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

internal class SavedRoutesRecyclerAdapter() : RecyclerView.Adapter<SavedRoutesRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRoutesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_saved_routes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 1 // ToDo: get count from room database
    }

    override fun onBindViewHolder(holder: SavedRoutesRecyclerAdapter.ViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        holder.savedRouteName.text = "Dummy Route"
        holder.savedRouteBegin.text = "Begin: about a week ago"
        holder.savedRouteEnd.text = "End: your death"
        holder.savedRouteDescription.text = "Description: Murda on the beat so it's not nice"
        holder.savedRouteDuration.text = "Duration: Too long bruh"
        holder.savedRouteGPX.text = "GPX: TakeoffTookOff.gpx"
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var savedRouteName: TextView
        var savedRouteBegin: TextView
        var savedRouteEnd: TextView
        var savedRouteDescription: TextView
        var savedRouteDuration: TextView
        var savedRouteGPX: TextView

        init {
            savedRouteName = itemView.findViewById(R.id.savedRoute_name)
            savedRouteBegin = itemView.findViewById(R.id.savedRoute_begin)
            savedRouteEnd = itemView.findViewById(R.id.savedRoute_end)
            savedRouteDescription = itemView.findViewById(R.id.savedRoute_description)
            savedRouteDuration = itemView.findViewById(R.id.savedRoute_duration)
            savedRouteGPX = itemView.findViewById(R.id.savedRoute_gpx)
        }
    }
}