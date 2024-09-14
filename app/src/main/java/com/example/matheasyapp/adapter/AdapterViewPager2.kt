package com.example.matheasyapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matheasyapp.view.calculate.CalculateFragment
import com.example.matheasyapp.view.calculate.CurrencyConverterFragment
import com.example.matheasyapp.view.calculate.UnitConverFragment

class AdapterViewPager2(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return CalculateFragment()
            }

            1 -> {
                return UnitConverFragment()
            }

            2 -> {
                return CurrencyConverterFragment()
            }

            else -> {
                return CalculateFragment()
            }
        }
    }

}