package com.example.matheasyapp.bottomsheft

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.matheasyapp.adapter.AdapterBottomUnit
import com.example.matheasyapp.bottomsheft.BottomSheftStateCaculator.callBackFunction
import com.example.matheasyapp.databinding.BottomsheftUnitconverBinding
import com.example.matheasyapp.livedata.ConvertViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class BottomSheftUnitFromConver : BottomSheetDialogFragment() {

    lateinit var binding: BottomsheftUnitconverBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomsheftUnitconverBinding.inflate(inflater, container, false)

        binding.viewPager2.adapter = AdapterBottomUnit(childFragmentManager, 3)
        binding.viewPager2.currentItem = 0

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Weight"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Lenght"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Square"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Volume"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Time"))

        binding.tablayout.getTabAt(0)!!.select()

        binding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager2.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.viewPager2.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.tablayout.getTabAt(position)!!.select()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


        return binding.root

    }



}