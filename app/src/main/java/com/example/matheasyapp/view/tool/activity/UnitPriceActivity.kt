package com.example.matheasyapp.view.tool.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityUnitPriceBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.keyboard.KeyboardUnitPrice
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class UnitPriceActivity : AppCompatActivity(), KeyboardUnitPrice.CallBackFunction {

    private lateinit var binding: ActivityUnitPriceBinding

    private var stateButton: Boolean = true
    private lateinit var viewModelKeyBoard: KeyboardViewModel

    lateinit var df: DecimalFormat

    val layoutIds = listOf(
        R.id.layoutA,
        R.id.layoutB,
        R.id.layoutC,
        R.id.layoutD
    )

    var listLayout: ArrayList<LinearLayout> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUnitPriceBinding.inflate(layoutInflater)
        listLayout.add(binding.layoutA)
        listLayout.add(binding.layoutB)
        listLayout.add(binding.layoutC)
        listLayout.add(binding.layoutD)
        setContentView(binding.root)

        stateButton = true
        binding.layoutResult.visibility = View.GONE
//        df = DecimalFormat("#,###,###", DecimalFormatSymbols(Locale.US))
        df = DecimalFormat("#,###")



        setEnableButtonResult(0)
        setEnableButtonCancle(0)

        initWidget()
        onClick()

    }

    private fun onClick() {

        binding.tvPriceA.setOnClickListener { handleClick("price_a") }
        binding.tvPriceB.setOnClickListener { handleClick("price_b") }
        binding.tvPriceC.setOnClickListener { handleClick("price_c") }
        binding.tvPriceD.setOnClickListener { handleClick("price_d") }

        binding.tvQuantityA.setOnClickListener { handleClick("quantity_a") }
        binding.tvQuantityB.setOnClickListener { handleClick("quantity_b") }
        binding.tvQuantityC.setOnClickListener { handleClick("quantity_c") }
        binding.tvQuantityD.setOnClickListener { handleClick("quantity_d") }

        binding.tvUnitPriceA.setOnClickListener { handleClick("unit_a") }
        binding.tvUnitPriceB.setOnClickListener { handleClick("unit_b") }
        binding.tvUnitPriceC.setOnClickListener { handleClick("unit_c") }
        binding.tvUnitPriceD.setOnClickListener { handleClick("unit_d") }


        binding.icBack.setOnClickListener {
            onBackPressed()
        }


        binding.btnResult.setOnClickListener {
            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()
                binding.layoutResult.visibility = View.VISIBLE

                var listLayout: ArrayList<LinearLayout> = ArrayList()
                listLayout.add(binding.layoutParentA)
                listLayout.add(binding.layoutParentB)
                listLayout.add(binding.layoutParentC)
                listLayout.add(binding.layoutParentD)

                var listUnit = processAllEditTexts(listLayout)

                val result = formatUnitPrices(listUnit)
                binding.tvResultUnitPrice.setText(result)


            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {
            showInputDialog()

        }

        binding.btnEditData.setOnClickListener {
            setEnableInput(true)

            stateButton = true
            checkStateButton()
            binding.layoutResult.visibility = View.GONE
        }


    }


    private fun handleClick(s: String) {
        showKeyBoard(s)

    }

    private fun initWidget() {
        viewModelKeyBoard = ViewModelProvider(this).get(KeyboardViewModel::class.java)
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

    fun checkEnableButtonResult() {

        var check = checkEditextFill(listLayout)

        Log.d("045674563463", check.size.toString() + "   ")

        if (check.size >= 2) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if (binding.tvPriceA.text.toString().length > 0
            || binding.tvQuantityA.text.toString().length > 0
            || binding.tvUnitPriceA.text.toString().length > 0
            || binding.tvPriceB.text.toString().length > 0
            || binding.tvQuantityB.text.toString().length > 0
            || binding.tvUnitPriceB.text.toString().length > 0
            || binding.tvPriceC.text.toString().length > 0
            || binding.tvQuantityC.text.toString().length > 0
            || binding.tvUnitPriceC.text.toString().length > 0
            || binding.tvPriceD.text.toString().length > 0
            || binding.tvQuantityD.text.toString().length > 0
            || binding.tvUnitPriceD.text.toString().length > 0
        ) {
            setEnableButtonCancle(2)

        } else {
            setEnableButtonCancle(0)
        }
    }


    private fun showKeyBoard(key: String) {
        val bottomSheet = KeyboardUnitPrice.newInstance(key)
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)

    }


    fun checkEditextFill(list: ArrayList<LinearLayout>): ArrayList<String> {
        var listCount: ArrayList<String> = ArrayList()
        for (item in list) {
            var count = 0
            for (i in 0 until item.childCount) {
                val child = item.getChildAt(i)
                if (i != 0) {
                    child as EditText
                    if (child.text.toString().length > 0) {
                        count++
                    }
                }

            }
            if (count == 2) {

                listCount.add("1")
            }
        }
        return listCount
    }


    private fun clearAllEditext(text: String) {

        for (layoutId in layoutIds) {
            val layout = findViewById<LinearLayout>(layoutId)
            layout?.let {
                for (i in 0 until it.childCount) {
                    val child = it.getChildAt(i)
                    if (child is EditText) {
                        child.setText(text)
                    }
                }
            }
        }
    }


    private fun setEnableInput(isEnabled: Boolean) {
        // Danh sách các LinearLayout ID

        // Duyệt qua từng LinearLayout và cập nhật trạng thái cho các EditText bên trong
        for (layoutId in layoutIds) {
            val layout = findViewById<LinearLayout>(layoutId)
            layout?.let {
                for (i in 0 until it.childCount) {
                    val child = it.getChildAt(i)
                    if (child is EditText) {
                        child.isEnabled = isEnabled
                    }
                }
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


    override fun onClickNumber(mode: String, value: String) {

        checkEnableButtonResult()

        when (mode) {
            "price_a" -> {

                val currentText = binding.tvPriceA.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""

                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvPriceA.setText(format.toString())
                    checkEnableButtonResult()

                    if (binding.tvQuantityA.text.length > 0) {
                        caculatorPrice(
                            price = binding.tvPriceA,
                            quantity = binding.tvQuantityA,
                            unit = binding.tvUnitPriceA
                        )

                    }
                }

            }

            "quantity_a" -> {

                val currentText = binding.tvQuantityA.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    if (binding.tvQuantityA.text.length > 0) {
                        var unit =
                            clearDotsAndCommas(binding.tvPriceA.text.toString()).toDouble() / clearDotsAndCommas(
                                binding.tvQuantityA.text.toString()
                            ).toDouble()
                        var format = df.format(unit)
                        binding.tvUnitPriceA.setText(format)
                        checkEnableButtonResult()

                    }
                    binding.tvQuantityA.setText(format.toString())
                    checkEnableButtonResult()

                    if (binding.tvPriceA.text.length > 0) {
                        caculatorQuantity(
                            price = binding.tvPriceA,
                            quantity = binding.tvQuantityA,
                            unitPrice = binding.tvUnitPriceA
                        )
                    }
                }

            }

            "unit_a" -> {

                val currentText = binding.tvUnitPriceA.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvUnitPriceA.setText(format.toString())
                    checkEnableButtonResult()

                    if (binding.tvPriceA.text.length > 0) {
                        caculatorUnit(
                            price = binding.tvPriceA,
                            quantity = binding.tvQuantityA,
                            unit = binding.tvUnitPriceA
                        )
                    }
                }


            }

            "price_b" -> {

                val currentText = binding.tvPriceB.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvPriceB.setText(format.toString())
                    checkEnableButtonResult()

                    if (binding.tvQuantityB.text.length > 0) {

                        caculatorPrice(
                            price = binding.tvPriceB,
                            quantity = binding.tvQuantityB,
                            unit = binding.tvUnitPriceB
                        )
                    }
                }

            }

            "quantity_b" -> {

                val currentText = binding.tvQuantityB.text.toString() ?: ""

                var clearText: String =
                    if (currentText.isNotEmpty()) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvQuantityB.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvPriceB.text.length > 0) {
                        caculatorQuantity(
                            price = binding.tvPriceB,
                            quantity = binding.tvQuantityB,
                            unitPrice = binding.tvUnitPriceB
                        )

                    }
                }

            }

            "unit_b" -> {

                val currentText = binding.tvUnitPriceB.text.toString() ?: ""
                var clearText: String =
                    if (currentText.isNotEmpty()) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvUnitPriceB.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvPriceB.text.length > 0) {
                        caculatorUnit(
                            price = binding.tvPriceB,
                            quantity = binding.tvQuantityB,
                            unit = binding.tvUnitPriceB
                        )

                    }
                }


            }

            "price_c" -> {
                val currentText = binding.tvPriceC.text.toString() ?: ""

                var clearText: String =
                    if (currentText.isNotEmpty()) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvPriceC.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvQuantityC.text.length > 0) {
                        caculatorPrice(
                            price = binding.tvPriceC,
                            quantity = binding.tvQuantityB,
                            unit = binding.tvUnitPriceC
                        )

                    }
                }

            }

            "quantity_c" -> {
                val currentText = binding.tvQuantityC.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvQuantityC.setText(format.toString())
                    checkEnableButtonResult()



                    if (binding.tvPriceC.text.length > 0) {
                        caculatorQuantity(
                            price = binding.tvPriceC,
                            quantity = binding.tvQuantityC,
                            unitPrice = binding.tvUnitPriceC
                        )


                    }
                }

            }

            "unit_c" -> {
                val currentText = binding.tvUnitPriceC.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvUnitPriceC.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvPriceC.text.length > 0) {
                        caculatorUnit(
                            price = binding.tvPriceC,
                            quantity = binding.tvQuantityC,
                            unit = binding.tvUnitPriceC
                        )


                    }
                }

            }

            "price_d" -> {
                val currentText = binding.tvPriceD.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvPriceD.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvQuantityD.text.length > 0) {
                        caculatorPrice(
                            price = binding.tvPriceD,
                            quantity = binding.tvQuantityD,
                            unit = binding.tvUnitPriceD
                        )

                    }
                }

            }

            "quantity_d" -> {
                val currentText = binding.tvQuantityD.text.toString() ?: ""

                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvQuantityD.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvPriceD.text.length > 0) {
                        caculatorQuantity(
                            price = binding.tvPriceD,
                            quantity = binding.tvQuantityD,
                            unitPrice = binding.tvUnitPriceD
                        )

                    }
                }

            }

            "unit_d" -> {
                val currentText = binding.tvUnitPriceD.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                if (isNumberLengthValid(concatenatedText)) {
                    val format: String = try {
                        df.format(concatenatedText.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        concatenatedText
                    }
                    binding.tvUnitPriceD.setText(format.toString())
                    checkEnableButtonResult()


                    if (binding.tvPriceD.text.isNotEmpty()) {

                        caculatorUnit(
                            price = binding.tvPriceD,
                            quantity = binding.tvQuantityD,
                            unit = binding.tvUnitPriceD
                        )

                    }
                }

            }

        }
    }

    private fun caculatorUnit(price: EditText, quantity: EditText, unit: EditText) {
        Log.d("53452352", "%3452345")
        if (price.text.isNotEmpty() && unit.text.isNotEmpty()) {
            var result =
                clearDotsAndCommas(price.text.toString()).toDouble() / clearDotsAndCommas(unit.text.toString()).toDouble()
            var format = df.format(result)

            quantity.setText(format)
            checkEnableButtonResult()
        }
    }


    override fun onClearItem(mode: String) {
        when (mode) {
            "price_a" -> {
                val currentText = binding.tvPriceA.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvPriceA.setText(valueFormat.toString())
                    if (binding.tvQuantityA.text.toString().length == 0) {
                        binding.tvUnitPriceA.setText("")
                        checkEnableButtonResult()
                    } else if (binding.tvQuantityA.text.length > 0) {
                        if (binding.tvPriceA.text.toString().length > 0) {
                            caculatorPrice(
                                price = binding.tvPriceA,
                                quantity = binding.tvQuantityA,
                                unit = binding.tvUnitPriceA
                            )
                        }
                    }

                }
            }

            "quantity_a" -> {
                val currentText = binding.tvQuantityA.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)


                    binding.tvQuantityA.setText(valueFormat)

                    if (binding.tvQuantityA.text.toString().length == 0) {
                        binding.tvUnitPriceA.setText("")
                        checkEnableButtonResult()

                    } else if (binding.tvQuantityA.text.length > 0) {
//                        caculatorQuantityA()

                        if (binding.tvQuantityA.text.toString().length > 0) {
                            caculatorQuantity(
                                price = binding.tvPriceA,
                                quantity = binding.tvQuantityA,
                                unitPrice = binding.tvUnitPriceA
                            )

                        }

                    }

                }
            }

            "unit_a" -> {
                val currentText = binding.tvUnitPriceA.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvUnitPriceA.setText(valueFormat)

                    if (binding.tvUnitPriceA.text.toString().length == 0) {
                        binding.tvQuantityA.setText("")
                        checkEnableButtonResult()

                    } else if (binding.tvPriceA.text.length > 0) {
                        if (binding.tvUnitPriceA.text.length > 0) {
                            caculatorUnit(
                                price = binding.tvPriceA,
                                quantity = binding.tvQuantityA,
                                unit = binding.tvUnitPriceA
                            )
                        }

                    }


                }
            }


            "price_b" -> {
                val currentText = binding.tvPriceB.text ?: ""
                if (currentText.isNotEmpty()) {
                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvPriceB.setText(valueFormat)
                    if (binding.tvQuantityB.text.toString().length == 0) {
                        binding.tvUnitPriceB.setText("")
                        checkEnableButtonResult()
                    } else if (binding.tvQuantityB.text.toString().length > 0) {
                        if (binding.tvPriceB.text.toString().length > 0) {
                            caculatorPrice(
                                price = binding.tvPriceB,
                                quantity = binding.tvQuantityB,
                                unit = binding.tvUnitPriceB
                            )
                        }
                    }
                }
            }

            "quantity_b" -> {
                val currentText = binding.tvQuantityB.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvQuantityB.setText(valueFormat)
                    if (binding.tvQuantityB.text.toString().length == 0) {
                        binding.tvUnitPriceB.setText("")
                        checkEnableButtonResult()
                    } else if (binding.tvQuantityB.text.length > 0) {
                        if (binding.tvQuantityB.text.length > 0) {
                            caculatorQuantity(
                                price = binding.tvPriceB,
                                quantity = binding.tvQuantityB,
                                unitPrice = binding.tvUnitPriceB
                            )
                        }
                    }
                }
            }

            "unit_b" -> {
                val currentText = binding.tvUnitPriceB.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvUnitPriceB.setText(valueFormat)
                    if (binding.tvUnitPriceB.text.toString().length == 0) {
                        binding.tvQuantityB.setText("")
                        checkEnableButtonResult()

                    } else if (binding.tvUnitPriceB.text.length > 0) {
                        if (binding.tvUnitPriceB.text.length > 0) {
                            caculatorUnit(
                                price = binding.tvPriceB,
                                quantity = binding.tvQuantityB,
                                unit = binding.tvUnitPriceB
                            )
                        }
                    }
                }
            }

            "price_c" -> {
                val currentText = binding.tvPriceC.text ?: ""
                if (currentText.isNotEmpty()) {
                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvPriceC.setText(valueFormat)
                    if (binding.tvQuantityC.text.toString().length == 0) {
                        binding.tvUnitPriceC.setText("")
                        checkEnableButtonResult()

                    } else if (binding.tvQuantityC.text.toString().length > 0) {
                        if (binding.tvPriceC.text.length > 0) {
                            caculatorPrice(
                                price = binding.tvPriceC,
                                quantity = binding.tvQuantityC,
                                unit = binding.tvUnitPriceC
                            )
                        }
                    }
                }
            }

            "quantity_c" -> {
                val currentText = binding.tvQuantityC.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvQuantityC.setText(valueFormat)

                    if (binding.tvQuantityC.text.toString().length == 0) {
                        binding.tvUnitPriceC.setText("")
                        checkEnableButtonResult()
                    } else if (binding.tvQuantityC.text.toString().length > 0) {

                        if (binding.tvPriceC.text.length > 0) {
                            caculatorQuantity(
                                price = binding.tvPriceC,
                                quantity = binding.tvQuantityC,
                                unitPrice = binding.tvUnitPriceC
                            )
                        }

                    }
                }
            }

            "unit_c" -> {
                val currentText = binding.tvUnitPriceC.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvUnitPriceC.setText(valueFormat)
                    if (binding.tvUnitPriceC.text.toString().length == 0) {
                        binding.tvQuantityC.setText("")
                        checkEnableButtonResult()
                    } else if (binding.tvUnitPriceC.text.toString().length > 0) {
                        if (binding.tvPriceC.text.length > 0) {
                            caculatorUnit(
                                price = binding.tvPriceC,
                                quantity = binding.tvQuantityC,
                                unit = binding.tvUnitPriceC
                            )
                        }
                    }
                }
            }

            "price_d" -> {
                val currentText = binding.tvPriceD.text ?: ""
                if (currentText.isNotEmpty()) {


                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvPriceD.setText(valueFormat)
                    if (binding.tvQuantityD.text.toString().length == 0) {
                        binding.tvUnitPriceD.setText("")
                        caculatorPrice(
                            price = binding.tvPriceD,
                            quantity = binding.tvQuantityD,
                            unit = binding.tvUnitPriceD
                        )

                    }
                }
            }

            "quantity_d" -> {
                val currentText = binding.tvQuantityD.text ?: ""
                if (currentText.isNotEmpty()) {


                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvQuantityD.setText(valueFormat)
                    if (binding.tvQuantityD.text.toString().length == 0) {
                        binding.tvUnitPriceD.setText("")
                        checkEnableButtonResult()
                    } else if (binding.tvQuantityD.text.toString().length > 0) {
                        if (binding.tvPriceD.text.length > 0) {
                            caculatorQuantity(
                                price = binding.tvUnitPriceD,
                                quantity = binding.tvPriceD,
                                unitPrice = binding.tvUnitPriceD
                            )
                        }
                    }
                }
            }

            "unit_d" -> {
                val currentText = binding.tvUnitPriceD.text ?: ""
                if (currentText.isNotEmpty()) {

                    val value = currentText.substring(0, currentText.length - 1)
                    var valueFormat = formatTextInput(value)

                    binding.tvUnitPriceD.setText(valueFormat)
                    if (binding.tvUnitPriceD.text.toString().length == 0) {
                        binding.tvQuantityD.setText("")
                        checkEnableButtonResult()

                    } else if (binding.tvUnitPriceD.text.toString().length > 0) {
                        if (binding.tvPriceD.text.isNotEmpty()) {
                            caculatorUnit(
                                price = binding.tvPriceD,
                                quantity = binding.tvQuantityD,
                                unit = binding.tvUnitPriceD
                            )
                        }
                    }
                }
            }

        }
    }

    private fun formatTextInput(value: String): String {
        var valueFormat = ""
        if (value.toString().length > 0) {
            val valueClearDot = clearDotsAndCommas(value).toString()
            valueFormat = df.format(valueClearDot.toDouble())
        }
        return valueFormat
    }

    override fun onClearAll(mode: String) {
        when (mode) {
            "price_a" -> {
                binding.tvPriceA.setText("")
                checkEnableButtonResult()

            }

            "price_b" -> {
                binding.tvPriceB.setText("")
                checkEnableButtonResult()

            }

            "price_c" -> {
                binding.tvPriceC.setText("")

            }

            "price_d" -> {
                binding.tvPriceD.setText("")
            }

            "quantity_a" -> {
                binding.tvQuantityA.setText("")
            }

            "quantity_b" -> {
                binding.tvQuantityB.setText("")
            }

            "quantity_c" -> {
                binding.tvQuantityC.setText("")
            }

            "quantity_d" -> {
                binding.tvQuantityD.setText("")
            }

            "unit_a" -> {
                binding.tvUnitPriceA.setText("")
            }

            "unit_b" -> {
                binding.tvUnitPriceB.setText("")
            }

            "unit_c" -> {
                binding.tvUnitPriceC.setText("")
            }

            "unit_d" -> {
                binding.tvUnitPriceD.setText("")
            }
        }
    }

    fun processAllEditTexts(layouts: ArrayList<LinearLayout>): ArrayList<UnitPrice> {

        var listUnit = ArrayList<UnitPrice>()

        for (layout in layouts) {
            var count = 0
            var title = ""
            var value = 0.0

            // Duyệt qua tất cả các View bên trong LinearLayout chính
            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)

                // Lấy title từ TextView đầu tiên
                if (i == 0) {
                    val titleTextView = child as TextView
                    title = titleTextView.text.toString()
                }

                // Kiểm tra nếu View là LinearLayout chứa các EditText
                if (i == 1) {
                    val innerLayout = child as LinearLayout

                    // Duyệt qua các View bên trong innerLayout (LinearLayout con)
                    for (j in 0 until innerLayout.childCount) {
                        val innerChild = innerLayout.getChildAt(j)

                        // Kiểm tra và xử lý EditText
                        when (j) {
                            0 -> {
                                // Đây là EditText với id tvPrice_D
                                val priceEditText = innerChild as EditText
                                val text = priceEditText.text.toString()
                                if (text.isNotEmpty()) {
                                    count++
                                }
                            }

                            1 -> {
                                // Đây là EditText với id tvQuantity_D
                                val quantityEditText = innerChild as EditText
                                val text = quantityEditText.text.toString()
                                if (text.isNotEmpty()) {
                                    count++
                                }
                            }

                            2 -> {
                                // Đây là EditText với id tvUnitPrice_D
                                val unitPriceEditText = innerChild as EditText
                                val text = unitPriceEditText.text.toString()
                                if (text.isNotEmpty()) {
                                    value = clearDotsAndCommas(text).toDouble()
                                }
                            }
                        }
                    }
                }
            }

            // Thêm vào danh sách nếu điều kiện đúng
            if (count == 2) {
                listUnit.add(UnitPrice(value, title))
            }
        }

        return listUnit
    }

