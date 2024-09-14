package com.example.matheasyapp.view.tool

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NonSwipeableRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return false
    }
}
