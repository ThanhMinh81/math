package com.example.matheasyapp.bottomsheft

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.AdapterBottomUnit
import com.example.matheasyapp.databinding.BottomsheftUnitconverBinding
import com.example.matheasyapp.view.calculate.ItemTabViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BottomSheftUnitFromConver : BottomSheetDialogFragment() {

    lateinit var binding: BottomsheftUnitconverBinding

    val tabbarTitle = arrayListOf("Mass", "Lenght", "Acreage", "Volume", "Time", "Data")

    lateinit var viewModel: ViewModelSearch

    override fun onStart() {
        super.onStart()

        dialog?.let {
            val bottomSheet = it as BottomSheetDialog
            val bottomSheetInternal = bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheetInternal!!)

            // Lấy chiều cao của màn hình
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels

            // Đặt chiều cao của BottomSheet theo % màn hình (80%)
            val desiredHeight = (screenHeight * 0.9).toInt()

            bottomSheetInternal.layoutParams.height = desiredHeight
            bottomSheetInternal.layoutParams = bottomSheetInternal.layoutParams

            // Đảm bảo BottomSheet không bị collapse
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            bottomSheetInternal.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bottomSheetInternal.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    behavior.peekHeight = desiredHeight
                }
            })
        }

}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = BottomsheftUnitconverBinding.inflate(inflater, container, false)

        binding.viewPager2.adapter = AdapterBottomUnit(childFragmentManager, 3)

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Mass"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Lenght"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Acreage"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Volume"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Time"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Data"))

        binding.tablayout.getTabAt(0)!!.select()

        setUpLayoutTabbarItem()

        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    viewModel.setKeyData(s.toString())
                } else {
                    viewModel.setKeyData("")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                binding.viewPager2.setCurrentItem(tab.position)

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.viewPager2.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

    private fun setUpLayoutTabbarItem() {
        val tabCount = binding.tablayout.tabCount
        for (i in 0 until tabCount) {
            val tab = binding.tablayout.getTabAt(i)

            tab?.customView = LayoutInflater.from(requireActivity()).inflate(R.layout.item_tabbar_viewpager, null)

            val tabText = tab?.customView?.findViewById<TextView>(R.id.itemName)
            tabText?.text = binding.tablayout.getTabAt(i)!!.text.toString()
        }

        val tabs = binding.tablayout.getChildAt(0) as ViewGroup

        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = 13
            tab.layoutParams = layoutParams
            binding.tablayout.requestLayout()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setKeyData("")

    }


}