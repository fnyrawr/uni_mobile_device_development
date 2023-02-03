package com.example.hikeroute

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    val routeName: String,
    val begin: String,
    val end: String,
    val duration: String,
    val gpx: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)