package com.example.hikeroute

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pois")
data class PoiEntity(
    val routeId: Int,
    val latitude: Double,
    val longitude: Double,
    val place: String,
    val description: String,
    val photo: String,
    @PrimaryKey(autoGenerate = true) val id: Int
)