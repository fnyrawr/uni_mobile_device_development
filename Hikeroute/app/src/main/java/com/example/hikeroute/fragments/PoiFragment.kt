package com.example.hikeroute.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeroute.MainActivity
import com.example.hikeroute.PoiRecyclerAdapter
import com.example.hikeroute.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PoiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PoiFragment : Fragment() {
    private var poiLayoutManager: RecyclerView.LayoutManager? = null
    private var poiAdapter: RecyclerView.Adapter<PoiRecyclerAdapter.ViewHolder>? = null
    private lateinit var recyclerViewPoi: RecyclerView

    companion object {
        fun newInstance(): PoiFragment {
            return PoiFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_poi, container, false)

        val mainActivity = activity as MainActivity

        val addPoiButton = view.findViewById<FloatingActionButton>(R.id.floating_add_poi)
        recyclerViewPoi = view.findViewById<RecyclerView>(R.id.recyclerView_pois)
        poiLayoutManager = LinearLayoutManager(this.context)
        recyclerViewPoi.layoutManager = poiLayoutManager
        poiAdapter = PoiRecyclerAdapter(mainActivity.pois)
        recyclerViewPoi.adapter = poiAdapter

        addPoiButton.setOnClickListener {
            val poisHeader = view.findViewById<TextView>(R.id.pois_header)
            poisHeader.text = ""
            val addPoiFragment = AddPoiFragment()
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.poi_fragment, addPoiFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        poiAdapter?.notifyDataSetChanged()
    }
}