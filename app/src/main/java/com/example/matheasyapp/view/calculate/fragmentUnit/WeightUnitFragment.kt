package com.example.matheasyapp.view.calculate.fragmentUnit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterItemConver
import com.example.matheasyapp.databinding.FragmentWeightUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.ConvertViewModel
import com.example.matheasyapp.livedata.InitKeyViewModel
import com.example.matheasyapp.livedata.SearchViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.History


class WeightUnitFragment : Fragment() , OnClickItemConver {

    private lateinit var binding: FragmentWeightUnitBinding

    private  lateinit var  convertAdapter : AdapterItemConver

    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var initKeyFrom : InitKeyViewModel

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel : SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())
        binding = FragmentWeightUnitBinding.inflate(inflater, container, false)

        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        searchViewModel.getSearchValue().observe(viewLifecycleOwner, Observer {
            searchValue -> searchFunc(searchValue)
        })

        initValue()
        initData()

        var currentCheck = sharedPreferencesHelper.getString("from","").toString()

        convertAdapter.setCheckItem(currentCheck)
        convertAdapter.notifyDataSetChanged()

        return binding.root

    }

    private fun searchFunc(searchValue: String?) {
             var arraySearch : ArrayList<Convert> = ArrayList()

        listData.forEach { it->
            if(it.name.lowercase().contains(searchValue!!.lowercase().trim()) || searchValue.lowercase().trim().contains(it.name) ){
                 arraySearch.add(it)
            }
        }


    }

    private fun initValue() {
        listData = ArrayList<Convert>()
        selectedFromViewModel = ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        convertAdapter = AdapterItemConver(listData,this, requireActivity())

        binding.rcvWeight.layoutManager = LinearLayoutManager(requireActivity())
        binding.rcvWeight.adapter = convertAdapter
    }

    private fun initData() {

        listData.add(Convert("Microgram", "mcg", false,"weight"))
        listData.add(Convert("Milligram", "mg", false,"weight"))
        listData.add(Convert("Gram", "g", false,"weight"))
        listData.add(Convert("Kilogram", "kg", false,"weight"))
        listData.add(Convert("Metric Tonne", "mt", false,"weight"))
        listData.add(Convert("Ounce", "oz", false,"weight"))
        listData.add(Convert("Pound", "lb", false,"weight"))
        listData.add(Convert("Ton", "t", false,"weight"))


    }

    override fun onClick(item: Convert) {
        sharedPreferencesHelper.saveValueTo("from",item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeFrom",item.type.toString())
        selectedFromViewModel.setValueFrom(item.symbol)
    }

}