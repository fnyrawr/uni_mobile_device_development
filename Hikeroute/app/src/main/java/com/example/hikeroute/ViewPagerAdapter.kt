package com.example.hikeroute

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hikeroute.fragments.PoiFragment
import com.example.hikeroute.fragments.RouteFragment
import com.example.hikeroute.fragments.WaypointFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                RouteFragment()
            }
            1 -> {
                WaypointFragment()
            }
            2 -> {
                PoiFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

}