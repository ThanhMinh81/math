package com.example.matheasyapp.view.home.pdf

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matheasyapp.view.home.pdf.fragment.SearchPDFFragment
import com.example.matheasyapp.view.home.pdf.fragment.StartPDFFragment
import com.example.matheasyapp.view.home.pdf.fragment.ViewPDFFragment

class ViewPagerAdapter(fm: Fragment) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ViewPDFFragment()
            1 -> SearchPDFFragment()
            else -> ViewPDFFragment()
        }
    }
}
