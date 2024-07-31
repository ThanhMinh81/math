package com.example.matheasyapp.bottomsheft.money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterItemConver
import com.example.matheasyapp.adapter.AdapterMoneyConverter
import com.example.matheasyapp.databinding.BottomsheftMoneyconverBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.ConvertMoneyViewModel
import com.example.matheasyapp.model.Convert
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheftFromMoney : BottomSheetDialogFragment(), OnClickItemConver {

    lateinit var binding: BottomsheftMoneyconverBinding

    private lateinit var converAdapter: AdapterMoneyConverter

    private lateinit var listCountry: ArrayList<Convert>

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var selectedViewModel: ConvertMoneyViewModel

    // fromMoney
    // toMoney

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheftMoneyconverBinding.inflate(inflater, container, false)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        selectedViewModel =
            ViewModelProvider(requireActivity()).get(ConvertMoneyViewModel::class.java)
        var valueToSelected = sharedPreferencesHelper.getString("toMoney").toString()
        var valueFromSelected = sharedPreferencesHelper.getString("fromMoney").toString()


        initDataList()

        if ((listCountry.find { it.symbol.equals(valueToSelected) }) != null) {
            val indexRemove = listCountry.indexOfFirst { it.symbol.equals(valueToSelected) }
            if (indexRemove != -1) {
                listCountry.removeAt(indexRemove)
            }

            val indexCheck = listCountry.indexOfFirst { it.symbol.equals(valueFromSelected) }
            if (indexCheck != -1) {
                listCountry.get(indexCheck).check = true
            }
            sharedPreferencesHelper.saveValueTo("fromMoney", listCountry.get(0).symbol.toString())

//            selectedViewModel.setValueMoneyFrom(listCountry.get(0).symbol)

            converAdapter = AdapterMoneyConverter(listCountry, this, requireActivity())
        }


        binding.rcvUnitTo.adapter = converAdapter
        binding.rcvUnitTo.layoutManager = LinearLayoutManager(requireActivity())


        return binding.root
    }

    private fun initDataList() {
        listCountry = ArrayList();
        listCountry.add(Convert("United States Dollar", "USD", false))
        listCountry.add(Convert("United Arab Emirates Dirham", "AED", false))
        listCountry.add(Convert("Afghan Afghan", "AFN", false))
        listCountry.add(Convert("Albanian Lek", "AMD", false))
        listCountry.add(Convert("Armenian Dram", "amd", false))
    }


    override fun onClick(item: Convert) {
        initDataList()
        sharedPreferencesHelper.saveValueTo("fromMoney", item.symbol.toString())
        selectedViewModel.setValueMoneyFrom(item.symbol.toString())
        selectedViewModel.setOnClickCloseFrom("true")

    }
}