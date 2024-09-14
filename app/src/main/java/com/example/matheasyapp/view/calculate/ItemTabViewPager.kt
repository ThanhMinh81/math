package com.example.matheasyapp.view.calculate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matheasyapp.view.calculate.fragmentUnit.DataUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.LengthUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.SquareUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.TimeUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.VolumeUnitFragment
import com.example.matheasyapp.view.calculate.fragmentUnit.WeightUnitFragment

class ItemTabViewPager(fm : Fragment) : FragmentStateAdapter(fm) {



    override fun getItemCount(): Int  = 6


    override fun createFragment(position: Int): Fragment {
         return when(position){
             0 -> WeightUnitFragment()
             1 -> LengthUnitFragment()
             2 -> SquareUnitFragment()
             3 -> VolumeUnitFragment()
             4 -> TimeUnitFragment()
             5 -> DataUnitFragment()
             else -> WeightUnitFragment()
         }
    }


}