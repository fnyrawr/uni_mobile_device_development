package com.example.hikeroute.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeroute.MainActivity
import com.example.hikeroute.R
import com.example.hikeroute.WaypointRecyclerAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [WaypointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaypointFragment() : Fragment() {
    private var waypointLayoutManager: RecyclerView.LayoutManager? = null
    private var waypointAdapter: RecyclerView.Adapter<WaypointRecyclerAdapter.ViewHolder>? = null
    private lateinit var recyclerViewWaypoints: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_waypoint, container, false)

        val mainActivity = activity as MainActivity

        recyclerViewWaypoints = view.findViewById<RecyclerView>(R.id.recyclerView_waypoints)
        waypointLayoutManager = LinearLayoutManager(this.context)
        recyclerViewWaypoints.layoutManager = waypointLayoutManager
        waypointAdapter = WaypointRecyclerAdapter(mainActivity.waypoints)
        recyclerViewWaypoints.adapter = waypointAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        waypointAdapter?.notifyDataSetChanged()
    }
}