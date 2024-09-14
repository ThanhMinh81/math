package com.example.matheasyapp.view.calculate.fragmentUnit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.view.calculate.format.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterItemConver
import com.example.matheasyapp.bottomsheft.ViewModelSearch
import com.example.matheasyapp.databinding.FragmentWeightUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency


class WeightUnitFragment : Fragment(), OnClickItemConver {

    private lateinit var binding: FragmentWeightUnitBinding

    private lateinit var convertAdapter: AdapterItemConver

    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel: ViewModelSearch


    interface BottomSheetListener {
        fun onActionClicked(data: String)
    }

    private var listener: BottomSheetListener? = null

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        try {
//            listener = context as WeightUnitFragment.BottomSheetListener
//        } catch (e: ClassCastException) {
//            throw ClassCastException("$context must implement BottomSheetListener")
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())
        binding = FragmentWeightUnitBinding.inflate(inflater, container, false)
        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        listData = ArrayList<Convert>()
        initData()
        initValue()

        var currentCheck = sharedPreferencesHelper.getString("from", "").toString()

        convertAdapter.setCheckItem(currentCheck)
        convertAdapter.notifyDataSetChanged()

        handleSearchView()

        return binding.root

    }

    private fun handleSearchView() {
        if (!(searchViewModel.getKeySearch().value.equals("empty")) && searchViewModel.getKeySearch().value!!.isNotEmpty()) {
            convertAdapter.filter.filter(searchViewModel.getKeySearch().value)
        }

        searchViewModel.getKeySearch().observe(viewLifecycleOwner, Observer { searchValue ->

            if (searchValue.isNotEmpty() && !(searchValue.equals("empty"))) {

                convertAdapter.getFilter().filter(searchValue, object : Filter.FilterListener {
                    override fun onFilterComplete(count: Int) {
                        val itemCount = convertAdapter.itemCount
                        if (itemCount > 0) {
                            binding.layoutNoData.visibility = View.GONE
                        } else {
                            binding.layoutNoData.visibility = View.VISIBLE
                        }
                    }
                })

            } else {
                binding.layoutNoData.visibility = View.GONE
                convertAdapter.updateFullList()
            }
        })
    }


    private fun initValue() {
        selectedFromViewModel = ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        convertAdapter = AdapterItemConver(listData, this, requireActivity())
        convertAdapter.notifyDataSetChanged()
        binding.rcvWeight.layoutManager = LinearLayoutManager(requireActivity())
        binding.rcvWeight.adapter = convertAdapter
    }

    private fun initData() {

        listData.add(Convert("Microgram", "mcg", false, "weight"))
        listData.add(Convert("Milligram", "mg", false, "weight"))
        listData.add(Convert("Gram", "g", false, "weight"))
        listData.add(Convert("Kilogram", "kg", false, "weight"))
        listData.add(Convert("Metric Tonne", "mt", false, "weight"))
        listData.add(Convert("Ounce", "oz", false, "weight"))
        listData.add(Convert("Pound", "lb", false, "weight"))
        listData.add(Convert("Ton", "t", false, "weight"))

    }

    override fun onClickCurrency(item: Currency) {
        TODO("Not yet implemented")
    }

    override fun onClick(item: Convert) {
        Log.d("534542352","53452352345352345")
        sharedPreferencesHelper.saveValueTo("from", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeFrom", item.type.toString())

        selectedFromViewModel.setValueFrom(item.symbol)

    }


}