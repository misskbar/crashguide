package com.fernandocejas.sample.features.onboarding

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class OnboardingAdapter(val fm : FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        val NUM_ITEMS = 3
    }
    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return PageOneFragment()
            }
            1 -> {
                return PageTwoFragment()
            }
            2 -> {
                return PageThreeFragment()
            }


        }

        return null
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

}