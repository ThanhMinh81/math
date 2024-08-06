package com.example.matheasyapp.view.tool

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matheasyapp.databinding.FragmentToolBinding
import com.example.matheasyapp.view.tipsscreen.TipsActivity
import com.example.matheasyapp.view.tool.activity.BorrowActivity
import com.example.matheasyapp.view.tool.activity.CostMaterialActivity
import com.example.matheasyapp.view.tool.activity.SaveMoneyActivity
import com.example.matheasyapp.view.tool.activity.TaxMoneyActivity


class ToolFragment : Fragment() {

    lateinit var binding: FragmentToolBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolBinding.inflate(inflater, container, false)


//        val spinner: Spinner = findViewById(R.id.spinner)


        binding.layoutTips.setOnClickListener {
            val intent = Intent(requireActivity(), TipsActivity::class.java)
            startActivity(intent)
        }

        binding.layoutBorrowMoney.setOnClickListener {
            val intent = Intent(requireActivity(), BorrowActivity::class.java)
            startActivity(intent)
        }
        binding.layoutTaxMoney.setOnClickListener {
            val intent = Intent(requireActivity(), TaxMoneyActivity::class.java)
            startActivity(intent)
        }

        binding.layoutCostMaterial.setOnClickListener {
            val intent = Intent(requireActivity(), CostMaterialActivity::class.java)
            startActivity(intent)
        }

        binding.layoutSaveMoney.setOnClickListener {
            val intent = Intent(requireActivity(), SaveMoneyActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


}