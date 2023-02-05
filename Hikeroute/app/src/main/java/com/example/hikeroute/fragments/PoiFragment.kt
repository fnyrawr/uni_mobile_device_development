package com.example.hikeroute.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeroute.MainActivity
import com.example.hikeroute.PoiRecyclerAdapter
import com.example.hikeroute.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PoiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PoiFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var poiLayoutManager: RecyclerView.LayoutManager? = null
    private var poiAdapter: RecyclerView.Adapter<PoiRecyclerAdapter.ViewHolder>? = null
    private lateinit var recyclerViewPoi: RecyclerView

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
        var view = inflater.inflate(R.layout.fragment_poi, container, false)

        val mainActivity = activity as MainActivity

        recyclerViewPoi = view.findViewById<RecyclerView>(R.id.recyclerView_pois)
        poiLayoutManager = LinearLayoutManager(this.context)
        recyclerViewPoi.layoutManager = poiLayoutManager
        poiAdapter = PoiRecyclerAdapter(mainActivity.pois)
        recyclerViewPoi.adapter = poiAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        poiAdapter?.notifyDataSetChanged()
    }
}