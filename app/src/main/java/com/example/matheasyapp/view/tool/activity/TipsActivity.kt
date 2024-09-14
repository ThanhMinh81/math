package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.databinding.ActivityTipsBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.keyboard.KeyboardTaxMoney
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


class TipsActivity : AppCompatActivity(), KeyboardTaxMoney.CallBackFunction {

    private lateinit var binding: ActivityTipsBinding

//    private lateinit var keyBoardViewModel: KeyboardViewModel

    private var stateButton: Boolean = true
    lateinit var df: DecimalFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        df = DecimalFormat("#,###.##")
          df = DecimalFormat("#,##0.########")

        binding = ActivityTipsBinding.inflate(layoutInflater)

        stateButton = true

        binding.layoutResult.visibility = View.GONE

        binding.layoutNonTipsVat.visibility = View.GONE
        binding.layoutContainerAddTips.visibility = View.GONE
        binding.layoutContainerTaxTips.visibility = View.GONE

        iniView()
        onClickKeyBoard()
        handleClick()

        setContentView(binding.root)

    }

    private fun onClickKeyBoard() {

        binding.tvMoneyBill.setOnClickListener {
            showKeyBoard("money_bill")
        }

        binding.layoutPersonAmount.setOnClickListener {
            showKeyBoard("person_amount")
        }

        binding.tvMoneyGive.setOnClickListener {
            showKeyBoard("money_give")
        }

        binding.tvPercentMoney.setOnClickListener {
            showKeyBoard("percent_give")
        }

        binding.tvTaxAmount.setOnClickListener {
            showKeyBoard("tax_amount")
        }

        binding.tvVatRate.setOnClickListener {
            showKeyBoard("vat_rate")

        }

    }


    private fun iniView() {

        checkStateButton()
        checkEnableButtonResult()
        setEnableInput(true)



        binding.btnResult.setOnClickListener {

            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()

                binding.layoutResult.visibility = View.VISIBLE

                if (!(binding.swAddTips.isChecked)) {

                    val moneyBill = binding.tvMoneyBill.text.toString().replace(",", "").replace(".", "")
                    binding.tvResultLastMoney.setText(df.format(moneyBill.toBigDecimal()).toString())
                    val personMoney = moneyBill.toDouble() / binding.layoutPersonAmount.text.toString().toDouble()

//                    val clearPerson = clearDotsAndCommas(personMoney.toString())
                    val clearPerson = removeTrailingZeroes(personMoney.toString())
                    val clearDot = clearDotsAndCommas(clearPerson)

//                    Log.d("truong hop 1", df.format(clearDot.toBigDecimal()).toString())

                    binding.tvResultPerson.setText(df.format(clearDot.toBigDecimal()).toString())


                } else if (binding.swAddTips.isChecked && !binding.scAddTipsVAT.isChecked) {

                    val moneyBill =
                        binding.tvMoneyBill.text.toString().replace(",", "").replace(".", "")
                    val moneyTip =
                        binding.tvMoneyGive.text.toString().replace(",", "").replace(".", "")
                    val total = moneyBill.toBigDecimal() + moneyTip.toBigDecimal()
                    val personMoney =
                        moneyBill.toBigDecimal() / binding.layoutPersonAmount.text.toString()
                            .toBigDecimal()

                    binding.tvResultLastMoney.setText(df.format(total).toString())
                    binding.tvResultPerson.setText(df.format(personMoney).toString())
                    Log.d("truong hop 2", "5432523")

                } else if (binding.swAddTips.isChecked && binding.scAddTipsVAT.isChecked) {
                    Log.d("truong hop 3", "5432523")

                    val moneyBill =
                        binding.tvMoneyBill.text.toString().replace(",", "").replace(".", "")
                    val moneyTip =
                        binding.tvMoneyGive.text.toString().replace(",", "").replace(".", "")

//                    Tính tiền tip (nếu có Tip Percent):
                    val tipAmount = moneyBill.toDouble() * (moneyTip.toDouble() / 100)

//                    Tính tổng tiền trước thuế:
                    val totalBeforeTax = moneyBill.toDouble() + tipAmount.toDouble()
//                    Tính tiền thuế VAT:
                    val vat = totalBeforeTax * (binding.tvVatRate.text.toString().toDouble() / 100)
//                    Tính tổng số tiền cuối cùng (Final Amount):
                    val taxAmount = clearDotsAndCommas(binding.tvTaxAmount.text.toString())
                    val final = totalBeforeTax + vat + taxAmount.toDouble()
                    val person = final / binding.layoutPersonAmount.text.toString().toDouble()

                    val valClearFinal = clearDotsAndCommas(final.toString())
                    val valPerson = clearDotsAndCommas(person.toString())

                    binding.tvResultLastMoney.setText(
                        df.format(valClearFinal.toBigDecimal()).toString()
                    )
                    binding.tvResultPerson.setText(df.format(valPerson.toBigDecimal()).toString())
                }

            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {
                // clear all
                binding.tvMoneyBill.setText("")
                binding.tvMoneyGive.setText("")
                binding.tvPercentMoney.setText("")

                checkEnableButtonResult()
                setEnableInput(true)

            } else {
                setEnableInput(true)
                // update data
                stateButton = true
                checkStateButton()
                binding.layoutResult.visibility = View.GONE
            }
        }

    }
    private fun checkStateButton() {
        if (stateButton) {

            binding.btnResult.setText("See Result")
            binding.btnCancle.setText("Delete All")

            binding.btnCancle.visibility = View.VISIBLE
            binding.btnEditData.visibility = View.GONE
        } else {
            binding.btnCancle.visibility = View.GONE
            binding.btnEditData.visibility = View.VISIBLE
            binding.btnResult.setText("Home")
            binding.btnCancle.setText("Edit data")

        }
    }

    fun checkEnableButtonResult() {

        if (binding.scAddTipsVAT.isChecked) {

            if (binding.tvMoneyBill.text.isNotEmpty() && binding.layoutPersonAmount.text.isNotEmpty()
                && binding.tvMoneyGive.text.isNotEmpty() && binding.tvPercentMoney.text.isNotEmpty()
                && binding.tvTaxAmount.text.isNotEmpty() && binding.tvVatRate.text.isNotEmpty()
            ) {
                setEnableButtonResult(2)
            } else {
                setEnableButtonResult(0)
            }

            if (binding.tvMoneyBill.text.isNotEmpty() || binding.layoutPersonAmount.text.isNotEmpty()
                || binding.tvMoneyGive.text.isNotEmpty() || binding.tvPercentMoney.text.isNotEmpty()
            ) {
                setEnableButtonCancle(2)
            } else {
                setEnableButtonCancle(0)
            }

        } else if (binding.swAddTips.isChecked) {
            if (binding.tvMoneyBill.text.isNotEmpty() && binding.layoutPersonAmount.text.isNotEmpty()
                && binding.tvMoneyGive.text.isNotEmpty() && binding.tvPercentMoney.text.isNotEmpty()
            ) {
                setEnableButtonResult(2)
            } else {
                setEnableButtonResult(0)
            }

            if (binding.tvMoneyBill.text.isNotEmpty() || binding.layoutPersonAmount.text.isNotEmpty()
                || binding.tvMoneyGive.text.isNotEmpty() || binding.tvPercentMoney.text.isNotEmpty()
            ) {
                setEnableButtonCancle(2)
            } else {
                setEnableButtonCancle(0)
            }

        } else if (!(binding.swAddTips.isChecked)) {
            if (binding.tvMoneyBill.text.isNotEmpty() && binding.layoutPersonAmount.text.isNotEmpty()) {
                setEnableButtonResult(2)
            } else {
                setEnableButtonResult(0)

            }
            if (binding.tvMoneyBill.text.isNotEmpty() || binding.layoutPersonAmount.text.isNotEmpty()) {
                setEnableButtonCancle(2)
            } else {
                setEnableButtonCancle(0)
            }
        }

    }


    fun setEnableButtonResult(size: Int) {
        if (size > 0) {
            binding.btnResult.alpha = 1f
            binding.btnResult.isEnabled = true
        } else {
            binding.btnResult.alpha = 0.5f
            binding.btnResult.isEnabled = false
        }

    }

    fun setEnableButtonCancle(size: Int) {
        if (size > 0) {
            binding.btnCancle.alpha = 1f
            binding.btnCancle.isEnabled = true
        } else {
            binding.btnCancle.alpha = 0.5f
            binding.btnCancle.isEnabled = false
        }
    }

    private fun handleClick() {

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.swAddTips.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.layoutContainerAddTips.visibility = View.VISIBLE
                binding.layoutNonTipsVat.visibility = View.VISIBLE
                checkEnableButtonResult()
            } else {
                binding.layoutContainerAddTips.visibility = View.GONE
                binding.layoutNonTipsVat.visibility = View.GONE
                checkEnableButtonResult()
            }
        })
        binding.scAddTipsVAT.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.layoutContainerTaxTips.visibility = View.VISIBLE
                checkEnableButtonResult()

            } else {
                binding.layoutContainerTaxTips.visibility = View.GONE
                checkEnableButtonResult()
            }
        })

        binding.btnEditData.setOnClickListener {
            setEnableInput(true)
            // update data
            stateButton = true
            checkStateButton()
            binding.layoutResult.visibility = View.GONE
        }


    }

    private fun showKeyBoard(key: String) {

        val keyBoard = KeyboardTaxMoney.newInstance(key)

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }


    private fun setEnableInput(boolean: Boolean) {

        binding.tvMoneyBill.isEnabled = boolean
        binding.spMoneyBill.isEnabled = boolean
        binding.layoutPersonAmount.isEnabled = boolean

        binding.tvMoneyGive.isEnabled = boolean
        binding.spMoneyGive.isEnabled = boolean
        binding.tvPercentMoney.isEnabled = boolean
    }

    override fun onClickNumber(mode: String, value: String) {

        when (mode) {
            "money_bill" -> {

                val currentText = binding.tvMoneyBill.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""

                val concatenatedText = clearText + value

                val valueFormat = clearDotsAndCommas(concatenatedText)
                Log.d("452452452",valueFormat.toString())


                if (isNumberLengthValid(concatenatedText)) {

                    val format: String = try {
                        df.format(valueFormat.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }

                    binding.tvMoneyBill.setText(format.toString())

                    checkEnableButtonResult()

                    calculatorMoneyBill(binding.tvMoneyBill.text.toString())

                }
            }

            "person_amount" -> {
                val currentText = binding.layoutPersonAmount.text.toString() ?: ""
                val concatenatedText = currentText + value

                binding.layoutPersonAmount.setText(concatenatedText)
                checkEnableButtonResult()

            }

            "money_give" -> {

                val currentText = binding.tvMoneyGive.text.toString() ?: ""
                val concatenatedText = currentText + value

                val valueFormat = clearDotsAndCommas(concatenatedText)

                val format: String = try {
                    df.format(valueFormat.toBigDecimal())
                } catch (e: NumberFormatException) {
                    concatenatedText
                }

                binding.tvMoneyGive.setText(format)
                checkEnableButtonResult()

                calculatorMoneyGive()

            }

            "percent_give" -> {
                checkEnableButtonResult()

                val currentText = binding.tvPercentMoney.text.toString() ?: ""
                val concatenatedText = currentText + value

                if (hasZeroOrOneDot(concatenatedText) == 1) {
                    binding.tvPercentMoney.setText(concatenatedText)

                    if (!(binding.tvPercentMoney.text.toString().last().equals("."))) {
                        calculatorPercentGive()
                        checkEnableButtonResult()

                    }

                } else if (hasZeroOrOneDot(concatenatedText) == 0) {
                    if (checkInput(concatenatedText)) {
                        binding.tvPercentMoney.setText(concatenatedText.toString())
                        calculatorPercentGive()
                        checkEnableButtonResult()

                    } else {
                        binding.tvPercentMoney.setText("100")
                        calculatorPercentGive()
                        checkEnableButtonResult()

                    }
                }
            }

            "tax_amount" -> {
                val currentText = binding.tvTaxAmount.text.toString() ?: ""
                val concatenatedText = currentText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val valueClear = clearDotsAndCommas(concatenatedText)
                    binding.tvTaxAmount.setText(df.format(valueClear.toBigDecimal()).toString())
                    calculatorTaxAmount()
                    checkEnableButtonResult()
                }
            }

            "vat_rate" -> {
                val currentText = binding.tvVatRate.text.toString() ?: ""
                val concatenatedText = currentText + value

                if (!(concatenatedText.startsWith("0")) && !(concatenatedText.startsWith("."))) {
                    if (checkInput(concatenatedText)) {

                        binding.tvVatRate.setText(concatenatedText)
                        calculatorVAT()
                        checkEnableButtonResult()


                    } else {
                        binding.tvVatRate.setText("100")
                    }
                }

            }

        }

    }

    fun hasZeroOrOneDot(input: String): Int {
        // Đếm số lượng dấu chấm trong chuỗi
        val dotCount = input.count { it == '.' }

        // Trả về true nếu có 0 hoặc 1 dấu chấm, ngược lại trả về false
        return dotCount
    }

    private fun calculatorVAT() {
        if (binding.tvMoneyBill.text.isNotEmpty() && binding.tvVatRate.text.isNotEmpty()) {
            val valueMoney = clearDotsAndCommas(binding.tvMoneyBill.text.toString())
            val valueTaxAmount =
                (binding.tvVatRate.text.toString().toDouble() * valueMoney.toDouble()) / 100
            val valueTaxAmountClear = clearTrailingZerosAndDot(valueTaxAmount.toString())
            if (valueTaxAmountClear.contains("E")) {
                binding.tvTaxAmount.setText(
                    df.format(valueTaxAmountClear.toBigDecimal()).toString()
                )
            } else {
                binding.tvTaxAmount.setText(df.format(valueTaxAmount.toBigDecimal()).toString())
            }
        }
    }


    private fun calculatorTaxAmount() {

        if (binding.tvTaxAmount.text.isNotEmpty() && binding.tvMoneyBill.text.isNotEmpty()) {

            val moneyBillClear = clearDotsAndCommas(binding.tvMoneyBill.text.toString())
            val taxAmount = clearDotsAndCommas(binding.tvTaxAmount.text.toString())

            val vatRate = (taxAmount.toDouble() / moneyBillClear.toDouble()) * 100

            Log.d("535432523523", vatRate.toString())

            val clearDot = clearDotsAndCommas(vatRate.toString())

            if (!clearDot.contains("E")) {
                if (vatRate.toString().startsWith("0")) {
                    binding.tvVatRate.setText(vatRate.toString())
                } else {
                    binding.tvVatRate.setText(
                        df.format(clearDot.toString().toBigDecimal()).toString()
                    )
                }
            }
        }
    }


    private fun checkInput(concatenatedText: String): Boolean {
        if (concatenatedText.toDouble() <= 100) {
            return true
        }
        return false
    }


    fun clearTrailingZerosAndDot(input: String): String {
        // Kiểm tra nếu chuỗi là số nguyên (không có phần thập phân)
        if (!input.contains('.')) {
            return input
        }

        // Xóa các số 0 ở cuối chuỗi
        var result = input.trimEnd('0')

        // Xóa dấu chấm nếu nó là ký tự cuối cùng
        if (result.endsWith('.')) {
            result = result.dropLast(1)
        }

        return result
    }

    private fun calculatorPercentGive() {
        if (binding.tvPercentMoney.text.toString()
                .isNotEmpty() && binding.tvMoneyBill.text.toString().isNotEmpty()
        ) {

            if (binding.tvPercentMoney.text.toString().startsWith("0.")) {
                val moneyBillClear = clearDotsAndCommas(binding.tvMoneyBill.text.toString())
                val valuePercent = binding.tvPercentMoney.text.toString().toDouble() / 100.0
                val valueTipAmount = valuePercent * moneyBillClear.toDouble()
                if (valuePercent.toString().startsWith("0.")) {
                    binding.tvMoneyGive.setText(valueTipAmount.toString())
                } else {
                    // xóa đi số .0
                    val valueClearDot = clearTrailingZerosAndDot(valueTipAmount.toString())
                    // format lại từ đầu
                    val clear = clearDotsAndCommas(valueClearDot.toString())
                    binding.tvMoneyGive.setText(df.format(clear.toBigDecimal()).toString())
                }

            } else {
                val percentMoneyClear = clearDotsAndCommas(binding.tvPercentMoney.text.toString())
                val moneyBillClear = clearDotsAndCommas(binding.tvMoneyBill.text.toString())

                val valuePercent = percentMoneyClear.toDouble() / 100.0
                val valueTipAmount = valuePercent * moneyBillClear.toDouble()
                binding.tvMoneyGive.setText(df.format(valueTipAmount.toBigDecimal()).toString())
            }


        } else {
            binding.tvMoneyGive.setText("")
        }
    }


    private fun calculatorMoneyGive() {
        if (binding.tvMoneyBill.text.length > 0 && binding.tvMoneyGive.text.isNotEmpty()) {

            val moneyBillClear = clearDotsAndCommas(binding.tvMoneyBill.text.toString())
            val valueMoneyTipClear = clearDotsAndCommas(binding.tvMoneyGive.text.toString())

            if (valueMoneyTipClear.toDouble() > 0 && moneyBillClear.toDouble() > 0) {

                val percentGive = (valueMoneyTipClear.toDouble() / moneyBillClear.toDouble()) * 100

                if (!percentGive.toString().contains("E")) {
                    binding.tvPercentMoney.setText(percentGive.toString())
                }
            }
        } else if (binding.tvMoneyBill.text.toString()
                .isEmpty() && binding.tvMoneyGive.text.isNotEmpty()
        ) {
            binding.tvPercentMoney.text = binding.tvMoneyGive.text
        }
    }

    private fun calculatorMoneyBill(value: String) {
        if (binding.tvMoneyGive.text.toString()
                .isNotEmpty() && binding.tvPercentMoney.text.toString().isNotEmpty()
        ) {

            if (value.isNotEmpty()) {

                if (binding.tvMoneyGive.text.toString().isNotEmpty()) {

                    val tipPercent = binding.tvMoneyGive.text.toString()

                    binding.tvPercentMoney.setText(tipPercent.toString())

                } else {
                    binding.tvPercentMoney.setText("")
                }
            }

        } else if (binding.tvMoneyGive.text.isNotEmpty() && binding.tvPercentMoney.text.toString()
                .isEmpty()
        ) {

            if (value.isNotEmpty()) {

                val moneyBillClear = clearDotsAndCommas(value).toDouble()

                if (moneyBillClear > 0 && binding.tvMoneyGive.text.toString().isNotEmpty()) {

                    val moneyGive = binding.tvMoneyGive.text.toString().toDouble()

                    val percentGive = (moneyGive.toDouble() / moneyBillClear) * 100
                    binding.tvPercentMoney.setText(percentGive.toString())

                } else {
                    binding.tvPercentMoney.setText("")
                }
            }

        } else if (!(binding.tvMoneyGive.text.isNotEmpty()) && binding.tvPercentMoney.text.toString()
                .isNotEmpty()
        ) {
            val moneyBill = clearDotsAndCommas(value)

            if (moneyBill.toDouble() > 0 && binding.tvPercentMoney.text.toString().isNotEmpty()) {

                val tipClear = clearDotsAndCommas(binding.tvPercentMoney.text.toString())

                val percentTip = tipClear.toDouble() * moneyBill.toDouble()
                val clearPercentTip = clearDotsAndCommas(percentTip.toString())

                binding.tvMoneyGive.setText(df.format(clearPercentTip.toBigDecimal()).toString())


            } else {
                binding.tvMoneyGive.setText("")
            }

        }
    }

    override fun onClearItem(mode: String) {
        when (mode) {
            "money_bill" -> {
                val currentText = binding.tvMoneyBill.text ?: ""
                if (currentText.isNotEmpty()) {
                    val valueClear = clearDotsAndCommas(currentText.toString())
                    val newText = valueClear.substring(0, valueClear.length - 1)

                    if (newText.isNotEmpty()) {
                        binding.tvMoneyBill.setText(df.format(newText.toBigDecimal()).toString())
                        calculatorMoneyBill(binding.tvMoneyBill.text.toString())
                        checkEnableButtonResult()
                    } else {
                        binding.tvMoneyBill.setText("")
                    }

                }
            }

            "person_amount" -> {
                val currentText = binding.layoutPersonAmount.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.layoutPersonAmount.setText(
                        currentText.substring(
                            0,
                            currentText.length - 1
                        )
                    )
                    checkEnableButtonResult()
                }
            }

            "money_give" -> {
                val currentText = binding.tvMoneyGive.text ?: ""
                if (currentText.isNotEmpty()) {
                    val valueClearDot = clearDotsAndCommas(binding.tvMoneyGive.text.toString())
                    val newValue = valueClearDot.substring(0, valueClearDot.length - 1)
                    if (newValue.isNotEmpty()) {
                        binding.tvMoneyGive.setText(df.format(newValue.toBigDecimal()).toString())
                        calculatorMoneyGive()
                        checkEnableButtonResult()
                    } else {
                        binding.tvMoneyGive.setText("")
                        binding.tvPercentMoney.setText("")
                        checkEnableButtonResult()
                    }
                }
            }

            "percent_give" -> {
                val currentText = binding.tvPercentMoney.text ?: ""
                if (currentText.startsWith("0.")) {
                    val current = currentText.substring(0, currentText.length - 1)
                    if (current.isNotEmpty()) {
                        binding.tvPercentMoney.setText(current)
                        calculatorPercentGive()
                        checkEnableButtonResult()
                    } else {
                        binding.tvPercentMoney.setText("")
                        binding.tvMoneyGive.setText("")
                        checkEnableButtonResult()

                    }
                } else {
                    val currentClear = clearDotsAndCommas(currentText.toString())
                    if (currentClear.isNotEmpty()) {
                        val current = currentClear.substring(0, currentClear.length - 1)
                        if (current.isNotEmpty()) {
                            binding.tvPercentMoney.setText(
                                df.format(current.toBigDecimal()).toString()
                            )
                            calculatorPercentGive()
                            checkEnableButtonResult()
                        } else {
                            binding.tvPercentMoney.setText("")
                            binding.tvMoneyGive.setText("")
                            checkEnableButtonResult()

                        }
                    }
                }
            }

            "vat_rate" -> {
                val currentText = binding.tvVatRate.text ?: ""
                if (currentText.isNotEmpty()) {
                    val newValue = currentText.substring(0, currentText.length - 1)
                    if (newValue.isNotEmpty()) {
                        binding.tvVatRate.setText(newValue)
                        calculatorVAT()
                        checkEnableButtonResult()
                    } else {
                        binding.tvVatRate.setText("")
                        binding.tvTaxAmount.setText("")
                        checkEnableButtonResult()

                    }
                }
            }

            "tax_amount" -> {
                val currentText = binding.tvTaxAmount.text ?: ""
                if (currentText.isNotEmpty()) {
                    val valueClearDot = clearDotsAndCommas(binding.tvTaxAmount.text.toString())
                    val newValue = valueClearDot.substring(0, valueClearDot.length - 1)
                    if (newValue.isNotEmpty()) {
                        binding.tvTaxAmount.setText(df.format(newValue.toBigDecimal()).toString())
                        calculatorTaxAmount()
                        checkEnableButtonResult()
                    } else {
                        binding.tvTaxAmount.setText("")
                        binding.tvVatRate.setText("")
                        checkEnableButtonResult()

                    }
                }
            }

        }
    }

    override fun onClearAll(mode: String) {
        when (mode) {
            "money_bill" -> {
                binding.tvMoneyBill.setText("")
                checkEnableButtonResult()

            }

            "person_amount" -> {
                binding.layoutPersonAmount.setText("")
                checkEnableButtonResult()

            }

            "money_give" -> {
                binding.tvMoneyGive.setText("")
                binding.tvPercentMoney.setText("")
                checkEnableButtonResult()

            }

            "percent_give" -> {
                binding.tvMoneyGive.setText("")
                binding.tvPercentMoney.setText("")
                checkEnableButtonResult()
            }
        }
    }

    fun isNumberLengthValid(text: String): Boolean {
        if (text.isNotEmpty()) {
            val text = text.toString()
            val digitsOnly = text.filter { it.isDigit() }
            return digitsOnly.length <= 15
        }
        return true
    }

    fun clearDotsAndCommas(input: String): String {
        return input.replace(".", "").replace(",", "").toString()
    }
    fun removeTrailingZeroes(input: String): String {
        // Check if the string contains a decimal point and ends with '.0'
        return if (input.contains(".0")) {
            input.substringBefore(".0")
        } else {
            input
        }
    }

}
