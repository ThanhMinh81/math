package com.example.matheasyapp.view.tool.bottomsheft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.databinding.KeyboardToolLayoutBinding
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomsheftKeyboard : BottomSheetDialogFragment() {

    lateinit var binding: KeyboardToolLayoutBinding

    private lateinit var viewModel: KeyboardViewModel

    lateinit var keyKeyboard: String;

    private var enableInput: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        enableInput = true

        binding = KeyboardToolLayoutBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(KeyboardViewModel::class.java)

        // get key
        keyKeyboard = viewModel.getKeyArgumentValue().toString()

        binding.btn0.setOnClickListener { onNumberButtonClick("0") }
        binding.btn1.setOnClickListener { onNumberButtonClick("1") }
        binding.btn2.setOnClickListener { onNumberButtonClick("2") }
        binding.btn3.setOnClickListener { onNumberButtonClick("3") }
        binding.btn4.setOnClickListener { onNumberButtonClick("4") }
        binding.btn5.setOnClickListener { onNumberButtonClick("5") }
        binding.btn6.setOnClickListener { onNumberButtonClick("6") }
        binding.btn7.setOnClickListener { onNumberButtonClick("7") }
        binding.btn8.setOnClickListener { onNumberButtonClick("8") }
        binding.btn9.setOnClickListener { onNumberButtonClick("9") }

        binding.btn00.setOnClickListener { onNumberButtonClick("00") }

        binding.btnClear.setOnClickListener { onClearClick() }
        binding.btnAC.setOnClickListener { clearAllClick() }

        return binding.root
    }



    private fun clearAllClick() {
        if (keyKeyboard.equals("principalAmount")) {
            viewModel.setPrincipalLoanAmountValue("")
        } else if (keyKeyboard.equals("interest")) {
            viewModel.setInterestValue("")
        } else if (keyKeyboard.equals("barrowtime")) {
            viewModel.setInterestValue("")
        } else if (keyKeyboard.equals("payinterest")) {
            viewModel.setPayInterestValue("")
        } else if (keyKeyboard.equals("money_bill")) {
            viewModel.setValueMoneyBill("")
        } else if (keyKeyboard.equals("person_amount")) {
            viewModel.setValueAmountPerson("")
        } else if (keyKeyboard.equals("money_give")) {
            viewModel.setValueAmountMoneyGive("")
        } else if (keyKeyboard.equals("percent_give")) {
            viewModel.setValuePercentMoneyGive("")
        } else if (keyKeyboard.equals("money_scot")) {
            viewModel.setValueAmountScot("")
        } else if (keyKeyboard.equals("percent_vat")) {
            viewModel.setValueAmountVAT("")
        } else if (keyKeyboard.equals("value_tax")) {
            viewModel.setValueTax("")
        } else if (keyKeyboard.equals("value_cost")) {
            viewModel.setValueCost("")
        } else if (keyKeyboard.equals("value_distance")) {
            viewModel.setValueDistance("")
        } else if (keyKeyboard.equals("value_material")) {
            viewModel.setValueMaterial("")
        } else if (keyKeyboard.equals("value_gas")) {
            viewModel.setValueGas("")
        } else if (keyKeyboard.equals("money_level_save")) {
            viewModel.setValueMoneyLevel("")
        } else if (keyKeyboard.equals("interest_save")) {
            viewModel.setInterestValue("")
        } else if (keyKeyboard.equals("value_money_bill_save")) {
            viewModel.setValueMoneyBillSave("")
        } else if (keyKeyboard.equals("value_tax_interest")) {
            viewModel.setValueTaxInterest("")
        } else if (keyKeyboard.equals("price_a")) {
            viewModel.setValuePriceA("")
        } else if (keyKeyboard.equals("price_b")) {
            viewModel.setValuePriceB("")
        } else if (keyKeyboard.equals("price_c")) {
            viewModel.setValuePriceC("")
        } else if (keyKeyboard.equals("price_d")) {
            viewModel.setValuePriceD("")
        } else if (keyKeyboard.equals("quantity_a")) {
            viewModel.setValueQuantityA("")
        } else if (keyKeyboard.equals("quantity_b")) {
            viewModel.setValueQuantityB("")
        } else if (keyKeyboard.equals("quantity_c")) {
            viewModel.setValueQuantityC("")
        } else if (keyKeyboard.equals("quantity_d")) {
            viewModel.setValueQuantityD("")
        } else if (keyKeyboard.equals("unit_a")) {
            viewModel.setValueUnitA("")
        } else if (keyKeyboard.equals("unit_b")) {
            viewModel.setValueUnitB("")
        } else if (keyKeyboard.equals("unit_c")) {
            viewModel.setValueUnitC("")
        } else if (keyKeyboard.equals("unit_d")) {
            viewModel.setValueUnitD("")
        }


    }


    private fun onNumberButtonClick(number: String) {
        if (keyKeyboard.equals("principalAmount")) {
            val currentText = viewModel.getPrincipalLoanAmountValue().value ?: ""
            viewModel.setPrincipalLoanAmountValue(currentText + number)
        } else if (keyKeyboard.equals("interest")) {
            val currentText = viewModel.getInterestValue().value ?: ""
            viewModel.setInterestValue(currentText + number)
        } else if (keyKeyboard.equals("barrowtime")) {
            val currentText = viewModel.getBorrowTimeValue().value ?: ""
            viewModel.setBorrowTimeValue(currentText + number)
        } else if (keyKeyboard.equals("payinterest")) {
            val currentText = viewModel.getPayInterestValue().value ?: ""
            viewModel.setPayInterestValue(currentText + number)
        } else if (keyKeyboard.equals("money_bill")) {
            val currentText = viewModel.getValueMoneyBill().value ?: ""
            viewModel.setValueMoneyBill(currentText + number)
        } else if (keyKeyboard.equals("person_amount")) {
            val currentText = viewModel.getValueAmountPerson().value ?: ""

            viewModel.setValueAmountPerson(currentText + number)

        } else if (keyKeyboard.equals("money_give")) {
            val currentText = viewModel.getValueAmountMoneyGive().value ?: ""


            viewModel.setValueAmountMoneyGive(currentText + number)

        } else if (keyKeyboard.equals("percent_give")) {
            val currentText = viewModel.getValuePercentMoneyGive().value ?: ""


            viewModel.setValuePercentMoneyGive(currentText + number)

        } else if (keyKeyboard.equals("money_scot")) {
            val currentText = viewModel.getValueAmountScot().value ?: ""


            viewModel.setValueAmountScot(currentText + number)

        } else if (keyKeyboard.equals("percent_vat")) {
            val currentText = viewModel.getValueAmountVAT().value ?: ""
            viewModel.setValueAmountVAT(currentText + number)
        } else if (keyKeyboard.equals("value_tax")) {
            val currentText = viewModel.getValueTaxt().value ?: ""
            viewModel.setValueTax(currentText + number)
        } else if (keyKeyboard.equals("value_cost")) {
            val currentText = viewModel.getValueCost().value ?: ""
            viewModel.setValueCost(currentText + number)
        } else if (keyKeyboard.equals("value_distance")) {
            val currentText = viewModel.getValueDistance().value ?: ""
            viewModel.setValueDistance(currentText + number)
        } else if (keyKeyboard.equals("value_material")) {
            val currentText = viewModel.getValueMaterial().value ?: ""
            viewModel.setValueMaterial(currentText + number)
        } else if (keyKeyboard.equals("value_gas")) {
            val currentText = viewModel.getValueGas().value ?: ""
            viewModel.setValueGas(currentText + number)
        } else if (keyKeyboard.equals("money_level_save")) {
            val currentText = viewModel.getValueMoneyLevel().value ?: ""

            viewModel.setValueMoneyLevel(currentText + number)
        } else if (keyKeyboard.equals("interest_save")) {
            val currentText = viewModel.getInterestValue().value ?: ""

            viewModel.setInterestValue(currentText + number)
        } else if (keyKeyboard.equals("value_money_bill_save")) {
            val currentText = viewModel.getValueMoneyBillSave().value ?: ""

            viewModel.setValueMoneyBillSave(currentText + number)
        } else if (keyKeyboard.equals("value_tax_interest")) {
            val currentText = viewModel.getValueTaxInterest().value ?: ""

            viewModel.setValueTaxInterest(currentText + number)
        } else if (keyKeyboard.equals("price_a")) {

            if (enableInput) {
                val currentText = viewModel.getValuePriceA().value ?: ""

                viewModel.setValuePriceA(currentText + number)
            }

        } else if (keyKeyboard.equals("price_b")) {
            if (enableInput) {
                val currentText = viewModel.getValuePriceB().value ?: ""

                viewModel.setValuePriceB(currentText + number)
            }

        } else if (keyKeyboard.equals("price_c")) {
            if (enableInput) {
                val currentText = viewModel.getValuePriceC().value ?: ""

                viewModel.setValuePriceC(currentText + number)
            }

        } else if (keyKeyboard.equals("price_d")) {
            if (enableInput) {
                val currentText = viewModel.getValuePriceD().value ?: ""

                viewModel.setValuePriceD(currentText + number)
            }
        } else if (keyKeyboard.equals("quantity_a")) {
            if (enableInput) {
                val currentText = viewModel.getValueQuantityA().value ?: ""

                viewModel.setValueQuantityA(currentText + number)
            }
        } else if (keyKeyboard.equals("quantity_b")) {
            if (enableInput) {
                val currentText = viewModel.getValueQuantityB().value ?: ""

                viewModel.setValueQuantityB(currentText + number)
            }
        } else if (keyKeyboard.equals("quantity_c")) {
            if (enableInput) {
                val currentText = viewModel.getValueQuantityC().value ?: ""

                viewModel.setValueQuantityC(currentText + number)
            }
        } else if (keyKeyboard.equals("quantity_d")) {
            if (enableInput) {
                val currentText = viewModel.getValueQuantityD().value ?: ""

                viewModel.setValueQuantityD(currentText + number)
            }
        } else if (keyKeyboard.equals("unit_a")) {
            if (enableInput) {
                val currentText = viewModel.getValueUnitA().value ?: ""
                viewModel.setValueUnitA(currentText + number)
            }
        } else if (keyKeyboard.equals("unit_b")) {
            if (enableInput) {
                val currentText = viewModel.getValueUnitB().value ?: ""
                viewModel.setValueUnitB(currentText + number)
            }
        } else if (keyKeyboard.equals("unit_c")) {
            if (enableInput) {
                val currentText = viewModel.getValueUnitC().value ?: ""
                viewModel.setValueUnitC(currentText + number)
            }
        } else if (keyKeyboard.equals("unit_d")) {
            if (enableInput) {
                val currentText = viewModel.getValueUnitD().value ?: ""
                viewModel.setValueUnitD(currentText + number)
            }
        }


    }

    fun onClearClick() {

        if (keyKeyboard.equals("principalAmount")) {
            val currentText = viewModel.getPrincipalLoanAmountValue().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setPrincipalLoanAmountValue(
                    currentText.substring(0, currentText.length - 1)
                )
            }

        } else if (keyKeyboard.equals("interest")) {

            val currentText = viewModel.getInterestValue().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setInterestValue(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("barrowtime")) {

            val currentText = viewModel.getBorrowTimeValue().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setBorrowTimeValue(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("payinterest")) {

            val currentText = viewModel.getPayInterestValue().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setPayInterestValue(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("money_bill")) {
            val currentText = viewModel.getValueMoneyBill().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueMoneyBill(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("person_amount")) {
            val currentText = viewModel.getValueAmountPerson().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueAmountPerson(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("money_give")) {
            val currentText = viewModel.getValueAmountMoneyGive().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueAmountMoneyGive(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("percent_give")) {
            val currentText = viewModel.getValuePercentMoneyGive().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValuePercentMoneyGive(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("money_scot")) {
            val currentText = viewModel.getValueAmountScot().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueAmountScot(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("percent_vat")) {
            val currentText = viewModel.getValueAmountVAT().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueAmountVAT(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("value_tax")) {
            val currentText = viewModel.getValueTaxt().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueTax(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("value_cost")) {
            val currentText = viewModel.getValueCost().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueCost(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("value_distance")) {

            val currentText = viewModel.getValueDistance().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueDistance(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("value_material")) {

            val currentText = viewModel.getValueMaterial().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueMaterial(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("value_gas")) {

            val currentText = viewModel.getValueGas().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueGas(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("money_level_save")) {
            val currentText = viewModel.getValueMoneyLevel().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueMoneyLevel(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("interest_save")) {

            val currentText = viewModel.getInterestValue().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setInterestValue(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("value_money_bill_save")) {

            val currentText = viewModel.getValueMoneyBillSave().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueMoneyBillSave(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("value_tax_interest")) {

            val currentText = viewModel.getValueTaxInterest().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueTaxInterest(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("price_a")) {

            val currentText = viewModel.getValuePriceA().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValuePriceA(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("price_b")) {
            val currentText = viewModel.getValuePriceB().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValuePriceB(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("price_c")) {
            val currentText = viewModel.getValuePriceC().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValuePriceC(currentText.substring(0, currentText.length - 1))
            }

        } else if (keyKeyboard.equals("price_d")) {
            val currentText = viewModel.getValuePriceD().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValuePriceD(currentText.substring(0, currentText.length - 1))
            }

            viewModel.setValuePriceD(currentText + currentText)
        } else if (keyKeyboard.equals("quantity_a")) {
            val currentText = viewModel.getValueQuantityA().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueQuantityA(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("quantity_b")) {
            val currentText = viewModel.getValueQuantityB().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueQuantityB(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("quantity_c")) {
            val currentText = viewModel.getValueQuantityC().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueQuantityC(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("quantity_d")) {
            val currentText = viewModel.getValueQuantityD().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueQuantityD(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("unit_a")) {
            val currentText = viewModel.getValueUnitA().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueUnitA(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("unit_b")) {
            val currentText = viewModel.getValueUnitB().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueUnitB(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("unit_c")) {
            val currentText = viewModel.getValueUnitC().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueUnitC(currentText.substring(0, currentText.length - 1))
            }
        } else if (keyKeyboard.equals("unit_d")) {
            val currentText = viewModel.getValueUnitD().value ?: ""

            if (currentText.isNotEmpty()) {
                viewModel.setValueUnitD(currentText.substring(0, currentText.length - 1))
            }
        }


    }

    fun setEnableInput(boolean: Boolean) {
        enableInput = boolean
    }

}