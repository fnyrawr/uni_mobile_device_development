package com.example.hikeroute

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waypoints")
data class WaypointEntity(
    val routeId: Long,
    val index: Int,
    val latitude: Double,
    val longitude: Double,
    val height: Double,
    val speed: Float,
    val timestamp: Long,
    val provider: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
)