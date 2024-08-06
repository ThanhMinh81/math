package com.example.matheasyapp.view.calculate

import TextSizeSyncManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.bottomsheft.BottomSheftUnitFromConver
import com.example.matheasyapp.bottomsheft.BottomSheftUnitToConver

import com.example.matheasyapp.databinding.FragmentConversionUnitBinding
import com.example.matheasyapp.livedata.CaculatorViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel



class UnitConverFragment : Fragment() {

    //    private lateinit var  converter: LengthConverter
    private lateinit var binding: FragmentConversionUnitBinding

    private var valueSelected: String = ""

    // check selected item
    private lateinit var selectedFromViewModel: SelectedFromViewModel

    private lateinit var dialogFrom: BottomSheftUnitFromConver

    private lateinit var dialogTo: BottomSheftUnitToConver

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var bottomSheftOnClick: BottomSheftUnitToConver

    private lateinit var viewModelKeyboard: CaculatorViewModel

    private lateinit var unitConversionManager: UnitConversionManager


    private var listLength = ArrayList<String>()
    private var listWeight = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val textSizeAdjuster = TextSizeAuto(requireActivity())

        bottomSheftOnClick = BottomSheftUnitToConver()

        binding = FragmentConversionUnitBinding.inflate(layoutInflater, container, false)

        selectedFromViewModel =
            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        unitConversionManager = UnitConversionManager(sharedPreferencesHelper)

        viewModelKeyboard = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)


        viewModelKeyboard.getLiveTvResult().observe(
            viewLifecycleOwner,
            Observer { newValue ->
                if (newValue.isNotEmpty()) {
                    if (newValue.last().isDigit()) {

                        val value = removeDotsAndCommas(newValue)
                        val result = unitConversionManager.unitConverter(value)
                        binding.fromValue.setText(newValue)
                        binding.toValue.setText(result)
                    } else {
                        binding.fromValue.setText("0")
                    }
                } else {
                    binding.fromValue.setText("0")
                    binding.toValue.setText("0")
                }
            })


        // init value

        if ((sharedPreferencesHelper.getString("from", "null").equals("null"))) {

            sharedPreferencesHelper.saveValueTo("from", "mcg")
            sharedPreferencesHelper.saveValueTo("typeFrom", "weight")
            sharedPreferencesHelper.saveValueTo("to", "g")
            sharedPreferencesHelper.saveValueTo("typeTo", "weight")

        }

        binding.tvValueFrom.text = sharedPreferencesHelper.getString("from", "mcg").toString()
        binding.tvValueTo.text = sharedPreferencesHelper.getString("to", "g").toString()

//        textSizeAdjuster.attachToEditText(binding.toValue)
//        textSizeAdjuster.attachToEditText(binding.fromValue)

        val textSizeSyncManager = TextSizeSyncManager(
            binding.toValue,
            binding.fromValue,
            initialTextSize = 20f,
            minTextSize = 14f,
            thresholdLength = 10,
            scaleStep = 2f
        )

    //init value title
        unitConversionManager.unitConverter("1")
        binding.tvTitleConvert.setText(unitConversionManager.getTitleConvert())

        binding.btnSwichConver.setOnClickListener {
            swichConverter()
        }

        binding.wrapperUnitFrom.setOnClickListener {

            dialogFrom = BottomSheftUnitFromConver()
            dialogFrom.show(childFragmentManager, "MyDialogFragment")

        }


        handleEvent()

        binding.wrapUnitTo.setOnClickListener {
            // Tooo

            bottomSheftOnClick = BottomSheftUnitToConver
                .newInstance(
                    sharedPreferencesHelper.getString("to", "").toString(),
                    sharedPreferencesHelper.getString("from", "").toString()
                )
            bottomSheftOnClick.show(
                requireActivity().getSupportFragmentManager(),
                bottomSheftOnClick.tag
            )

        }

        return binding.root

    }

    private fun handleEvent() {
        // hide dialog from
        selectedFromViewModel.getLiveDataValueFrom()
            .observe(viewLifecycleOwner, Observer { valueFrom ->

                binding.tvValueFrom.setText(valueFrom.toString())

                if (dialogFrom != null) {
                    dialogFrom.dismiss()
                }

                // show dialog to
                if (!(sharedPreferencesHelper.getString("typeFrom").equals(sharedPreferencesHelper.getString("typeTo")))
                    || (sharedPreferencesHelper.getString("from").equals(sharedPreferencesHelper.getString("to")))
                ) {

                    println("305500934505u093092523")
                    try {
                        dialogTo = BottomSheftUnitToConver()
                        dialogTo.show(childFragmentManager, "MyDialogFragment")
                    } catch (e: java.lang.Exception) {
                        println("456456 ${e.printStackTrace()}")
                    }
                }

                if (sharedPreferencesHelper.getString("typeTo").equals(
                        sharedPreferencesHelper.getString("typeFrom")
                    ) && !(sharedPreferencesHelper.getString("to")
                        .equals(sharedPreferencesHelper.getString("from")))
                ) {
                    convertUnit()
                }


            })

        //update text button
        selectedFromViewModel.getLiveDataValueTo().observe(viewLifecycleOwner, Observer { valueTo ->
            binding.tvValueTo.setText(valueTo)
            convertUnit()
        })

        // close selected dialog to
        selectedFromViewModel.getSelectedCloseDialogTo()
            .observe(viewLifecycleOwner, Observer { valueSelected ->

                if (dialogTo != null) {
                    dialogTo.dismiss()
                }

                convertUnit()

            })

        // close dialog to
        selectedFromViewModel.getOnClickCloseDialogTo()
            .observe(viewLifecycleOwner, Observer { value ->
                bottomSheftOnClick.dismiss()

                convertUnit()

            })
    }


    private fun swichConverter() {
        val fromUnit: String = sharedPreferencesHelper.getString("from", "").toString()
        val toUnit: String = sharedPreferencesHelper.getString("to", "").toString()

        binding.tvValueFrom.setText(toUnit)
        binding.tvValueTo.setText(fromUnit)

        sharedPreferencesHelper.saveValueTo("from", toUnit)
        sharedPreferencesHelper.saveValueTo("to", fromUnit)

        convertUnit()

    }

    fun removeDotsAndCommas(input: String): String {
        return input.replace(Regex("[.,]"), "")
    }

    private fun convertUnit() {

        val value = removeDotsAndCommas(binding.fromValue.text.toString())
        println("remove dot ${value}")
        if (value.trim().isNotEmpty()) {
            if (value.toBigDecimal() > "0".toBigDecimal()) {

                if (sharedPreferencesHelper.getString("typeFrom").toString().equals(
                        sharedPreferencesHelper.getString("typeTo").toString()
                    )
                ) {
//                    unitConverter(value)
                    var result: String = unitConversionManager.unitConverter(value)
                    binding.tvTitleConvert.setText(unitConversionManager.getTitleConvert())

                    if (value != "1") {
                        binding.toValue.setText(result.toString())
                    }

                }
            }
        }else {
           unitConversionManager.unitConverter("1")
            binding.tvTitleConvert.setText(unitConversionManager.getTitleConvert())
        }
    }




}