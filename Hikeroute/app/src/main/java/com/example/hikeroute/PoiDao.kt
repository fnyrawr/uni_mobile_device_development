package com.example.hikeroute

import androidx.room.*

@Dao
interface PoiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(poi: PoiEntity): Long

    @Update
    fun update(poi: PoiEntity)

    @Delete
    fun delete(poi: PoiEntity)

    @Query("DELETE FROM pois WHERE id = (:pID)")
    fun deletePoi(pID: Long)

    @Query("SELECT * FROM pois WHERE routeId = (:rID) ORDER BY timestamp")
    fun getRoutePois(rID: Long): MutableList<PoiEntity>
}