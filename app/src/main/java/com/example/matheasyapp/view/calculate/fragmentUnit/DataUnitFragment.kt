package com.example.matheasyapp.view.calculate.fragmentUnit

import android.os.Bundle
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
import com.example.matheasyapp.databinding.FragmentDataUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency

class DataUnitFragment : Fragment(), OnClickItemConver {

    private lateinit var binding: FragmentDataUnitBinding

    private lateinit var adapterConvert: AdapterItemConver
    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel: ViewModelSearch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDataUnitBinding.inflate(inflater, container, false)

        listData = ArrayList<Convert>()

        initData()

        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        adapterConvert = AdapterItemConver(listData, this, requireActivity())

        selectedFromViewModel = ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        val indexOf = listData.indexOfFirst { it ->
            it.symbol.trim().toString().equals(sharedPreferencesHelper.getString("from"))
        }
        if (indexOf != -1) {
            adapterConvert.setCheckItem(listData[indexOf].symbol)
        }

        binding.rcvData.setAdapter(adapterConvert);
        binding.rcvData.layoutManager = LinearLayoutManager(requireActivity())

        handleSearchView()

        return binding.root
    }

    private fun handleSearchView() {
        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        if (!(searchViewModel.getKeySearch().value.equals("empty")) && searchViewModel.getKeySearch().value!!.isNotEmpty()) {
            adapterConvert.filter.filter(searchViewModel.getKeySearch().value)
        }

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
        listData.add(Convert("Bit", "b", false, "data"))
        listData.add(Convert("Kilobit", "Kb", false, "data"))
        listData.add(Convert("Megabit", "Mb", false, "data"))
        listData.add(Convert("Gigabit", "Gb", false, "data"))
        listData.add(Convert("Terabit", "Tb", false, "data"))
        listData.add(Convert("Byte", "B", false, "data"))
        listData.add(Convert("Kilobyte", "KB", false, "data"))
        listData.add(Convert("Megabyte", "MB", false, "data"))
        listData.add(Convert("Gigabyte", "GB", false, "data"))
        listData.add(Convert("Terabyte", "TB", false, "data"))
    }

    override fun onClickCurrency(item: Currency) {
        TODO("Not yet implemented")
    }

    override fun onClick(item: Convert) {
        sharedPreferencesHelper.saveValueTo("from", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeFrom", item.type.toString())
        selectedFromViewModel.setValueFrom(item.symbol)

    }

}