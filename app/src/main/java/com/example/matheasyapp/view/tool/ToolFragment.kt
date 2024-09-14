package com.example.matheasyapp.view.tool

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.FragmentToolBinding
import com.example.matheasyapp.view.home.CaculatorFragment
import com.example.matheasyapp.view.tool.activity.TipsActivity
import com.example.matheasyapp.view.tool.activity.BorrowActivity
import com.example.matheasyapp.view.tool.activity.CostMaterialActivity
import com.example.matheasyapp.view.tool.activity.SaveMoneyActivity
import com.example.matheasyapp.view.tool.activity.TaxMoneyActivity
import com.example.matheasyapp.view.tool.activity.UnitPriceActivity


class ToolFragment : Fragment() {

    lateinit var binding: FragmentToolBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolBinding.inflate(inflater, container, false)

//        val spinner: Spinner = findViewById(R.id.spinner)

        binding.layoutCurrency.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("keyString", "currency")

            val calculator = CaculatorFragment()
            calculator.arguments = bundle

            // Thực hiện giao dịch chuyển đổi Fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, calculator)
                .addToBackStack(null)
                .commit()

        }

        binding.layoutUnit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyString", "unit")

            val calculator = CaculatorFragment()
            calculator.arguments = bundle

            // Thực hiện giao dịch chuyển đổi Fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, calculator)
                .addToBackStack(null)
                .commit()
        }

        binding.layoutTips.setOnClickListener {
            val intent = Intent(requireActivity(), TipsActivity::class.java)
            startActivity(intent)
        }

        binding.layoutBorrowMoney.setOnClickListener {
            val intent = Intent(requireActivity(), SaveMoneyActivity::class.java)
            startActivity(intent)
        }
        binding.layoutUnitPrice.setOnClickListener {
            val intent = Intent(requireActivity(), UnitPriceActivity::class.java)
            startActivity(intent)
        }

        binding.layoutSaleTax.setOnClickListener {
            val intent = Intent(requireActivity(), TaxMoneyActivity::class.java)
            startActivity(intent)
        }

        binding.layoutFuelCost.setOnClickListener {
//            val intent = Intent(requireActivity(), SaveMoneyActivity::class.java)
//            startActivity(intent)
            val intent = Intent(requireActivity(), CostMaterialActivity::class.java)
            startActivity(intent)
        }

        binding.layoutLoan.setOnClickListener {
//            val intent = Intent(requireActivity(), UnitPriceActivity::class.java)
//            startActivity(intent)
            val intent = Intent(requireActivity(), BorrowActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }


}