package com.example.matheasyapp.adapter



import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.matheasyapp.view.calculate.CalculateFragment
import com.example.matheasyapp.view.calculate.CurrencyConverterFragment
import com.example.matheasyapp.view.calculate.UnitConverFragment


class CaculatorPagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = listOf(
        CalculateFragment(),
        UnitConverFragment(),
        CurrencyConverterFragment()
    )

    override fun getCount(): Int {
        return 3;
    }

    override fun getItem(position: Int): Fragment {

        when(position) {
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

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Tab 1"
            }
            1 -> {
                return "Tab 2"
            }
            2 -> {
                return "Tab 3"
            }
        }
        return super.getPageTitle(position)
    }

    fun getFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}