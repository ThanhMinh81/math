package com.example.matheasyapp.view.calculate

import TextSizeSyncManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.bottomsheft.BottomSheftUnitFromConver
import com.example.matheasyapp.bottomsheft.BottomSheftUnitToConver

import com.example.matheasyapp.databinding.FragmentConversionUnitBinding
import com.example.matheasyapp.livedata.CaculatorViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel
import com.example.matheasyapp.view.calculate.format.SharedPreferencesHelper
import com.example.matheasyapp.view.calculate.fragmentUnit.WeightUnitFragment
import java.math.BigDecimal


class UnitConverFragment : Fragment()  {

    //    private lateinit var  converter: LengthConverter
    private lateinit var binding: FragmentConversionUnitBinding



    // check selected item
    private lateinit var selectedFromViewModel: SelectedFromViewModel

    private lateinit var dialogFrom: BottomSheftUnitFromConver

    private lateinit var dialogTo: BottomSheftUnitToConver

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var bottomSheftOnClick: BottomSheftUnitToConver

    private lateinit var viewModelKeyboard: CaculatorViewModel

    private lateinit var unitConversionManager: UnitConversionManager

    private var isObserverRegistered = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialogFrom = BottomSheftUnitFromConver()
        dialogTo = BottomSheftUnitToConver()

        bottomSheftOnClick = BottomSheftUnitToConver()

        binding = FragmentConversionUnitBinding.inflate(layoutInflater, container, false)

//        selectedFromViewModel =
//            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        unitConversionManager = UnitConversionManager(sharedPreferencesHelper)

