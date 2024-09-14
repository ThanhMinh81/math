package com.example.matheasyapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.matheasyapp.view.calculate.fragmentUnit.DataUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.LengthUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.SquareUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.TimeUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.VolumeUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.WeightUnitFragment

class AdapterBottomUnit(fm: FragmentManager, var totalItem: Int) : FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 6
    }


    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                return WeightUnitFragment()
            }

            1 -> {
                return LengthUnitFragment()
            }
            2 -> {
                return  SquareUnitFragment()
            }

            3 -> {
                return  VolumeUnitFragment()
            }
            4 -> {
                return  TimeUnitFragment()
            }
            5 -> {
                return  DataUnitFragment()
            }


            else -> {
                return WeightUnitFragment()
            }
        }

    }
}