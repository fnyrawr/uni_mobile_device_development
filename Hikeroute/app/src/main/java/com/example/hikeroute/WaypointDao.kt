package com.example.hikeroute

import androidx.room.*

@Dao
interface WaypointDao {
    @Query("SELECT * FROM waypoints")
    fun getAll(): List<WaypointEntity>

    @Query("SELECT * FROM waypoints WHERE routeId IN (:rID)")
    fun getByRouteID(rID: Int): List<WaypointEntity>

    @Query("DELETE FROM waypoints WHERE routeId IS (:rID)")
    fun deleteRouteWaypoints(rID: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(waypoint: WaypointEntity)

    @Delete
    fun delete(waypoint: WaypointEntity)
}