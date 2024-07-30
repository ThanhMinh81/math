package com.example.matheasyapp.view.tool

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matheasyapp.databinding.FragmentToolBinding
import com.example.matheasyapp.view.tool.activity.BorrowActivity


class ToolFragment : Fragment() {

    lateinit var binding: FragmentToolBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolBinding.inflate(inflater, container, false)


//        val spinner: Spinner = findViewById(R.id.spinner)




        binding.layoutBorrowMoney.setOnClickListener {
             val intent = Intent(requireActivity(), BorrowActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


}