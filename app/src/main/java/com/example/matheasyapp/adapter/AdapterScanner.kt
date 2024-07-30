package com.example.matheasyapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matheasyapp.CameraFragment
import com.example.matheasyapp.DrawFragment
import com.example.matheasyapp.KeyboardFragment
import com.example.matheasyapp.ScannerFragment

class AdapterScanner(fragmentActivity: ScannerFragment) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CameraFragment()
            1 -> DrawFragment()
            2 -> KeyboardFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
