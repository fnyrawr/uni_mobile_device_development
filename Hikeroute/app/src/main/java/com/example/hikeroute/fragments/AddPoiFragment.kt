package com.example.hikeroute.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hikeroute.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

    private val cameraRequestId = 100

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

        if(ContextCompat.checkSelfPermission(mainActivity.applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.CAMERA), cameraRequestId)
        }

        buttonPhoto.setOnClickListener {
            val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraInt, cameraRequestId)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == cameraRequestId) {
            val mediaPath = createNewImageFile(this.requireContext())
            val image: Bitmap = data?.extras?.get("data") as Bitmap
            val file = saveBitmapToFile(image, "image/jpg", mediaPath?.absolutePath)
            val file_path = file?.path
            println(file_path.toString())
            view?.findViewById<TextView>(R.id.textViewPoiPhoto)?.text = file_path.toString()
        }
    }

    @Throws(IOException::class)
    fun createNewImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            absolutePath
        }
    }

    fun saveBitmapToFile(bitmap: Bitmap?, mimeType: String, absolutePath: String?): File? {
        if(absolutePath == null || bitmap == null){
            return null
        }

        val file = File(absolutePath)
        val stream = FileOutputStream(file)

        if (mimeType.contains("jpg", true) || mimeType.contains("jpeg", true))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        else if (mimeType.contains("png", true))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        stream.close()

        return file
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