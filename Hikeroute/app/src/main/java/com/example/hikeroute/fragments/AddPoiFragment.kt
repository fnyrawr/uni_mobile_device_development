package com.example.hikeroute.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hikeroute.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
        var poiLatitude = view.findViewById<EditText>(R.id.inputPoiLatitude)
        var poiLongitude = view.findViewById<EditText>(R.id.inputPoiLongitude)
        var poiTimestamp = view.findViewById<TextView>(R.id.textViewPoiTimestamp)
        var poiPhoto = view.findViewById<TextView>(R.id.textViewPoiPhoto)
        var buttonPhoto = view.findViewById<Button>(R.id.buttonPhoto)
        var buttonSave = view.findViewById<Button>(R.id.buttonSave)
        var buttonClose = view.findViewById<Button>(R.id.buttonClose)

        poiTimestamp.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        buttonSave.setOnClickListener {
            val saved = savePoi()
            val manager = parentFragmentManager
            if(saved) {
                var appDatabase = AppDatabase.getInstance(mainActivity)
                mainActivity.pois = appDatabase.poiDao().getRoutePois(mainActivity.routeId)
                val transaction = manager.beginTransaction()
                val toBeReplaced = manager.findFragmentById(R.id.poi_fragment)?.id
                val fragment = PoiFragment()
                if (toBeReplaced != null) {
                    transaction.replace(toBeReplaced, fragment)
                }
                transaction.detach(fragment)
                transaction.attach(fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                Toast.makeText(activity, "Poi saved", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(activity, "Not all required information given", Toast.LENGTH_SHORT).show()
            }
        }

        buttonClose.setOnClickListener {
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            val toBeReplaced = manager.findFragmentById(R.id.poi_fragment)?.id
            val fragment = PoiFragment()
            if (toBeReplaced != null) {
                transaction.replace(toBeReplaced, fragment)
            }
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    fun savePoi(): Boolean {
        var saved = false
        var view = this.view
        val mainActivity = activity as MainActivity
        val poiName = view?.findViewById<EditText>(R.id.inputPoiName)?.text.toString().trim()
        val poiDescription = view?.findViewById<EditText>(R.id.inputPoiDescription)?.text.toString().trim()
        val poiLatitude = view?.findViewById<EditText>(R.id.inputPoiLatitude)?.text.toString().trim()
        val poiLongitude = view?.findViewById<EditText>(R.id.inputPoiLongitude)?.text.toString().trim()
        val poiTimestamp = view?.findViewById<TextView>(R.id.textViewPoiTimestamp)?.text.toString().trim()
        val poiPhoto = view?.findViewById<TextView>(R.id.textViewPoiPhoto)?.text.toString().trim()

        var routeId = mainActivity.routeId

        if((poiName.isNotEmpty() && poiDescription.isNotEmpty() && poiLatitude.isNotEmpty() && poiLongitude.isNotEmpty())) {
            saved = true
            subscribeOnBackground {
                // get database instance (singleton for performance reasons)
                var appDatabase = AppDatabase.getInstance(mainActivity)

                // save poi
                appDatabase.poiDao().insert(
                    PoiEntity(
                        routeId = routeId,
                        name = poiName,
                        description = poiDescription,
                        latitude = poiLatitude.toDouble(),
                        longitude = poiLongitude.toDouble(),
                        timestamp = poiTimestamp,
                        photo = poiPhoto
                    )
                )
            }
        }

        return saved
    }

}