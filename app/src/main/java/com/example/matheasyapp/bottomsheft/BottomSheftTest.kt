package com.example.matheasyapp.bottomsheft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.matheasyapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheftTest : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(
            R.layout.test,container, false
        )




        return v

    }

}