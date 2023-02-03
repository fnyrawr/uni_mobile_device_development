package com.example.hikeroute

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class RouteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = RouteRepository(app)
    private val allRoutes = repository.getAllRoutes()

    fun insert(route: RouteEntity) {
        repository.insert(route)
    }

    fun update(route: RouteEntity) {
        repository.update(route)
    }

    fun deleteRoute(rID: Long) {
        repository.deleteRoute(rID)
    }

    fun getAllRoutes(): List<RouteEntity> {
        return allRoutes
    }
}