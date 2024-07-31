package com.example.matheasyapp.view.tool.bottomsheft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.bottomsheft.BottomSheftUnitToConver
import com.example.matheasyapp.databinding.KeyboardToolLayoutBinding
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomsheftKeyboard : BottomSheetDialogFragment() {

    lateinit var binding: KeyboardToolLayoutBinding

    private lateinit var viewModel: KeyboardViewModel

    lateinit var keyKeyboard: String;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


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
        }


    }

}