package com.example.hikeroute

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RouteEntity::class], version = 1)
abstract class RouteDatabase: RoomDatabase() {

    abstract fun routeDao(): RouteDao

    companion object {
        private var instance: RouteDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): RouteDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, RouteDatabase::class.java, "route_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!
        }
    }

}