package com.example.hikeroute

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

internal class SavedRoutesRecyclerAdapter(var routes: MutableList<RouteEntity>) : RecyclerView.Adapter<SavedRoutesRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRoutesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_saved_routes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return routes.count()
    }

    override fun onBindViewHolder(holder: SavedRoutesRecyclerAdapter.ViewHolder, position: Int) {
        holder.savedRouteName.text = routes[position].routeName
        holder.savedRouteBegin.text = "Begin: ${routes[position].begin}"
        holder.savedRouteEnd.text = "End: ${routes[position].end}"
        holder.savedRouteDuration.text = "Duration: ${routes[position].duration}"
        holder.savedRouteGPX.text = "GPX: ${routes[position].gpx}"

        val id = routes[position].id
        val mainActivity = holder.itemView.context as MainActivity
        var appDatabase = AppDatabase.getInstance(mainActivity)

        // load waypoints on click
        holder.itemView.setOnClickListener {
            if (id != null) {
                mainActivity.waypoints.clear()
                val waypoints = appDatabase.waypointDao().getByRouteID(id)
                for((i, waypoint) in waypoints.withIndex()) {
                    var wp = Location(
                        waypoint.provider
                    )
                    wp.latitude = waypoint.latitude
                    wp.longitude = waypoint.longitude
                    wp.altitude = waypoint.height
                    wp.speed = waypoint.speed
                    wp.time = waypoint.timestamp
                    mainActivity.waypoints.add(wp)
                }
                val pois = appDatabase.poiDao().getRoutePois(id.toLong())
                mainActivity.pois = pois
                mainActivity.routeId = id.toLong()
                Toast.makeText(holder.itemView.context, "Route loaded", Toast.LENGTH_SHORT).show()
            }
        }

        holder.buttonDelete.setOnClickListener {
            if (id != null) {
                appDatabase.routeDao().deleteRoute(id.toLong())
                appDatabase.waypointDao().deleteRouteWaypoints(id)
                routes = appDatabase.routeDao().getAllRoutes()
                this.notifyDataSetChanged()
                mainActivity.waypoints.clear()
                mainActivity.pois.clear()
                mainActivity.routeId = 0
                Toast.makeText(holder.itemView.context, "Route deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var savedRouteName: TextView
        var savedRouteBegin: TextView
        var savedRouteEnd: TextView
        var savedRouteDuration: TextView
        var savedRouteGPX: TextView
        var buttonDelete: Button

        init {
            savedRouteName = itemView.findViewById(R.id.savedRoute_name)
            savedRouteBegin = itemView.findViewById(R.id.savedRoute_begin)
            savedRouteEnd = itemView.findViewById(R.id.savedRoute_end)
            savedRouteDuration = itemView.findViewById(R.id.savedRoute_duration)
            savedRouteGPX = itemView.findViewById(R.id.savedRoute_gpx)
            buttonDelete = itemView.findViewById(R.id.buttonDelete)
        }
    }
}