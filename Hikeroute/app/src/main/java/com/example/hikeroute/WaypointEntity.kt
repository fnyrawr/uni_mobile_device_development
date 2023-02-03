package com.example.hikeroute

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
    // using Long here because Int may not be enough if several longer routes are saved
    @PrimaryKey(autoGenerate = true) val id: Long? = null
)