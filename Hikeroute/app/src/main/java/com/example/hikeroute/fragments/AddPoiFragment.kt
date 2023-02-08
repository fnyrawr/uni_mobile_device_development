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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hikeroute.*
import kotlinx.android.synthetic.main.recyclerview_pois.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
        var poiLatitude = view.findViewById<EditText>(R.id.inputLatitude)
        var poiLongitude = view.findViewById<EditText>(R.id.inputLongitude)
        var poiTimestamp = view.findViewById<TextView>(R.id.textViewTimestamp)
        var poiPhoto = view.findViewById<TextView>(R.id.textViewPhoto)
        var buttonPhoto = view.findViewById<Button>(R.id.buttonPhoto)
        var buttonSave = view.findViewById<Button>(R.id.buttonSave)

        poiTimestamp.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        buttonSave.setOnClickListener {
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            val toBeReplaced = manager.findFragmentById(R.id.poi_fragment)?.id
            if (toBeReplaced != null) {
                transaction.replace(toBeReplaced, PoiFragment())
            }
            transaction.addToBackStack(null)
            transaction.commit()

            savePoi()
        }

        return view
    }

    fun savePoi(): Boolean {
        var saved = false
        var view = this.view
        val mainActivity = activity as MainActivity
        val poiNameValue = view?.findViewById<EditText>(R.id.inputPoiName)?.text.toString().trim()
        val poiDescriptionValue = view?.findViewById<EditText>(R.id.inputPoiDescription)?.text.toString().trim()
        val poiLatitudeValue = view?.findViewById<EditText>(R.id.inputLatitude)?.text.toString().trim()
        val poiLongitudeValue = view?.findViewById<EditText>(R.id.inputLongitude)?.text.toString().trim()
        val poiTimeStampValue = view?.findViewById<TextView>(R.id.textViewTimestamp)?.text.toString().trim()
        val poiPhotoValue = view?.findViewById<TextView>(R.id.textViewPhoto)?.text.toString().trim()

        var routeId = mainActivity.routeId

        if((poiNameValue.isNotEmpty() && poiDescriptionValue.isNotEmpty() && poiLatitudeValue.isNotEmpty() && poiLongitudeValue.isNotEmpty()) && routeId != "0".toLong()) {
            subscribeOnBackground {
                // get database instance (singleton for performance reasons)
                var appDatabase = AppDatabase.getInstance(mainActivity)

                // save poi
                appDatabase.poiDao().insert(
                    PoiEntity(
                        routeId = routeId,
                        name = poiNameValue,
                        description = poiDescriptionValue,
                        latitude = poiLatitudeValue.toDouble(),
                        longitude = poiLongitudeValue.toDouble(),
                        timestamp = poiTimeStampValue,
                        photo = poiPhotoValue
                    )
                )
                saved = true
            }
            Toast.makeText(activity, "Poi saved", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(activity, "You cannot save with empty fields", Toast.LENGTH_SHORT).show()
        }

        return saved

    }

}