package com.example.hikeroute

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RouteEntity::class, WaypointEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun routeDao(): RouteDao
    abstract fun waypointDao(): WaypointDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): AppDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "hikeroute_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!
        }
    }

}