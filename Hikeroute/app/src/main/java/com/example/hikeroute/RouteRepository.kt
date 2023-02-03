package com.example.hikeroute

import android.app.Application
import androidx.lifecycle.LiveData

class RouteRepository(application: Application) {

    private var routeDao: RouteDao
    private var allRoutes: LiveData<List<RouteEntity>>
    private val database = AppDatabase.getInstance(application)

    init {
        routeDao = database.routeDao()
        allRoutes = routeDao.getAllRoutes()
    }

    fun insert(route: RouteEntity) {
        subscribeOnBackground { routeDao.insert(route) }
    }

    fun update(route: RouteEntity) {
        subscribeOnBackground { routeDao.update(route) }
    }

    fun delete(route: RouteEntity) {
        subscribeOnBackground { routeDao.delete(route) }
    }

    fun deleteAllRoutes() {
        subscribeOnBackground { routeDao.deleteAllRoutes() }
    }

    fun getAllRoutes(): LiveData<List<RouteEntity>> {
        return allRoutes
    }
}