//    data class UnitPrice(val title: String, val value: String)

    data class UnitPrice(val value: Double, val title: String)

    fun formatUnitPrices(unitPrices: List<UnitPrice>): String {
        // Sắp xếp theo giá trị giảm dần, nếu giá trị giống nhau thì sắp xếp theo tiêu đề tăng dần
        val sortedPrices =
            unitPrices.sortedWith(compareByDescending<UnitPrice> { it.value }.thenBy { it.title })

        // Nhóm theo giá trị
        val groupedByValue = sortedPrices.groupBy { it.value }


        // Tạo chuỗi kết quả
        val result = groupedByValue.entries.joinToString(" > ") { entry ->
            entry.value.joinToString(" = ") { it.title }
        }

        return result
    }

    private fun caculatorQuantity(price: EditText, quantity: EditText, unitPrice: EditText) {
        if (price.text.isNotEmpty() && quantity.text.isNotEmpty()) {
            var unit =
                clearDotsAndCommas(price.text.toString()).toDouble() / clearDotsAndCommas(quantity.text.toString()).toDouble()

            val roundedNumber = String.format("%.2f", unit).toDouble()

            if (roundedNumber.toString().length <= 4) {
                unitPrice.setText(roundedNumber.toString())
            } else {
                var format = df.format(unit)
                unitPrice.setText(format)
                checkEnableButtonResult()
            }
        }
    }

    private fun caculatorQuantityA() {
        var unit =
            clearDotsAndCommas(binding.tvPriceA.text.toString()).toDouble() / clearDotsAndCommas(
                binding.tvQuantityA.text.toString()
            ).toDouble()
        var format = df.format(unit)
        binding.tvUnitPriceA.setText(format)
        checkEnableButtonResult()
    }

    private fun caculatorPrice(price: EditText, quantity: EditText, unit: EditText) {

        if (price.text.isNotEmpty() && quantity.text.isNotEmpty()) {
            var unitClear =
                clearDotsAndCommas(price.text.toString()).toDouble() / clearDotsAndCommas(quantity.text.toString()).toDouble()
            var format = df.format(unitClear)
            unit.setText(format.toString())
            checkEnableButtonResult()
        }

    }


    fun showInputDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancle: Button = dialog.findViewById(R.id.btnCancle)
        val btnOk: Button = dialog.findViewById(R.id.btnOk)

        btnCancle.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            clearAllEditext("")
            checkEnableButtonResult()
            setEnableInput(true)
            dialog.dismiss()
        }

// Set width và height của dialog là match_parent
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialog.window?.attributes = layoutParams
        dialog.show()


    }


}