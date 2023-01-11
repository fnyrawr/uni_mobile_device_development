package com.example.hikeroute

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waypoints")
data class WaypointEntity(
    val routeId: Int,
    val index: Int,
    val latitude: Double,
    val longitude: Double,
    val height: Double,
    val speed: Double,
    val timestamp: String,
    @PrimaryKey(autoGenerate = true) val id: Int
)