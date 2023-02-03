package com.example.hikeroute

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

internal class SavedRoutesRecyclerAdapter(var routes: List<RouteEntity>) : RecyclerView.Adapter<SavedRoutesRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRoutesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_saved_routes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return routes.count()
    }

    override fun onBindViewHolder(holder: SavedRoutesRecyclerAdapter.ViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        holder.savedRouteName.text = routes[position].routeName
        holder.savedRouteBegin.text = "Begin: ${routes[position].begin}"
        holder.savedRouteEnd.text = "End: ${routes[position].end}"
        holder.savedRouteDuration.text = "Duration: ${routes[position].duration}"
        holder.savedRouteGPX.text = "GPX: ${routes[position].gpx}"
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var savedRouteName: TextView
        var savedRouteBegin: TextView
        var savedRouteEnd: TextView
        var savedRouteDuration: TextView
        var savedRouteGPX: TextView

        init {
            savedRouteName = itemView.findViewById(R.id.savedRoute_name)
            savedRouteBegin = itemView.findViewById(R.id.savedRoute_begin)
            savedRouteEnd = itemView.findViewById(R.id.savedRoute_end)
            savedRouteDuration = itemView.findViewById(R.id.savedRoute_duration)
            savedRouteGPX = itemView.findViewById(R.id.savedRoute_gpx)
        }
    }
}