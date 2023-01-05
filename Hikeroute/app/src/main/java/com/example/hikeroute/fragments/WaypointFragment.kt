package com.example.hikeroute.fragments

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeroute.R
import com.example.hikeroute.WaypointRecyclerAdapter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaypointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaypointFragment(var waypoints: MutableList<Location>) : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var waypointLayoutManager: RecyclerView.LayoutManager? = null
    private var waypointAdapter: RecyclerView.Adapter<WaypointRecyclerAdapter.ViewHolder>? = null
    private lateinit var recyclerViewWaypoints: RecyclerView

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
        var view = inflater.inflate(R.layout.fragment_waypoint, container, false)

        recyclerViewWaypoints = view.findViewById<RecyclerView>(R.id.recyclerView_waypoints)
        waypointLayoutManager = LinearLayoutManager(this.context)
        recyclerViewWaypoints.layoutManager = waypointLayoutManager
        waypointAdapter = WaypointRecyclerAdapter(waypoints)
        recyclerViewWaypoints.adapter = waypointAdapter

        return view
    }
}