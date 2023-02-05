package com.example.hikeroute.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeroute.AppDatabase
import com.example.hikeroute.MainActivity
import com.example.hikeroute.R
import com.example.hikeroute.SavedRoutesRecyclerAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [SavedRoutesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedRoutesFragment : Fragment() {
    private var savedRoutesLayoutManager: RecyclerView.LayoutManager? = null
    private var savedRoutesAdapter: RecyclerView.Adapter<SavedRoutesRecyclerAdapter.ViewHolder>? = null
    private lateinit var recyclerViewSavedRoutes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_saved_routes, container, false)

        val mainActivity = activity as MainActivity

        recyclerViewSavedRoutes = view.findViewById<RecyclerView>(R.id.recyclerView_savedRoutes)
        savedRoutesLayoutManager = LinearLayoutManager(this.context)
        recyclerViewSavedRoutes.layoutManager = savedRoutesLayoutManager
        savedRoutesAdapter = SavedRoutesRecyclerAdapter(mainActivity.routes)
        recyclerViewSavedRoutes.adapter = savedRoutesAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        savedRoutesAdapter?.notifyDataSetChanged()
    }
}