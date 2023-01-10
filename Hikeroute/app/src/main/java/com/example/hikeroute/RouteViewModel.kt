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

    fun delete(route: RouteEntity) {
        repository.deleteAllRoutes()
    }

    fun getAllRoutes(): LiveData<List<RouteEntity>> {
        return allRoutes
    }
}