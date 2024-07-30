package com.example.matheasyapp.bottomsheft

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterItemConver
import com.example.matheasyapp.databinding.BottomSheeftUnitToConvertBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.SearchViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.model.Convert
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheftUnitToConver : BottomSheetDialogFragment(), OnClickItemConver {

    lateinit var binding: BottomSheeftUnitToConvertBinding

//    private lateinit var arrayAdapter: ArrayAdapter<String>;

    private lateinit var converAdapter: AdapterItemConver

    private lateinit var selectedFromViewModel: SelectedFromViewModel

    public lateinit var listLength: ArrayList<Convert>

    public lateinit var listWeight: ArrayList<Convert>

    private lateinit var listObjectLength: ArrayList<Convert>

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var searchViewModel: SearchViewModel

    private var passedText: String? = null
    private var itemRemove: String? = null

    companion object {
        fun newInstance(currentSelectedTo: String, itemClear: String): BottomSheftUnitToConver {
            val fragment = BottomSheftUnitToConver()
            val args = Bundle()
            args.putString("passedText", currentSelectedTo)
            args.putString("clearText", itemClear)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        binding = BottomSheeftUnitToConvertBinding.inflate(inflater, container, false)

        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        val keyUnit = sharedPreferencesHelper.getString("from", "null")

        // Kiểm tra xem arguments có tồn tại hay không
        arguments?.let { args ->
            passedText = args.getString("passedText")
            itemRemove = args.getString("clearText")
        }

        listWeight = ArrayList()
        listLength = ArrayList()
        initDataLength()
        initDataWeight()

        // 2 cái trùng nhau không show
        // 2 cái khác nhau show

        selectedFromViewModel = ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("Text changed: ${s.toString()}")
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })


        if (!(sharedPreferencesHelper.getString("typeFrom")
                .equals(sharedPreferencesHelper.getString("typeTo")))) {
         // khác kiểu convert

            if ((listLength.find { it.symbol.equals(keyUnit) }) != null) {

                val indexRemove = listLength.indexOfFirst { it.symbol.equals(keyUnit) }
                listLength.removeAt(indexRemove)

                //khong hien thi
                listLength.get(0).check = true
                sharedPreferencesHelper.saveValueTo("to", listLength.get(0).symbol.toString())

                selectedFromViewModel.setValueTo(listLength.get(0).symbol)

                converAdapter = AdapterItemConver(listLength, this, requireActivity())

            } else if ((listWeight.find { it.symbol.equals(keyUnit) }) != null) {

                val indexRemove = listWeight.indexOfFirst { it.symbol.equals(keyUnit) }

                listWeight.removeAt(indexRemove)
                listWeight.get(0).check = true

                sharedPreferencesHelper.saveValueTo("to", listWeight.get(0).symbol.toString())

                selectedFromViewModel.setValueTo(listWeight.get(0).symbol.toString())

                converAdapter = AdapterItemConver(listWeight, this, requireActivity())

            } else if (keyUnit.equals("null")) {

            }
        }else if((sharedPreferencesHelper.getString("to").equals(sharedPreferencesHelper.getString("from")))){

            // trùng 1 giá trị

            when (sharedPreferencesHelper.getString("typeTo")) {
                "length" -> {

                    val indexItemClear = listLength.indexOfFirst { it -> it.symbol == sharedPreferencesHelper.getString("from") }
                    if(indexItemClear != -1)
                    {
                        listLength.removeAt(indexItemClear)
                    }

                    listLength.get(0).check = true
                    sharedPreferencesHelper.saveValueTo("to",listLength[0].symbol)
                    selectedFromViewModel.setValueTo(listLength[0].symbol)
                    converAdapter = AdapterItemConver(listLength, this, requireActivity())
                }

                "weight" -> {

                    val indexItemClear = listWeight.indexOfFirst { it -> it.symbol == sharedPreferencesHelper.getString("from") }
                    if(indexItemClear != -1)
                    {
                        listWeight.removeAt(indexItemClear)
                    }

                    listWeight.get(0).check = true
                    sharedPreferencesHelper.saveValueTo("to",listWeight[0].symbol)
                    selectedFromViewModel.setValueTo(listWeight[0].symbol)
                    converAdapter = AdapterItemConver(listWeight, this, requireActivity())
                }

            }
        }
        else {
            // click
            println("54739523752")
            when (sharedPreferencesHelper.getString("typeTo")) {
                "length" -> {
                    println("dofsaoif ${passedText}")

                    val indexItemClear = listLength.indexOfFirst { it -> it.symbol == itemRemove }
                    if(indexItemClear != -1)
                    {
                        listLength.removeAt(indexItemClear)
                    }

                    val index = listLength.indexOfFirst { it ->
                        it.symbol.equals(
                            sharedPreferencesHelper.getString("to")
                        )
                    }
                    if (index != -1) {
                        listLength.get(index).check = true
                    }
                    converAdapter = AdapterItemConver(listLength, this, requireActivity())
                }

                "weight" -> {

                    val indexItemClear = listWeight.indexOfFirst { it -> it.symbol == itemRemove }
                    if(indexItemClear != -1)
                    {
                        listWeight.removeAt(indexItemClear)
                    }

                    val index = listWeight.indexOfFirst { it ->
                        it.symbol.equals(
                            sharedPreferencesHelper.getString("to")
                        )
                    }

                    listWeight.get(index).check = true

                    converAdapter = AdapterItemConver(listWeight, this, requireActivity())
                }
            }
        }

        binding.rcvUnitTo.adapter = converAdapter
        binding.rcvUnitTo.layoutManager = LinearLayoutManager(requireActivity())

        return binding.root

    }


    private fun initDataLength() {
        listLength.clear()
        listLength.add(Convert("Milimeter", "mm", false, "length"))
        listLength.add(Convert("Centimeter", "cm", false, "length"))
        listLength.add(Convert("Meter", "m", false, "length"))
        listLength.add(Convert("Kilometer", "km", false, "length"))
        listLength.add(Convert("Inch", "in", false, "length"))
        listLength.add(Convert("Yard", "yd", false, "length"))
        listLength.add(Convert("US Survey Foot", "ft-us", false, "length"))
        listLength.add(Convert("Foot", "ft", false, "length"))
        listLength.add(Convert("Mile", "mi", false, "length"))

    }

    private fun initDataWeight() {
        listWeight.clear()
        listWeight.add(Convert("Microgram", "mcg", false, "weight"))
        listWeight.add(Convert("Milligram", "mg", false, "weight"))
        listWeight.add(Convert("Gram", "g", false, "weight"))
        listWeight.add(Convert("Kilogram", "kg", false, "weight"))
        listWeight.add(Convert("Metric Tonne", "mt", false, "weight"))
        listWeight.add(Convert("Ounce", "oz", false, "weight"))
        listWeight.add(Convert("Pound", "lb", false, "weight"))
        listWeight.add(Convert("Ton", "t", false, "weight"))

    }


    override fun onClick(item: Convert) {
        initDataWeight()
        initDataLength()
        sharedPreferencesHelper.saveValueTo("to", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeTo", item.type.toString())
        selectedFromViewModel.setValueTo(item.symbol.toString())
        // dong dialog
        // dong boottmClick
        if (passedText != null) {
//            println("5325235 ${passedText}")
            selectedFromViewModel.setOnClickBottomTo("close")
        } else {
            println("99999")
            selectedFromViewModel.setSelectedOnClick("close")
        }
    }


}