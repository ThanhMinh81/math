package com.example.matheasyapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.matheasyapp.adapter.AdapterScanner

class ScannerFragment : Fragment() {

    private lateinit var iView: View

    private lateinit var viewPager: ViewPager2

    private lateinit var radioGroup: RadioGroup;

    private lateinit var radioButtonScan: RadioButton;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        iView = inflater.inflate(R.layout.fragment_scanner, container, false)

        radioGroup = iView.findViewById(R.id.rgScanner)

        viewPager = iView.findViewById(R.id.viewPagerScanPage)
        radioButtonScan = iView.findViewById(R.id.rbScan)
        viewPager.adapter = AdapterScanner(this)
        viewPager.currentItem = 0
        radioButtonScan.isChecked = true

        viewPager.isUserInputEnabled = false

        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rbScan -> {
                    viewPager.currentItem = 0
                }

                R.id.rbDraw -> {
                    viewPager.currentItem = 1
                }

                R.id.rbKeyboa -> {
                    viewPager.currentItem = 2
                }
            }
        }


        return iView

    }


}
