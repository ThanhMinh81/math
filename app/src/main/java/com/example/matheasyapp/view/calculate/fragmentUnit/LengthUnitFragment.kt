package com.example.matheasyapp.view.calculate.fragmentUnit

import android.content.Context
import android.os.Bundle
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
import com.example.matheasyapp.databinding.FragmentLengthUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency


class LengthUnitFragment : Fragment(), OnClickItemConver {

    private lateinit var binding: FragmentLengthUnitBinding

    //    private lateinit var arrayAdapter: ArrayAdapter<String>;
    private lateinit var adapterConvert: AdapterItemConver

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel: ViewModelSearch


    private lateinit var listData: ArrayList<Convert>;

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentLengthUnitBinding.inflate(inflater, container, false)
        listData = ArrayList<Convert>();
        initData();

        adapterConvert = AdapterItemConver(listData, this, requireActivity())

        selectedFromViewModel =
            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        searchViewModel = ViewModelProvider(requireActivity()).get(ViewModelSearch::class.java)

        binding.layoutNoData.visibility = View.GONE

        val indexOf = listData.indexOfFirst { it ->
            it.symbol.trim().toString().equals(sharedPreferencesHelper.getString("from"))
        }
        if (indexOf != -1) {
            adapterConvert.setCheckItem(listData[indexOf].symbol)
        }

        binding.rcvLength.setAdapter(adapterConvert);
        binding.rcvLength.layoutManager = LinearLayoutManager(requireActivity())

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
        listData.add(Convert("Milimeter", "mm", false, "length"))
        listData.add(Convert("Centimeter", "cm", false, "length"))
        listData.add(Convert("Meter", "m", false, "length"))
        listData.add(Convert("Kilometer", "km", false, "length"))
        listData.add(Convert("Inch", "in", false, "length"))
        listData.add(Convert("Yard", "yd", false, "length"))
        listData.add(Convert("US Survey Foot", "ft-us", false, "length"))
        listData.add(Convert("Foot", "ft", false, "length"))
        listData.add(Convert("Mile", "mi", false, "length"))
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