package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.matheasyapp.databinding.ActivityCostMaterialBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.keyboard.BottomsheftKeyboardCostMaterial
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CostMaterialActivity : AppCompatActivity(), BottomsheftKeyboardCostMaterial.CallBackFunction {

    private lateinit var binding: ActivityCostMaterialBinding

    private var stateButton: Boolean = true

    lateinit var df: DecimalFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCostMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stateButton = true

        df = DecimalFormat("#,###")

        binding.layoutResult.visibility = View.GONE

        setEnableButtonResult(0)
        setEnableButtonCancle(0)


        binding.tvDistance.setOnClickListener {
            showKeyBoard("value_distance")
        }

        binding.tvValueMaterial.setOnClickListener {
            showKeyBoard("value_material")
        }

        binding.tvValueGas.setOnClickListener {
            showKeyBoard("value_gas")
        }

        onClick()

    }


    private fun showKeyBoard(key: String) {
        val keyboard = BottomsheftKeyboardCostMaterial.newInstance(key)
        keyboard.show(supportFragmentManager, keyboard.tag)

    }

    fun checkEnableButtonResult() {
        if ((binding.tvValueMaterial.text.toString().length > 0 && binding.tvValueGas.text.toString().length > 0
                    && binding.tvDistance.text.toString().length > 0)
        ) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvValueMaterial.text.toString().length > 0 || binding.tvValueGas.text.toString().length > 0
                    || binding.tvDistance.text.toString().length > 0)
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

    private fun setEnableInput(boolean: Boolean) {
        binding.tvDistance.isEnabled = boolean
        binding.tvValueMaterial.isEnabled = boolean
        binding.tvValueGas.isEnabled = boolean

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

    private fun calculatorMaterial() {

        if (binding.tvValueMaterial.text.length > 0 && binding.tvDistance.text.length > 0 &&
            binding.tvValueGas.text.length > 0
        ) {

            val distanceClear = clearDotsAndCommas(binding.tvDistance.text.toString())
            val materialClear = clearDotsAndCommas(binding.tvValueMaterial.text.toString())
            val gasClear = clearDotsAndCommas(binding.tvValueGas.text.toString())

            Log.d("543523542", distanceClear.toString())
            Log.d("543523542", materialClear.toString())
//            val valueMaterial: Double = (distanceClear.toDouble() / materialClear.toDouble()) * 100
            val valueMaterial: Double = (distanceClear.toDouble() / materialClear.toDouble())
            Log.d("543523542", valueMaterial.toString())

//


            var valueCost = valueMaterial * gasClear.toDouble()

            Log.d("543523542", valueCost.toString())

            if (!(valueCost.toString().contains("E"))) {
                var valueCostClear = clearTrailingZerosAndDot(valueCost.toString())
                binding.tvResultLastMoney.setText(
                    df.format(valueCostClear.toBigDecimal()).toString()
                )
            } else {
                binding.tvResultLastMoney.setText(df.format(valueCost.toBigDecimal()).toString())
            }

            if (valueMaterial.toString().length >= 7) {
                val clearDOtEnd = clearTrailingZerosAndDot(valueMaterial.toString())
                val clear = clearDotsAndCommas(clearDOtEnd.toString())
                binding.tvResultPerson.setText(df.format(clear.toBigDecimal()).toString())
            } else if (valueMaterial.toString().contains("E")) {
                binding.tvResultPerson.setText(df.format(valueMaterial.toBigDecimal()).toString())
            } else {
                binding.tvResultPerson.setText(valueMaterial.toString())
            }
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

            calculatorMaterial()
            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()
                binding.layoutResult.visibility = View.VISIBLE
            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {

                // clear all
                binding.tvDistance.setText("")
                binding.tvValueMaterial.setText("")
                binding.tvValueGas.setText("")

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

    override fun onClickNumber(mode: String, value: String) {
        when (mode) {
            "value_distance" -> {
                val currentText = binding.tvDistance.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvDistance.setText(format.toString())
                    checkEnableButtonResult()
                }
            }

            "value_material" -> {
                val currentText = binding.tvValueMaterial.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvValueMaterial.setText(format.toString())
                    checkEnableButtonResult()
                }
            }

            "value_gas" -> {
                val currentText = binding.tvValueGas.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvValueGas.setText(format.toString())
                    checkEnableButtonResult()
                }
            }


        }
    }

    override fun onClearItem(mode: String) {
        when (mode) {
            "value_distance" -> {
                val currentText = binding.tvDistance.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvDistance.setText(currentText.substring(0, currentText.length - 1))
                    checkEnableButtonResult()
                }
            }

            "value_material" -> {
                val currentText = binding.tvValueMaterial.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvValueMaterial.setText(
                        currentText.substring(
                            0,
                            currentText.length - 1
                        )
                    )
                    checkEnableButtonResult()
                }
            }

            "value_gas" -> {
                val currentText = binding.tvValueGas.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvValueGas.setText(currentText.substring(0, currentText.length - 1))
                    checkEnableButtonResult()
                }
            }


        }
    }

    override fun onClearAll(mode: String) {
        when (mode) {
            "value_distance" -> {
                binding.tvDistance.setText("")
            }

            "value_material" -> {
                binding.tvValueMaterial.setText("")
            }

            "value_gas" -> {
                binding.tvValueGas.setText("")
            }
        }
    }


    fun clearDotsAndCommas(input: String): String {
        return input.replace(".", "").replace(",", "").toString()
    }

    fun isNumberLengthValid(text: String): Boolean {
        if (text.isNotEmpty()) {
            val text = text.toString()
            val digitsOnly = text.filter { it.isDigit() }
            return digitsOnly.length <= 15
        }
        return true
    }


}