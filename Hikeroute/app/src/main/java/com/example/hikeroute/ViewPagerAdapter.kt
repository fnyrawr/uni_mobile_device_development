package com.example.hikeroute

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hikeroute.fragments.*

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                RouteFragment()
            }
            1 -> {
                SavedRoutesFragment()
            }
            2 -> {
                WaypointFragment()
            }
            3 -> {
                PoiFragment()
            }
            4 -> {
                VisualFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

}