package com.example.hikeroute

import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val waypoints: MutableList<Location> = ArrayList()
    private lateinit var routeName: String
    private var tracking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, waypoints)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Create Route"
                }
                1 -> {
                    tab.text = "Saved Routes"
                }
                2 -> {
                    tab.text = "Way points"
                }
                3 -> {
                    tab.text = "POI"
                }
                4 -> {
                    tab.text = "Visual"
                }
            }
        }.attach()
    }
}