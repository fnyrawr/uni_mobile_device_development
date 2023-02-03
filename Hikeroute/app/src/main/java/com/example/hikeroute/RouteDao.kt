package com.example.hikeroute

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(route: RouteEntity): Long

    @Update
    fun update(route: RouteEntity)

    @Delete
    fun delete(route: RouteEntity)

    @Query("DELETE FROM routes")
    fun deleteAllRoutes()

    @Query("SELECT * FROM routes ORDER BY begin, end")
    fun getAllRoutes(): LiveData<List<RouteEntity>>
}