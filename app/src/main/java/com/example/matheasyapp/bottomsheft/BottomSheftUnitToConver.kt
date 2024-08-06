package com.example.matheasyapp.bottomsheft

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

    lateinit var listLength: ArrayList<Convert>

    lateinit var listWeight: ArrayList<Convert>

    lateinit var listSquare: ArrayList<Convert>
    lateinit var  listVolumne : ArrayList<Convert>

    lateinit var listTime : ArrayList<Convert>


    private lateinit var listObjectLength: ArrayList<Convert>

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var searchViewModel: SearchViewModel

    private var passedText: String? = null
    private var itemRemove: String? = null
    private var keyUnit: String = ""

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

        keyUnit = sharedPreferencesHelper.getString("from", "null")!!

        Log.d("3253253252",keyUnit)

        // Kiểm tra xem arguments có tồn tại hay không
        arguments?.let { args ->
            passedText = args.getString("passedText")
            itemRemove = args.getString("clearText")
        }

        listWeight = ArrayList()
        listLength = ArrayList()
        listSquare = ArrayList()
        listVolumne = ArrayList()
        listTime = ArrayList()
        initDataLength()



        selectedFromViewModel =
            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

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

            if (!(keyUnit.equals("null"))) {

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

                } else if ((listSquare.find {
                        it.symbol.toString().trim().equals(keyUnit)
                    }) != null) {

                    val indexRemove = listSquare.indexOfFirst { it.symbol.equals(keyUnit) }

                    listSquare.removeAt(indexRemove)
                    listSquare.get(0).check = true

                    sharedPreferencesHelper.saveValueTo("to", listSquare.get(0).symbol.toString())

                    selectedFromViewModel.setValueTo(listSquare.get(0).symbol.toString())

                    converAdapter = AdapterItemConver(listSquare, this, requireActivity())
                } else if ((listVolumne.find { it.symbol.toString().trim().equals(keyUnit) }) != null) {

                    val indexRemove = listVolumne.indexOfFirst { it.symbol.equals(keyUnit) }

                    listVolumne.removeAt(indexRemove)
                    listVolumne.get(0).check = true

                    sharedPreferencesHelper.saveValueTo("to", listVolumne.get(0).symbol.toString())

                    selectedFromViewModel.setValueTo(listVolumne.get(0).symbol.toString())

                    converAdapter = AdapterItemConver(listVolumne, this, requireActivity())

                } else if ((listTime.find { it.symbol.toString().trim().equals(keyUnit) }) != null) {

                    val indexRemove = listTime.indexOfFirst { it.symbol.equals(keyUnit) }

                    listTime.removeAt(indexRemove)
                    listTime.get(0).check = true

                    sharedPreferencesHelper.saveValueTo("to", listTime.get(0).symbol.toString())

                    selectedFromViewModel.setValueTo( listTime.get(0).symbol.toString())

                    converAdapter = AdapterItemConver(listTime, this, requireActivity())

                }
            } else if ((sharedPreferencesHelper.getString("to")
                    .equals(sharedPreferencesHelper.getString("from")))
            ) {

                // trùng 1 giá trị

                when (sharedPreferencesHelper.getString("typeTo")) {
                    "length" -> {

                        val indexItemClear = listLength.indexOfFirst { it ->
                            it.symbol == sharedPreferencesHelper.getString("from")
                        }
                        if (indexItemClear != -1) {
                            listLength.removeAt(indexItemClear)
                        }

                        listLength.get(0).check = true
                        sharedPreferencesHelper.saveValueTo("to", listLength[0].symbol)
                        selectedFromViewModel.setValueTo(listLength[0].symbol)
                        converAdapter = AdapterItemConver(listLength, this, requireActivity())
                    }

                    "weight" -> {

                        val indexItemClear = listWeight.indexOfFirst { it ->
                            it.symbol == sharedPreferencesHelper.getString("from")
                        }
                        if (indexItemClear != -1) {
                            listWeight.removeAt(indexItemClear)
                        }

                        listWeight.get(0).check = true
                        sharedPreferencesHelper.saveValueTo("to", listWeight[0].symbol)
                        selectedFromViewModel.setValueTo(listWeight[0].symbol)
                        converAdapter = AdapterItemConver(listWeight, this, requireActivity())
                    }

                    "square" -> {
                        val indexItemClear = listSquare.indexOfFirst { it ->
                            it.symbol == sharedPreferencesHelper.getString("from")
                        }
                        if (indexItemClear != -1) {
                            listSquare.removeAt(indexItemClear)
                        }

                        listSquare.get(0).check = true
                        sharedPreferencesHelper.saveValueTo("to", listSquare[0].symbol)
                        selectedFromViewModel.setValueTo(listSquare[0].symbol)
                        converAdapter = AdapterItemConver(listSquare, this, requireActivity())
                    }

                    "volume" -> {
                        val indexItemClear = listVolumne.indexOfFirst { it ->
                            it.symbol == sharedPreferencesHelper.getString("from")
                        }
                        if (indexItemClear != -1) {
                            listVolumne.removeAt(indexItemClear)
                        }

                        listVolumne.get(0).check = true
                        sharedPreferencesHelper.saveValueTo("to", listVolumne[0].symbol)
                        selectedFromViewModel.setValueTo(listVolumne[0].symbol)
                        converAdapter = AdapterItemConver(listVolumne, this, requireActivity())
                    }

                    "time" -> {
                        val indexItemClear = listTime.indexOfFirst { it ->
                            it.symbol == sharedPreferencesHelper.getString("from")
                        }
                        if (indexItemClear != -1) {
                            listTime.removeAt(indexItemClear)
                        }

                        listTime.get(0).check = true
                        sharedPreferencesHelper.saveValueTo("to", listTime[0].symbol)
                        selectedFromViewModel.setValueTo(listTime[0].symbol)
                        converAdapter = AdapterItemConver(listTime, this, requireActivity())
                    }

                }
            } else if (itemRemove != null) {
                // click

                when (sharedPreferencesHelper.getString("typeTo")) {
                    "length" -> {

                        val indexItemClear =
                            listLength.indexOfFirst { it -> it.symbol == itemRemove }
                        if (indexItemClear != -1) {
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

                        val indexItemClear =
                            listWeight.indexOfFirst { it -> it.symbol == itemRemove }
                        if (indexItemClear != -1) {
                            listWeight.removeAt(indexItemClear)
                        }

                        val index = listWeight.indexOfFirst { it ->
                            it.symbol.equals(
                                sharedPreferencesHelper.getString("to")
                            )
                        }

                        if (index != -1) {
                            listWeight.get(index).check = true
                        }

                        converAdapter = AdapterItemConver(listWeight, this, requireActivity())
                    }

                    "square" -> {

                        val indexItemClear =
                            listSquare.indexOfFirst { it -> it.symbol == itemRemove }
                        if (indexItemClear != -1) {
                            listSquare.removeAt(indexItemClear)
                        }

                        val index = listSquare.indexOfFirst { it ->
                            it.symbol.equals(
                                sharedPreferencesHelper.getString("to")
                            )
                        }

                        if (index != -1) {
                            listSquare.get(index).check = true
                        }
                        converAdapter = AdapterItemConver(listSquare, this, requireActivity())
                    }

                    "volume" -> {

                        val indexItemClear =
                            listVolumne.indexOfFirst { it -> it.symbol == itemRemove }
                        if (indexItemClear != -1) {
                            listVolumne.removeAt(indexItemClear)
                        }

                        val index = listVolumne.indexOfFirst { it ->
                            it.symbol.equals(
                                sharedPreferencesHelper.getString("to")
                            )
                        }

                        if (index != -1) {
                            listVolumne.get(index).check = true
                        }
                        converAdapter = AdapterItemConver(listVolumne, this, requireActivity())
                    }


                    "time" -> {

                        val indexItemClear =
                            listTime.indexOfFirst { it -> it.symbol == itemRemove }
                        if (indexItemClear != -1) {
                            listTime.removeAt(indexItemClear)
                        }

                        val index = listTime.indexOfFirst { it ->
                            it.symbol.equals(
                                sharedPreferencesHelper.getString("to")
                            )
                        }

                        if (index != -1) {
                            listTime.get(index).check = true
                        }
                        converAdapter = AdapterItemConver(listTime, this, requireActivity())
                    }




                }
            }
        }

        binding.rcvUnitTo.adapter = converAdapter
        binding.rcvUnitTo.layoutManager = LinearLayoutManager(requireActivity())

        return binding.root

    }


    private fun initDataLength() {
        listLength.clear()
        listWeight.clear()

        listLength.add(Convert("Milimeter", "mm", false, "length"))
        listLength.add(Convert("Centimeter", "cm", false, "length"))
        listLength.add(Convert("Meter", "m", false, "length"))
        listLength.add(Convert("Kilometer", "km", false, "length"))
        listLength.add(Convert("Inch", "in", false, "length"))
        listLength.add(Convert("Yard", "yd", false, "length"))
        listLength.add(Convert("US Survey Foot", "ft-us", false, "length"))
        listLength.add(Convert("Foot", "ft", false, "length"))
        listLength.add(Convert("Mile", "mi", false, "length"))

        listWeight.add(Convert("Microgram", "mcg", false, "weight"))
        listWeight.add(Convert("Milligram", "mg", false, "weight"))
        listWeight.add(Convert("Gram", "g", false, "weight"))
        listWeight.add(Convert("Kilogram", "kg", false, "weight"))
        listWeight.add(Convert("Metric Tonne", "mt", false, "weight"))
        listWeight.add(Convert("Ounce", "oz", false, "weight"))
        listWeight.add(Convert("Pound", "lb", false, "weight"))
        listWeight.add(Convert("Ton", "t", false, "weight"))


        listSquare.add(Convert("Square Millimeter", "mm2", false, "square"))
        listSquare.add(Convert("Square Centimeter", "cm2", false, "square"))
        listSquare.add(Convert("Square Meter", "m2", false, "square"))
        listSquare.add(Convert("Hectare", "ha", false, "square"))
        listSquare.add(Convert("Square Kilometer", "km2", false, "square"))
        listSquare.add(Convert("Square Inch", "in2", false, "square"))
        listSquare.add(Convert("Square Yard", "yd2", false, "square"))
        listSquare.add(Convert("Square Foot", "ft2", false, "square"))
        listSquare.add(Convert("Acre", "ac", false, "square"))
        listSquare.add(Convert("Square Mile", "mi2", false, "square"))



        listVolumne.add(Convert("Milliliter", "ml", false, "volume"))
        listVolumne.add(Convert("Centiliter", "cl", false, "volume"))
        listVolumne.add(Convert("Deciliter", "dl", false, "volume"))
        listVolumne.add(Convert("Litre", "l", false, "volume"))
        listVolumne.add(Convert("Hectoliter", "hl", false, "volume"))
        listVolumne.add(Convert("Kiloliter", "kl", false, "volume"))
        listVolumne.add(Convert("Cubic Millimeter", "mm3", false, "volume"))
        listVolumne.add(Convert("Cubic Centimeter", "cm3", false, "volume"))
        listVolumne.add(Convert("Cubic Decimeter", "dm3", false, "volume"))
        listVolumne.add(Convert("Cubic Meter", "m3", false, "volume"))
        listVolumne.add(Convert("Cubic Inch", "in3", false, "volume"))
        listVolumne.add(Convert("Cubic Foot", "ft3", false, "volume"))
        listVolumne.add(Convert("Cubic Yard", "yd3", false, "volume"))
        listVolumne.add(Convert("Gallon (US)", "gal", false, "volume"))
        listVolumne.add(Convert("Liquid Quart (US)", "qt", false, "volume"))
        listVolumne.add(Convert("Panh", "pt", false, "volume"))
        listVolumne.add(Convert("Cup (metric) (US)", "cup", false, "volume"))
        listVolumne.add(Convert("Fluid Ounce", "fl-oz", false, "volume"))
        listVolumne.add(Convert("US Tablespoon", "tbsp", false, "volume"))
        listVolumne.add(Convert("US Teaspoon", "tsp", false, "volume"))
        listVolumne.add(Convert("Oil barrel", "bbl", false, "volume"))



        listTime.add(Convert("Nanosecond", "ns", false,"time"))
        listTime.add(Convert("Microsecond", "mu", false,"time"))
        listTime.add(Convert("Millisecond", "ms", false,"time"))
        listTime.add(Convert("Second", "s", false,"time"))
        listTime.add(Convert("Minute", "min", false,"time"))
        listTime.add(Convert("Hour", "h", false,"time"))
        listTime.add(Convert("days", "d", false,"time"))
        listTime.add(Convert("Week", "week", false,"time"))
        listTime.add(Convert("Month", "month", false,"time"))
        listTime.add(Convert("Year", "year", false,"time"))


    }


    override fun onClick(item: Convert) {

        initDataLength()
        sharedPreferencesHelper.saveValueTo("to", item.symbol.toString())
        sharedPreferencesHelper.saveValueTo("typeTo", item.type.toString())
        selectedFromViewModel.setValueTo(item.symbol.toString())
        // dong dialog
        // dong boottmClick
        if (passedText != null) {
//            println("5325235 ${passedText}")
            selectedFromViewModel.setOnClickCloseDialogTo("close")
        } else {
            println("99999")
            selectedFromViewModel.setSelectedCloseDialogTo("close")
        }
    }





}