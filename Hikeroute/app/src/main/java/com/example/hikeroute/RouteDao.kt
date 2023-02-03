package com.example.hikeroute

import androidx.room.*

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(route: RouteEntity): Long

    @Update
    fun update(route: RouteEntity)

    @Delete
    fun delete(route: RouteEntity)

    @Query("DELETE FROM routes WHERE id = (:rID)")
    fun deleteRoute(rID: Long)

    @Query("SELECT * FROM routes ORDER BY begin, end")
    fun getAllRoutes(): List<RouteEntity>

    @Query("SELECT COUNT() FROM routes")
    fun count(): Int
}
