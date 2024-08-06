package com.example.matheasyapp.view.calculate.fragmentUnit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterItemConver
import com.example.matheasyapp.databinding.FragmentDataUnitBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert

class DataUnitFragment : Fragment(), OnClickItemConver {

    private lateinit var binding: FragmentDataUnitBinding

    private lateinit var adapterConvert: AdapterItemConver
    private lateinit var listData: ArrayList<Convert>;

    private lateinit var selectedFromViewModel: SelectedFromViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDataUnitBinding.inflate(inflater, container, false)

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

        binding.rcvData.setAdapter(adapterConvert);
        binding.rcvData.layoutManager = LinearLayoutManager(requireActivity())


        return binding.root
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

    override fun onClick(item: Convert) {
        sharedPreferencesHelper.saveValueTo("from", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeFrom", item.type.toString())
        selectedFromViewModel.setValueFrom(item.symbol)

    }

}