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
import com.example.matheasyapp.databinding.FragmentVolumeBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency

class VolumeUnitFragment : Fragment(), OnClickItemConver {

    private lateinit var binding: FragmentVolumeBinding

    private lateinit var adapterConvert: AdapterItemConver
    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var searchViewModel: ViewModelSearch


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVolumeBinding.inflate(inflater, container, false)

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

        binding.rcvVolume.setAdapter(adapterConvert);
        binding.rcvVolume.layoutManager = LinearLayoutManager(requireActivity())

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
                binding.layoutNoData.visibility = View.GONE
                adapterConvert.updateFullList()
            }
        })
    }

    private fun initData() {

        listData.add(Convert("Milliliter", "ml", false, "volume"))
        listData.add(Convert("Centiliter", "cl", false, "volume"))
        listData.add(Convert("Deciliter", "dl", false, "volume"))
        listData.add(Convert("Litre", "l", false, "volume"))
        listData.add(Convert("Hectoliter", "hl", false, "volume"))
        listData.add(Convert("Kiloliter", "kl", false, "volume"))
        listData.add(Convert("Cubic Millimeter", "mm3", false, "volume"))
        listData.add(Convert("Cubic Centimeter", "cm3", false, "volume"))
        listData.add(Convert("Cubic Decimeter", "dm3", false, "volume"))
        listData.add(Convert("Cubic Meter", "m3", false, "volume"))
        listData.add(Convert("Cubic Inch", "in3", false, "volume"))
        listData.add(Convert("Cubic Foot", "ft3", false, "volume"))

        listData.add(Convert("Cubic Yard", "yd3", false, "volume"))
        listData.add(Convert("Gallon (US)", "gal", false, "volume"))
        listData.add(Convert("Liquid Quart (US)", "qt", false, "volume"))

        listData.add(Convert("Panh", "pt", false, "volume"))
        listData.add(Convert("Cup (metric) (US)", "cup", false, "volume"))
        listData.add(Convert("Fluid Ounce", "fl-oz", false, "volume"))
        listData.add(Convert("US Tablespoon", "tbsp", false, "volume"))
        listData.add(Convert("US Teaspoon", "tsp", false, "volume"))
        listData.add(Convert("Oil barrel", "bbl", false, "volume"))


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