package com.metalsensor.gold.detector.finder.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.metalsensor.gold.detector.finder.ui.frament.CoinlibraryFragment
import com.metalsensor.gold.detector.finder.ui.frament.ScanFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoinlibraryFragment()
            1 -> ScanFragment()
            else -> CoinlibraryFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}