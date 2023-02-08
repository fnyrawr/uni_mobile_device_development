package com.example.hikeroute

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class PoiRecyclerAdapter(var givenpois: MutableList<PoiEntity>) : RecyclerView.Adapter<PoiRecyclerAdapter.ViewHolder>() {
    var pois: MutableList<PoiEntity> = givenpois

    fun updateAdapter(pois: MutableList<PoiEntity>) {
        this.pois = pois
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_pois, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return pois.size
    }

    override fun onBindViewHolder(holder: PoiRecyclerAdapter.ViewHolder, position: Int) {
        holder.poiName.text = "Name: ${pois[position].name}"
        holder.poiDescription.text = "Description: ${pois[position].description}"
        holder.poiLongitude.text = "Longitude: ${pois[position].longitude}"
        holder.poiLatitude.text = "Latitude: ${pois[position].latitude}"
        holder.poiTimestamp.text = "Timestamp: ${pois[position].timestamp}"
        holder.poiPhoto.text = "Photo: ${pois[position].photo}"
        val imgFile = File("${pois[position].photo}")
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.poiThumbnail.setImageBitmap(bitmap)
        }

        holder.buttonDelete.setOnClickListener {
            val mainActivity = holder.itemView.context as MainActivity
            var appDatabase = AppDatabase.getInstance(mainActivity)
            val id = pois[position].id
            if (id != null) {
                appDatabase.poiDao().deletePoi(id.toLong())
                pois = appDatabase.poiDao().getRoutePois(mainActivity.routeId)
                this.notifyDataSetChanged()
                Toast.makeText(holder.itemView.context, "POI deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var poiName: TextView
        var poiDescription: TextView
        var poiLongitude: TextView
        var poiLatitude: TextView
        var poiTimestamp: TextView
        var poiPhoto: TextView
        var buttonDelete: Button
        var poiThumbnail: ImageView

        init {
            poiName = itemView.findViewById(R.id.poi_name)
            poiDescription = itemView.findViewById(R.id.poi_description)
            poiLongitude = itemView.findViewById(R.id.poi_longitude)
            poiLatitude = itemView.findViewById(R.id.poi_latitude)
            poiTimestamp = itemView.findViewById(R.id.poi_timestamp)
            poiPhoto = itemView.findViewById(R.id.poi_photo)
            buttonDelete = itemView.findViewById(R.id.buttonDelete)
            poiThumbnail = itemView.findViewById(R.id.poi_thumbnail)
        }
    }
}