        viewModelKeyboard = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)


        viewModelKeyboard.getLiveTvResult().observe(
            viewLifecycleOwner,
            Observer { newValue ->
                if (newValue.isNotEmpty()) {
                    if (newValue.last().isDigit()) {
                        Log.d("5452523",newValue.toString())
                        val value = removeDotsAndCommas(newValue)
                        if (value.toBigDecimal() > BigDecimal(0)) {
                            val result = unitConversionManager.unitConverter(value)
                            binding.fromValue.setText(newValue)
                            binding.toValue.setText(result)
                        }
                    } else {
                        binding.fromValue.setText("0")
                    }
                } else {
                    binding.fromValue.setText("0")
                    binding.toValue.setText("0")
                }
            })

        if(binding.fromValue.text.isEmpty()){
            binding.fromValue.setText("0")
            binding.toValue.setText("0")
        }



        // init value

        if ((sharedPreferencesHelper.getString("from", "null").equals("null"))) {
            sharedPreferencesHelper.saveValueTo("from", "mcg")
            sharedPreferencesHelper.saveValueTo("typeFrom", "weight")
            sharedPreferencesHelper.saveValueTo("to", "g")
            sharedPreferencesHelper.saveValueTo("typeTo", "weight")
        }

        binding.tvValueFrom.text = sharedPreferencesHelper.getString("from", "mcg").toString()
        binding.tvValueTo.text = sharedPreferencesHelper.getString("to", "g").toString()

        //init value title
        unitConversionManager.unitConverter("1")

        updateTitleConverter(unitConversionManager.getTitleConvert())

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

        // Đảm bảo rằng Observer chỉ được đăng ký một lần
        if (!isObserverRegistered) {
            Log.d("5452525","523523525325345")

            selectedFromViewModel = ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

            selectedFromViewModel.getLiveDataValueFrom().observe(viewLifecycleOwner, Observer { valueFrom ->

                    binding.tvValueFrom.setText(valueFrom.toString())

                    if (dialogFrom.isVisible && dialogFrom.isAdded) {
                        dialogFrom.dismiss()
                    }

                    // show dialog to
                    if (!(sharedPreferencesHelper.getString("typeFrom").equals(sharedPreferencesHelper.getString("typeTo")))
                        || (sharedPreferencesHelper.getString("from")
                            .equals(sharedPreferencesHelper.getString("to")))
                    ) {

                                dialogTo = BottomSheftUnitToConver()
                                dialogTo.show(childFragmentManager, "MyDialogFragment")

                                dialogTo.setOnDismissListener {
                                    if (sharedPreferencesHelper.getString("typeTo").equals(
                                            sharedPreferencesHelper.getString("typeFrom")
                                        ) && !(sharedPreferencesHelper.getString("to")
                                            .equals(sharedPreferencesHelper.getString("from")))
                                    ) {

                                        convertUnit()
                                    }
                                }


                    }

                    if (sharedPreferencesHelper.getString("typeTo").equals(sharedPreferencesHelper.getString("typeFrom")) && !(sharedPreferencesHelper.getString("to")
                            .equals(sharedPreferencesHelper.getString("from")))
                    ) {

                        convertUnit()
                    }


                })

            //update text button
            selectedFromViewModel.getLiveDataValueTo().observe(viewLifecycleOwner, Observer { valueTo ->
                binding.tvValueTo.setText(valueTo)
                if (sharedPreferencesHelper.getString("typeTo").equals(
                        sharedPreferencesHelper.getString("typeFrom")
                    ) && !(sharedPreferencesHelper.getString("to")
                        .equals(sharedPreferencesHelper.getString("from")))
                ) {
                    convertUnit()
                }
            })

            // close selected dialog to
            selectedFromViewModel.getSelectedCloseDialogTo()
                .observe(viewLifecycleOwner, Observer { valueSelected ->

                    if (dialogTo.isVisible && dialogTo.isAdded) {
                        dialogTo.dismiss()
                    }

                    if (sharedPreferencesHelper.getString("typeTo").equals(
                            sharedPreferencesHelper.getString("typeFrom")
                        ) && !(sharedPreferencesHelper.getString("to")
                            .equals(sharedPreferencesHelper.getString("from")))
                    ) {
                        convertUnit()
                    }

                })

            // close dialog to
            selectedFromViewModel.getOnClickCloseDialogTo()
                .observe(viewLifecycleOwner, Observer { value ->
                     if(bottomSheftOnClick.isAdded && bottomSheftOnClick.isVisible){
                         bottomSheftOnClick.dismiss()
                     }

                    if (sharedPreferencesHelper.getString("typeTo").equals(
                            sharedPreferencesHelper.getString("typeFrom")
                        ) && !(sharedPreferencesHelper.getString("to")
                            .equals(sharedPreferencesHelper.getString("from")))
                    ) {
                        convertUnit()
                    }
                })

            isObserverRegistered = true
        }
    }


    private fun swichConverter() {
        val fromUnit: String = sharedPreferencesHelper.getString("from", "").toString()
        val toUnit: String = sharedPreferencesHelper.getString("to", "").toString()

        binding.tvValueFrom.setText(toUnit)
        binding.tvValueTo.setText(fromUnit)

        sharedPreferencesHelper.saveValueTo("from", toUnit)
        sharedPreferencesHelper.saveValueTo("to", fromUnit)


            if (sharedPreferencesHelper.getString("typeTo").equals(
                    sharedPreferencesHelper.getString("typeFrom")
                ) && !(sharedPreferencesHelper.getString("to")
                    .equals(sharedPreferencesHelper.getString("from")))
            ) {
                convertUnit()
            }


    }

    fun removeDotsAndCommas(input: String): String {
        return input.replace(Regex("[.,]"), "")
    }

    private fun convertUnit() {

        val value = removeDotsAndCommas(binding.fromValue.text.toString())

        if (value.trim().isNotEmpty()) {
            if (value.toBigDecimal() > "0".toBigDecimal()) {
                if (sharedPreferencesHelper.getString("typeFrom").toString().equals(sharedPreferencesHelper.getString("typeTo").toString())) {
                    var result: String = unitConversionManager.unitConverter(value)
                    updateTitleConverter(unitConversionManager.getTitleConvert())
                    binding.toValue.setText(result.toString())
                    binding.tvValueTo.setText(sharedPreferencesHelper.getString("to"))
                }else {
                    Log.d("64636363","khac type")
                }
            }else{
                unitConversionManager.unitConverter("1")
                binding.tvValueTo.setText(sharedPreferencesHelper.getString("to"))
                updateTitleConverter(unitConversionManager.getTitleConvert())
            }
        } else {
            unitConversionManager.unitConverter("1")
            binding.tvValueTo.setText(sharedPreferencesHelper.getString("to"))
            updateTitleConverter(unitConversionManager.getTitleConvert())
        }
    }


    fun updateTitleConverter(value: String) {
        val parts = value.split("=")
        if (parts.size == 2) {
            binding.tvTitleFrom.setText(parts[0].toString())
            binding.tvTitleTo.setText(parts[1].toString())
        }
    }






}