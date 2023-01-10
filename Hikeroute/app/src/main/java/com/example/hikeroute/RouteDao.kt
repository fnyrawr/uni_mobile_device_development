package com.example.hikeroute

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RouteDao {

    @Insert
    fun insert(route: RouteEntity)

    @Update
    fun update(route: RouteEntity)

    @Delete
    fun delete(route: RouteEntity)

    @Query("delete from routes")
    fun deleteAllRoutes()

    @Query("select * from routes order by begin, end")
    fun getAllRoutes(): LiveData<List<RouteEntity>>
}