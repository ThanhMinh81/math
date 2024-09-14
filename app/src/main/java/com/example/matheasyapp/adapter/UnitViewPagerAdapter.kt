package com.example.matheasyapp.adapter

import android.R
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable


private class UnitViewPagerAdapter(supportFragmentManager: FragmentManager?) :  FragmentPagerAdapter(supportFragmentManager!!) {
    // Initialize arrayList
    var fragmentArrayList: ArrayList<Fragment> = ArrayList()
    var stringArrayList: ArrayList<String> = ArrayList()

//    var imageList: IntArray = intArrayOf(R.drawable.basic, R.drawable.advance, R.drawable.pro)

    // Create constructor
    fun addFragment(fragment: Fragment, s: String) {
        // Add fragment
        fragmentArrayList.add(fragment)
        // Add title
        stringArrayList.add(s)
    }

    @NonNull
    override fun getItem(position: Int): Fragment {
        // return fragment position
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        // Return fragment array listResult size
        return fragmentArrayList.size
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        // Initialize drawable

//        val drawable = ContextCompat.getDrawable(
////            getApplicationContext(),
////            imageList[position]
//        )


        // set bound
//        drawable!!.setBounds(
//            0, 0, drawable.intrinsicWidth,
//            drawable.intrinsicHeight
//        )


        // Initialize spannable image
        val spannableString = SpannableString("" + stringArrayList[position])




        // Set span
//        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        // return spannable string
        return spannableString
    }
}