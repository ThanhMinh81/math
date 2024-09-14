package com.example.matheasyapp.view.calculate.fragmentUnit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.view.calculate.format.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterItemConver
import com.example.matheasyapp.bottomsheft.ViewModelSearch
import com.example.matheasyapp.databinding.FragmentSquareUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency


class SquareUnitFragment : Fragment(), OnClickItemConver {


    private lateinit var binding: FragmentSquareUnitBinding
    private lateinit var adapterConvert: AdapterItemConver
    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel: ViewModelSearch


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        listData = ArrayList<Convert>()
        initData()
        binding = FragmentSquareUnitBinding.inflate(inflater, container, false)

        adapterConvert = AdapterItemConver(listData, this, requireActivity())

        selectedFromViewModel =
            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)


        val indexOf = listData.indexOfFirst { it ->
            it.symbol.trim().toString().equals(sharedPreferencesHelper.getString("from"))
        }
        if (indexOf != -1) {
            adapterConvert.setCheckItem(listData[indexOf].symbol)
        }

        binding.rcvSquare.setAdapter(adapterConvert);
        binding.rcvSquare.layoutManager = LinearLayoutManager(requireActivity())

        handleSearchView()

        return binding.root
    }

    private fun handleSearchView() {
        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        searchViewModel.getKeySearch().observe(viewLifecycleOwner, Observer { searchValue ->
            if (searchValue.isNotEmpty() && !(searchValue.equals("empty"))) {

                adapterConvert.getFilter().filter(searchValue, object : Filter.FilterListener {
                    override fun onFilterComplete(count: Int) {
                        val itemCount = adapterConvert.itemCount
                        if (itemCount > 0) {
                            binding.layoutNoData.visibility = View.GONE
                        } else {
                            binding.layoutNoData.visibility = View.VISIBLE
                        }
                    }
                })

            } else {
                adapterConvert.updateFullList()
                binding.layoutNoData.visibility = View.GONE

            }
        })
    }

    private fun initData() {
        listData.add(Convert("Square Millimeter", "mm2", false, "square"))
        listData.add(Convert("Square Centimeter", "cm2", false, "square"))
        listData.add(Convert("Square Meter", "m2", false, "square"))
        listData.add(Convert("Hectare", "ha", false, "square"))
        listData.add(Convert("Square Kilometer", "km2", false, "square"))
        listData.add(Convert("Square Inch", "in2", false, "square"))
        listData.add(Convert("Square Yard", "yd2", false, "square"))
        listData.add(Convert("Square Foot", "ft2", false, "square"))
        listData.add(Convert("Acre", "ac", false, "square"))
        listData.add(Convert("Square Mile", "mi2", false, "square"))
    }

    override fun onClickCurrency(item: Currency) {
        TODO("Not yet implemented")
    }

    override fun onClick(item: Convert) {
        Log.d("893523532", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("from", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeFrom", item.type.toString())
        selectedFromViewModel.setValueFrom(item.symbol)

    }

}