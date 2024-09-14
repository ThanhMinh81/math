package com.example.matheasyapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matheasyapp.view.home.CameraFragment
import com.example.matheasyapp.view.home.PaintFragment
import com.example.matheasyapp.view.home.ScannerFragment


class AdapterScanner(fragmentActivity: ScannerFragment) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CameraFragment()
            1 -> PaintFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}

// bon

