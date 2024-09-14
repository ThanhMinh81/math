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
import com.example.matheasyapp.databinding.FragmentTimeUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency

class TimeUnitFragment : Fragment(), OnClickItemConver {

    private lateinit var binding: FragmentTimeUnitBinding

    private lateinit var adapterConvert: AdapterItemConver
    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel: ViewModelSearch


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimeUnitBinding.inflate(inflater, container, false)

        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        listData = ArrayList<Convert>()
        initData()

        adapterConvert = AdapterItemConver(listData, this, requireActivity())

        selectedFromViewModel =
            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())


        val indexOf = listData.indexOfFirst { it ->
            it.symbol.trim().toString().equals(sharedPreferencesHelper.getString("from"))
        }
        if (indexOf != -1) {
            adapterConvert.setCheckItem(listData[indexOf].symbol)
        }

        binding.rcvTime.setAdapter(adapterConvert);
        binding.rcvTime.layoutManager = LinearLayoutManager(requireActivity())

        handleSearchView()

        return binding.root

    }

    private fun initData() {

        listData.add(Convert("Nanosecond", "ns", false, "time"))
        listData.add(Convert("Microsecond", "mu", false, "time"))
        listData.add(Convert("Millisecond", "ms", false, "time"))
        listData.add(Convert("Second", "s", false, "time"))
        listData.add(Convert("Minute", "min", false, "time"))
        listData.add(Convert("Hour", "h", false, "time"))
        listData.add(Convert("days", "d", false, "time"))
        listData.add(Convert("Week", "week", false, "time"))
        listData.add(Convert("Month", "month", false, "time"))
        listData.add(Convert("Year", "year", false, "time"))


    }

    private fun handleSearchView() {
        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        searchViewModel.getKeySearch().observe(viewLifecycleOwner, Observer { searchValue ->
            if (searchValue.isNotEmpty() && !(searchValue.equals("empty"))) {
//                adapterConvert.filter.filter(searchValue)

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

    override fun onClickCurrency(item: Currency) {
        TODO("Not yet implemented")
    }

    override fun onClick(item: Convert) {
        sharedPreferencesHelper.saveValueTo("from", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeFrom", item.type.toString())
        selectedFromViewModel.setValueFrom(item.symbol)
    }
}