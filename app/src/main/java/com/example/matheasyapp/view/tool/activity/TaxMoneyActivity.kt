package com.example.matheasyapp.view.tool.activity

import android.R.attr.data
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.matheasyapp.databinding.ActivityTaxMoneyBinding
import com.example.matheasyapp.view.tool.keyboard.KeyboardTaxMoney
import java.text.DecimalFormat


class TaxMoneyActivity : AppCompatActivity(), KeyboardTaxMoney.CallBackFunction {

    private lateinit var binding: ActivityTaxMoneyBinding

    private var stateButton: Boolean = true
    lateinit var df: DecimalFormat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityTaxMoneyBinding.inflate(layoutInflater)

        stateButton = true

        df = DecimalFormat("#,###")

        setContentView(binding.root)

        setEnableButtonResult(0)
        setEnableButtonCancle(0)
        binding.layoutResult.visibility = View.GONE

        onClick()

        binding.tvValueTax.setOnClickListener {
            showKeyBoard("value_tax")
        }
        binding.tvValueCost.setOnClickListener {
            showKeyBoard("value_cost")

        }


    }

    private fun onClick() {

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnEditData.setOnClickListener {
            setEnableInput(true)
            // update data
            stateButton = true
            checkStateButton()
            binding.layoutResult.visibility = View.GONE
        }

        binding.btnResult.setOnClickListener {
            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()
                caculatorTax()
                binding.layoutResult.visibility = View.VISIBLE


            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {

                // clear all
                binding.tvValueTax.setText("")
                binding.tvValueCost.setText("")
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

    private fun caculatorTax() {

        var valueCost = clearDotsAndCommas(binding.tvValueCost.text.toString()).toDouble()
        var valueTax = binding.tvValueTax.text.toString().toDouble()

        var resultTax = (valueCost * (valueTax / 100))
        var totalPrice = (valueCost + resultTax)


        val tax = clearTrailingZerosAndDot(resultTax.toString())
        val total = clearTrailingZerosAndDot(totalPrice.toString())

        val taxFormat = clearTrailingZerosAndDot(tax)
        val totalFormat = clearTrailingZerosAndDot(total)

        binding.tvResultTax.setText(df.format(taxFormat.toBigDecimal()).toString())
        binding.tvResultPrice.setText(df.format(totalFormat.toBigDecimal()).toString())



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


    fun isNumberLengthValid(text: String): Boolean {
        if (text.isNotEmpty()) {
            val text = text.toString()
            val digitsOnly = text.filter { it.isDigit() }
            return digitsOnly.length <= 15
        }
        return true
    }


    fun clearDotsAndCommas(input: String): String {
        if (input.isNotEmpty()) {
            return input.replace(".", "").replace(",", "").toString()
        } else
            return ""
    }


    fun checkEnableButtonResult() {
        if ((binding.tvValueTax.text.toString().length > 0 && binding.tvValueCost.text.toString().length > 0)
        ) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvValueTax.text.toString().length > 0 || binding.tvValueCost.text.toString().length > 0)
        ) {
            setEnableButtonCancle(2)

        } else {
            setEnableButtonCancle(0)
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


    private fun showKeyBoard(key: String) {

        val keyBoard = KeyboardTaxMoney.newInstance(key)

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    private fun setEnableInput(boolean: Boolean) {
        binding.tvValueTax.isEnabled = boolean
        binding.tvValueCost.isEnabled = boolean
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

    override fun onClickNumber(mode: String, value: String) {
        when (mode) {

            "value_tax" -> {

                if (binding.tvValueTax.text.isNotEmpty()) {
                    maxValueTax()
                }

                val currentText = binding.tvValueTax.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                binding.tvValueTax.setText(concatenatedText)
                checkEnableButtonResult()

                maxValueTax()

            }

            "value_cost" -> {
                val currentText = binding.tvValueCost.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
                    binding.tvValueCost.setText(
                        df.format(concatenatedText.toBigDecimal()).toString()
                    )
                    checkEnableButtonResult()
                }
            }

        }
    }

    private fun maxValueTax() {
        if (binding.tvValueTax.text.toString().toDouble() >= 100) {
            Toast.makeText(this, "Max value is 100", Toast.LENGTH_SHORT).show()
            binding.tvValueTax.setText("100")
        }
    }

    override fun onClearItem(mode: String) {

        when (mode) {
            "value_tax" -> {
                val currentText = binding.tvValueTax.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvValueTax.setText(currentText.substring(0, currentText.length - 1))
                    checkEnableButtonResult()
                }
            }

            "value_cost" -> {
                val currentText = binding.tvValueCost.text ?: ""
                if (currentText.isNotEmpty()) {
                    var valueClear = clearDotsAndCommas(
                        (currentText.substring(0, currentText.length - 1)).toString()
                    )
                    if (valueClear.isNotEmpty()) binding.tvValueCost.setText(
                        df.format(valueClear.toBigDecimal()).toString()
                    )
                    else binding.tvValueCost.setText("")
                    checkEnableButtonResult()
                }
            }
        }

    }

    override fun onClearAll(mode: String) {
        when (mode) {
            "value_tax" -> {
                binding.tvValueTax.setText("")
            }

            "value_cos" -> {
                binding.tvValueCost.setText("")
            }
        }
    }

}