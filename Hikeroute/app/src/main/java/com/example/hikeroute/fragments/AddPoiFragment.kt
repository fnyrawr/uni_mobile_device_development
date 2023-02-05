package com.example.hikeroute.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
class AddPoiFragment : Fragment() {

    companion object {
        fun newInstance(): AddPoiFragment {
            return AddPoiFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_poi, container, false)
        val mainActivity = activity as MainActivity

        var poiName = view.findViewById<EditText>(R.id.inputPoiName)
        var poiDescription = view.findViewById<EditText>(R.id.inputPoiDescription)
        var poiLatitude = view.findViewById<TextView>(R.id.textViewLatitude)
        var poiLongitude = view.findViewById<TextView>(R.id.textViewLongitude)
        var poiTimestamp = view.findViewById<TextView>(R.id.textViewTimestamp)
        var poiPhoto = view.findViewById<TextView>(R.id.textViewPhoto)
        var buttonPhoto = view.findViewById<Button>(R.id.buttonPhoto)
        var buttonSave = view.findViewById<Button>(R.id.buttonSave)
        return view
    }

}