package com.example.matheasyapp.view.calculate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.matheasyapp.Calculator
import com.example.matheasyapp.Expression
import com.example.matheasyapp.MyPreferences
import com.example.matheasyapp.NumberFormatter
import com.example.matheasyapp.R
import com.example.matheasyapp.TextSizeAdjuster
import com.example.matheasyapp.adapter.AdapterViewPager2
import com.example.matheasyapp.adapter.CaculatorPagerAdapter
import com.example.matheasyapp.bottomsheft.BottomSheftStateCaculator
import com.example.matheasyapp.databinding.FragmentCaculatorLayoutBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.division_by_0
import com.example.matheasyapp.domain_error
import com.example.matheasyapp.is_infinity
import com.example.matheasyapp.livedata.CaculatorViewModel
import com.example.matheasyapp.model.History
import com.example.matheasyapp.require_real_number
import com.example.matheasyapp.syntax_error
import com.example.matheasyapp.view.toast.showCustomToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormatSymbols
import java.util.Locale


class CaculatorFragment : Fragment(), BottomSheftStateCaculator.callBackFunction {

    lateinit var iView: View;
    private var isEqualLastAction = false

//    private  var  checkShow = false

    private var isInvButtonClicked = false

    private var isDegreeModeActivated = true

    private lateinit var database: HistoryDatabase

    private var calculationResult = BigDecimal.ZERO

    private val decimalSeparatorSymbol =
        DecimalFormatSymbols.getInstance().decimalSeparator.toString()

    private val groupingSeparatorSymbol =
        DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private lateinit var binding: FragmentCaculatorLayoutBinding

    private lateinit var viewModel: CaculatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val textSizeAdjuster = TextSizeAdjuster(requireActivity())

        binding = FragmentCaculatorLayoutBinding.inflate(layoutInflater, container, false)
        iView = binding.root

        database = HistoryDatabase.getDatabase(requireActivity())

        binding.keyboard2.visibility = View.GONE

        viewModel = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)

        initViewPager()

        binding.btnNumber1.setOnClickListener { onNumberClick(it) }
        binding.btnNumber2.setOnClickListener { onNumberClick(it) }
        binding.btnNumber3.setOnClickListener { onNumberClick(it) }
        binding.btnNumber4.setOnClickListener { onNumberClick(it) }
        binding.btnNumber5.setOnClickListener { onNumberClick(it) }
        binding.btnNumber6.setOnClickListener { onNumberClick(it) }
        binding.btnNumber7.setOnClickListener { onNumberClick(it) }
        binding.btnNumber8.setOnClickListener { onNumberClick(it) }
        binding.btnNumber9.setOnClickListener { onNumberClick(it) }
        binding.btnNumber0.setOnClickListener { onNumberClick(it) }
        binding.btnNumber00.setOnClickListener { onNumberClick(it) }

        binding.btnNumber0Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber1Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber2Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber3Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber4Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber5Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber6Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber7Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber8Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber9Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber0Inv.setOnClickListener { onNumberClick(it) }
        binding.btnNumber00Inv.setOnClickListener { onNumberClick(it) }

        binding.addButton.setOnClickListener { addSymbol(it, "+") }
        binding.subtractButton.setOnClickListener { addSymbol(it, "-") }
        binding.multiplyButton.setOnClickListener { addSymbol(it, getString(R.string.multiply)) }
        binding.divideButton.setOnClickListener { addSymbol(it, "÷") }

        binding.addButtonInv.setOnClickListener { addSymbol(it, "+") }
        binding.subtractButtonInv.setOnClickListener { addSymbol(it, "-") }
        binding.multiplyButtonInv.setOnClickListener { addSymbol(it, getString(R.string.multiply)) }
        binding.divideButtonInv.setOnClickListener { addSymbol(it, "÷") }
        binding.equalsButton.setOnClickListener { equalsButton(it) }

        binding.sineButton.setOnClickListener { sineButton(it) }
        binding.cosineButton.setOnClickListener { cosineButton(it) }
        binding.tangentButton.setOnClickListener { tangentButton(it) }
        binding.buttonOpen.setOnClickListener { parenthesesButton(it) }
        binding.buttonClose.setOnClickListener { parenthesesButton(it) }
        binding.btnClear.setOnClickListener { clearButton(it) }
        binding.clearButton.setOnClickListener { clearButton(it) }
        binding.squareButton.setOnClickListener { squareButton(it) }
        binding.btnPoint.setOnClickListener { pointButton(it) }
        binding.btnPointInv.setOnClickListener { pointButton(it) }

        binding.logarithmButton.setOnClickListener { logarithmButton(it) }
        binding.naturalLogarithmButton.setOnClickListener { naturalLogarithmButton(it) }
        binding.divideBy100Button.setOnClickListener { percent(it) }


        binding.eButton.setOnClickListener { eButton(it) }
        binding.piButton.setOnClickListener { piButton(it) }



        binding.backspaceButton.setOnClickListener { backspaceButton(it) }
        binding.exponentButton.setOnClickListener { exponentButton(it) }

        binding.changeStateKeyboard1.setOnClickListener {
            binding.keyboard1.visibility = View.GONE
            binding.keyboard2.visibility = View.VISIBLE
            changeStateCaculatorInv(true);

        }

        binding.changeStateKeyboard2.setOnClickListener {
            binding.keyboard2.visibility = View.GONE
            binding.keyboard1.visibility = View.VISIBLE
        }


        binding.backspaceButton.setOnLongClickListener {
            binding.tvInput.setText("")
//            binding.tvResult.text = ""
            viewModel.setValueResult("")
            viewModel.setValueCal("")

            true
        }

        binding.btnChangeOperator.setOnClickListener {
            if (!isInvButtonClicked) {
                isInvButtonClicked = true

                changeStateCaculatorInv(isInvButtonClicked);

            } else {

                isInvButtonClicked = false
                changeStateCaculatorInv(isInvButtonClicked)

            }
        }


        binding.layoutChangeState.setOnClickListener {
//            val bottomSheetFragment = BottomSheftStateCaculator()
//            bottomSheetFragment.show(this,bottomSheetFragment.tag)

            val currentItem = binding.viewpager.currentItem
            var nameCurrent = "";
            if (currentItem == 0) {
                nameCurrent = "caculator"
            } else if (currentItem == 1) {
                nameCurrent = "unit"

            } else if (currentItem == 2) {
                nameCurrent = "money"
            }

            val dialog = BottomSheftStateCaculator.newInstance(nameCurrent)
            dialog.setTargetFragment(this, 1)
            dialog.show(parentFragmentManager, "MyDialogFragment")

        }


        fun isNumericString(input: String): Boolean {
            // Kiểm tra từng ký tự trong chuỗi
            for (char in input) {
                // Nếu ký tự không phải là số, trả về false
                if (!char.isDigit()) {
                    return false
                }
            }
            // Nếu tất cả các ký tự đều là số, trả về true
            return true
        }


//        // Handle changes into input to update resultDisplay
        binding.tvInput.addTextChangedListener(object : TextWatcher {
            private var beforeTextLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeTextLength = s?.length ?: 0


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

//                println("iiiwiuiwu ${s.toString()}")
//                println("checck ${isExponentGreaterThanOrEqualTo419(s.toString())}")

                if (isExponentGreaterThanOrEqualTo419(s.toString())) {
                    viewModel.setShowTvResult(true)
                    is_infinity = false
                } else {
                    viewModel.setShowTvResult(false)
                    is_infinity = true
                }

                updateResultDisplay()
                textSizeAdjuster.adjustTextSize(
                    binding.tvInput,
                    TextSizeAdjuster.AdjustableTextType.Input
                )

            }

            override fun afterTextChanged(s: Editable?) {


                s?.let {

                    if (it.isNotEmpty()) {
                        val lastChar = it.last()

//                        binding.tvCalculation.text = binding.tvInput.text
                        viewModel.setValueCal(binding.tvInput.text.toString())


                        val value = s.toString()

                        if (lastChar.isDigit()) {

                            if (isExponentGreaterThanOrEqualTo419(binding.tvInput.text.toString())) {
                                viewModel.setShowTvResult(true)
                                viewModel.setValueResult(binding.tvInput.text.toString())
                            } else {
                                viewModel.setShowTvResult(false)
//                             viewModel.setValueResult(binding.tvInput.text.toString())
                            }

                        } else {

//                            binding.tvResult.visibility = View.GONE
//                            binding.tvResult.text = binding.tvInput.text
                            viewModel.setShowTvResult(false)
//                            viewModel.setValueResult(binding.tvInput.text.toString())

                        }

                    }
                }

            }
        })

        return iView
    }


    fun isExponentGreaterThanOrEqualTo419(input: String): Boolean {

        // Kiểm tra xem chuỗi có chứa dấu '^' hay không
        if (!input.contains('^')) return true

        // Tách chuỗi thành cơ số và số mũ
        val parts = input.split('^')
        if (parts.size != 2) return false

        // Lấy phần số mũ
        val exponent = parts[1].replace(",", "").toDoubleOrNull() ?: return false

        // Kiểm tra xem số mũ có lớn hơn hoặc bằng 419 hay không
        return exponent <= 419
    }


    fun removeDotsAndCommas(input: String): String {
        return input.replace(Regex("[.,]"), "")
    }

    private fun initViewPager() {

        binding.viewpager.adapter = AdapterViewPager2(requireActivity())

        binding.viewpager.setUserInputEnabled(false);

        binding.viewpager.currentItem = 0
        binding.tvTitleCurrentPage.setText(R.string.txt_caculator_machine)

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                println("callll ${position}")
                super.onPageSelected(position)

            }
        })


    }


    private fun changeStateCaculatorInv(invButtonClicked: Boolean) {
        if (invButtonClicked) {
            binding.sineButton.setText(R.string.sine)
            binding.cosineButton.setText(R.string.cosine)
            binding.tangentButton.setText(R.string.tangent)
        } else {
            binding.sineButton.setText(R.string.sineInv)
            binding.cosineButton.setText(R.string.cosineInv)
            binding.tangentButton.setText(R.string.tangentInv)
        }
    }


    fun checkLastPartLength(expression: String): Boolean {
        // Danh sách các toán tử và ký hiệu toán học
        val operators = listOf(
            "+",
            "-",
            "×",
            "%",
            "/",
            "(",
            ")",
            "√",
            "cos(",
            "log(",
            "exp(",
            "sin(",
            "tan(",
            "arc",
            "sqrt",
            "factorial",
            "÷"
        )

        // Tìm vị trí của toán tử cuối cùng
        var lastOperatorIndex = -1
        for (operator in operators) {
            val index = expression.lastIndexOf(operator)
            if (index > lastOperatorIndex) {
                lastOperatorIndex = index
            }
        }

        // Nếu không tìm thấy toán tử nào, coi toàn bộ chuỗi là phần cần kiểm tra
        if (lastOperatorIndex == -1) {
            return expression.length <= 18
        }

        // Lấy phần sau toán tử cuối cùng
        val partAfterLastOperator = expression.substring(lastOperatorIndex + 1).trim()

        // Kiểm tra số ký tự của phần sau toán tử
        return partAfterLastOperator.length <= 18
    }


    fun onNumberClick(view: View) {

        if (checkLastPartLength(binding.tvInput.text.toString())) {
//            println("tesst ${binding.tvInput.text.toString()}")
            updateDisplay(view, (view as Button).text as String)
        }
    }

    fun equalsButton(view: View) {
        lifecycleScope.launch(Dispatchers.Default) {

            val calculation = binding.tvInput.text.toString()

            if (calculation != "") {

                val resultString = calculationResult.toString()
                var formattedResult = NumberFormatter.format(
                    resultString.replace(".", decimalSeparatorSymbol),
                    decimalSeparatorSymbol,
                    groupingSeparatorSymbol
                )

                // If result is a number and it is finite
                if (!(division_by_0 || domain_error || syntax_error || is_infinity || require_real_number)) {

                    print("fsdfasd");

                    // Remove zeros at the end of the results (after point)
                    val resultSplited = resultString.split('.')
                    if (resultSplited.size > 1) {
                        val resultPartAfterDecimalSeparator = resultSplited[1].trimEnd('0')
                        var resultWithoutZeros = resultSplited[0]
                        if (resultPartAfterDecimalSeparator != "") {
                            resultWithoutZeros =
                                resultSplited[0] + "." + resultPartAfterDecimalSeparator
                        }
                        formattedResult = NumberFormatter.format(
                            resultWithoutZeros.replace(
                                ".",
                                decimalSeparatorSymbol
                            ), decimalSeparatorSymbol, groupingSeparatorSymbol
                        )
                    }

                    // Hide the cursor before updating binding.input to avoid weird cursor movement
                    withContext(Dispatchers.Main) {
                        binding.tvInput.isCursorVisible = false
                    }

                    // Display result
                    withContext(Dispatchers.Main) {

                        var result: String = formattedResult

                        val check: Boolean = containsMathSymbols(calculation)

                        val checkChar: Boolean = checkLastCharIsDigit(calculation)

                        if (check && checkChar) {
                            addHistory(result, calculation)
                        }

                        binding.tvInput.setText(formattedResult)

                    }

                    // Set cursor
                    withContext(Dispatchers.Main) {
                        // Scroll to the end
                        binding.tvInput.setSelection(binding.tvInput.length())

                        // Hide the cursor (do not remove this, it's not a duplicate)
                        binding.tvInput.isCursorVisible = false


                        // Clear resultDisplay
//                        binding.tvResult.text = ""
                    }

                    isEqualLastAction = true
                } else {
                    withContext(Dispatchers.Main) {
                        val view: Button = Button(requireActivity())

                        if (require_real_number) {

                            clearButton(view)
                            showCustomToast(requireActivity(), "Vui lòng nhập một biểu thức hợp lệ")
                        }

                        if (is_infinity) {
                            clearButton(view)
                            viewModel.setShowTvResult(false)
                            is_infinity = false
                        }

//                        if (syntax_error) {
//                            setErrorColor(true)
//                            binding.resultDisplay.text = getString(R.string.syntax_error)
//                        } else if (domain_error) {
//                            setErrorColor(true)
//                            binding.resultDisplay.text = getString(R.string.domain_error)
//                        } else if (require_real_number) {
//                            setErrorColor(true)
//                            binding.resultDisplay.text = getString(R.string.require_real_number)
//                        } else if (division_by_0) {
//                            setErrorColor(true)
//                            binding.resultDisplay.text = getString(R.string.division_by_0)
//                        } else if (is_infinity) {
//                            if (calculationResult < BigDecimal.ZERO) binding.resultDisplay.text = "-" + getString(
//                                R.string.infinity
//                            )
//                            else binding.resultDisplay.text = getString(R.string.value_too_large)
//                            //} else if (result.isNaN()) {
//                            //    setErrorColor(true)
//                            //    binding.resultDisplay.setText(getString(R.string.math_error))
//                        } else {
//                            binding.resultDisplay.text = formattedResult
//                            isEqualLastAction =
//                                true // Do not clear the calculation (if you click into a number) if there is an error
//                        }
                    }
                }

            } else {
//                withContext(Dispatchers.Main) { binding.tvResult.text = "" }
            }
        }
    }

    fun checkLastCharIsDigit(inputStr: String): Boolean {
        // Kiểm tra nếu chuỗi là rỗng
        if (inputStr.isEmpty()) {
            return false
        }

        // Lấy ký tự cuối cùng của chuỗi
        val lastChar = inputStr.last()

        // Kiểm tra nếu ký tự cuối cùng là số
        return lastChar.isDigit()
    }

    fun containsMathSymbols(input: String): Boolean {
        val mathSymbolsRegex = Regex("[+\\-*/()sincostan]")
        return mathSymbolsRegex.containsMatchIn(input)
    }

    private fun addHistory(result: String, calculation: String) {

        lifecycleScope.launch {
            var history = History(result, calculation)
            val newId = database.Dao().insert(history)


            // Kiểm tra xem insert có thành công hay không
            if (newId != -1L) {
                // Thêm thành công, sử dụng newId nếu cần
                println("Insert successful, new ID: $newId")
            } else {
                // Thêm thất bại
                println("Insert failed")
            }

        }

    }

    private fun updateResultDisplay() {
        lifecycleScope.launch(Dispatchers.Default) {

            val calculation = binding.tvInput.text.toString()

            if (calculation != "") {
                division_by_0 = false
                domain_error = false
                syntax_error = false
//                is_infinity = false
                require_real_number = false

                val calculationTmp = Expression().getCleanExpression(
                    binding.tvInput.text.toString(),
                    decimalSeparatorSymbol,
                    groupingSeparatorSymbol
                )

                calculationResult =
                    Calculator(MyPreferences(requireActivity()).numberPrecision!!.toInt()).evaluate(
                        calculationTmp,
                        isDegreeModeActivated
                    )

//                println("tinh toan ket qua ${calculationResult}")

                // If result is a number and it is finite


                if (!(division_by_0 || domain_error || syntax_error || is_infinity || require_real_number)) {

                    // Round
                    calculationResult = roundResult(calculationResult)

                    var formattedResult = NumberFormatter.format(
                        calculationResult.toString().replace(".", decimalSeparatorSymbol),
                        decimalSeparatorSymbol,
                        groupingSeparatorSymbol
                    )

                    println("roundhhhh ${formattedResult}")

                    // Remove zeros at the end of the results (after point)
                    if (!MyPreferences(requireActivity()).numberIntoScientificNotation || !(calculationResult >= BigDecimal(
                            9999
                        ) || calculationResult <= BigDecimal(0.1))
                    ) {
                        val resultSplited = calculationResult.toString().split('.')
                        if (resultSplited.size > 1) {
                            val resultPartAfterDecimalSeparator = resultSplited[1].trimEnd('0')
                            var resultWithoutZeros = resultSplited[0]
                            if (resultPartAfterDecimalSeparator != "") {
                                resultWithoutZeros =
                                    resultSplited[0] + "." + resultPartAfterDecimalSeparator
                            }
                            formattedResult = NumberFormatter.format(
                                resultWithoutZeros.replace(
                                    ".",
                                    decimalSeparatorSymbol
                                ), decimalSeparatorSymbol, groupingSeparatorSymbol
                            )
                        }
                    }

                    // show result on tvResult

//                    println.("${isValidNumber(formattedResult)}")

//                    var check =

                    withContext(Dispatchers.Main) {
                        if (formattedResult != calculation) {
                            println("3ffsfasfsafa")
                            viewModel.setValueResult(formattedResult)
                        } else {
                            viewModel.setValueResult(binding.tvInput.text.toString())

                        }
                    }

                } else withContext(Dispatchers.Main) {
//                    if (is_infinity && !division_by_0 && !domain_error && !require_real_number) {

//                        if (calculationResult < BigDecimal.ZERO)
                    /*  binding.tvResult.text =
                      "-" + "Infinity"*/
//                        try {//                        else binding.tvResult.text = "value_too_large"
//                        } catch (e: Exception) {
//                            TODO("Not yet implemented")
//                        }


                    if (division_by_0) {
                        println("callaalalal ${binding.tvInput.toString()}")

                        if (binding.tvInput.text.toString().contains("÷0")) {
                            showCustomToast(requireActivity(), "Bạn không thể chia cho 0")
                            viewModel.setShowTvResult(false)
                        }


                    } else {
                        withContext(Dispatchers.Main) {

                            viewModel.setValueResult("")
                        }
                    }
                }
            } else {

                withContext(Dispatchers.Main) {
                    viewModel.setValueResult("")

                }

            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun addSymbol(view: View, currentSymbol: String) {
        // Get input text length
        val textLength = binding.tvInput.text.length

        // If the input is not empty
        if (textLength > 0) {
            // Get cursor's current position
            val cursorPosition = binding.tvInput.selectionStart

            // Get next / previous characters relative to the cursor
            val nextChar =
                if (textLength - cursorPosition > 0) binding.tvInput.text[cursorPosition].toString() else "0" // use "0" as default like it's not a symbol
            val previousChar =
                if (cursorPosition > 0) binding.tvInput.text[cursorPosition - 1].toString() else "0"

            if (currentSymbol != previousChar // Ignore multiple presses of the same button
                && currentSymbol != nextChar
                && previousChar != "√" // No symbol can be added on an empty square root
                && previousChar != decimalSeparatorSymbol // Ensure that the previous character is not a comma
                && (previousChar != "(" // Ensure that we are not at the beginning of a parenthesis
                        || currentSymbol == "-")
            ) { // Minus symbol is an override
                // If previous character is a symbol, replace it
                if (previousChar.matches("[+\\-÷×^]".toRegex())) {

                }
//                 If next character is a symbol, replace it
                else if (nextChar.matches("[+\\-÷×^%!]".toRegex())
                    && currentSymbol != "%"
                ) {
                    val leftString = binding.tvInput.text.subSequence(0, cursorPosition).toString()
                    val rightString =
                        binding.tvInput.text.subSequence(cursorPosition + 1, textLength).toString()

                    if (cursorPosition > 0 && previousChar != "(") {
                        binding.tvInput.setText(leftString + currentSymbol + rightString)
                        binding.tvInput.setSelection(cursorPosition + 1)
                    } else if (currentSymbol == "+") binding.tvInput.setText(leftString + rightString)
                }
                // Otherwise just update the display
                else if (cursorPosition > 0 || nextChar != "0" && currentSymbol == "-") {
                    updateDisplay(view, currentSymbol)
                }
            }
        } else { // Allow minus symbol, even if the input is empty
            if (currentSymbol == "-") updateDisplay(view, currentSymbol)

        }
    }

    private fun roundResult(result: BigDecimal): BigDecimal {
        val numberPrecision = MyPreferences(requireActivity()).numberPrecision!!.toInt()
        var newResult = result.setScale(numberPrecision, RoundingMode.HALF_EVEN)
        if (MyPreferences(requireActivity()).numberIntoScientificNotation && (newResult >= BigDecimal(
                9999
            ) || newResult <= BigDecimal(
                0.1
            ))
        ) {
            val scientificString = String.format(Locale.US, "%.4g", result)
            newResult = BigDecimal(scientificString)
        }

        // Fix how is displayed 0 with BigDecimal
        val tempResult = newResult.toString().replace("E-", "").replace("E", "")
        val allCharsEqualToZero = tempResult.all { it == '0' }
        if (
            allCharsEqualToZero
            || newResult.toString().startsWith("0E")
        ) {
            return BigDecimal.ZERO
        }

        return newResult
    }

    fun parenthesesButton(view: View) {
        val cursorPosition = binding.tvInput.selectionStart
        val textLength = binding.tvInput.text.length

        var openParentheses = 0
        var closeParentheses = 0

        val text = binding.tvInput.text.toString()

        for (i in 0 until cursorPosition) {
            if (text[i] == '(') {
                openParentheses += 1
            }
            if (text[i] == ')') {
                closeParentheses += 1
            }
        }

        if (
            !(textLength > cursorPosition && binding.tvInput.text.toString()[cursorPosition] in "×÷+-^")
            && (
                    openParentheses == closeParentheses
                            || binding.tvInput.text.toString()[cursorPosition - 1] == '('
                            || binding.tvInput.text.toString()[cursorPosition - 1] in "×÷+-^"
                    )
        ) {
            updateDisplay(view, "(")
        } else {
            updateDisplay(view, ")")
        }
    }

    fun backspaceButton(view: View) {

        var cursorPosition = binding.tvInput.selectionStart
        val textLength = binding.tvInput.text.length
        var newValue = ""
        var isFunction = false
        var isDecimal = false
        var functionLength = 0

        if (isEqualLastAction) {
            cursorPosition = textLength
        }

        if (cursorPosition != 0 && textLength != 0) {
            // Check if it is a function to delete
            val functionsList =
                listOf("cos⁻¹(", "sin⁻¹(", "tan⁻¹(", "cos(", "sin(", "tan(", "ln(", "log(", "exp(")
            for (function in functionsList) {
                val leftPart = binding.tvInput.text.subSequence(0, cursorPosition).toString()
                if (leftPart.endsWith(function)) {
                    newValue = binding.tvInput.text.subSequence(0, cursorPosition - function.length)
                        .toString() +
                            binding.tvInput.text.subSequence(cursorPosition, textLength).toString()
                    isFunction = true
                    functionLength = function.length - 1
                    break
                }
            }
            // Else
            if (!isFunction) {
                // remove the grouping separator
                val leftPart = binding.tvInput.text.subSequence(0, cursorPosition).toString()
                val leftPartWithoutSpaces = leftPart.replace(groupingSeparatorSymbol, "")
                functionLength = leftPart.length - leftPartWithoutSpaces.length

                newValue = leftPartWithoutSpaces.subSequence(0, leftPartWithoutSpaces.length - 1)
                    .toString() +
                        binding.tvInput.text.subSequence(cursorPosition, textLength).toString()

                isDecimal = binding.tvInput.text[cursorPosition - 1] == decimalSeparatorSymbol[0]
            }

            // Handle decimal deletion as a special case when finding cursor position
            var rightSideCommas = 0
            if (isDecimal) {
                val oldString = binding.tvInput.text
                var immediateRightDigits = 0
                var index = cursorPosition
                // Find number of digits that were previously to the right of the decimal
                while (index < textLength && oldString[index].isDigit()) {
                    index++
                    immediateRightDigits++
                }
                // Determine how many thousands separators that gives us to our right
                if (immediateRightDigits > 3)
                    rightSideCommas = immediateRightDigits / 3
            }

            val newValueFormatted =
                NumberFormatter.format(newValue, decimalSeparatorSymbol, groupingSeparatorSymbol)
            var cursorOffset = newValueFormatted.length - newValue.length - rightSideCommas
            if (cursorOffset < 0) cursorOffset = 0

            binding.tvInput.setText(newValueFormatted)
            viewModel.setValueCal(newValueFormatted)
            if (binding.tvInput.text.length == 0) {
                viewModel.setValueResult("")
            }

            binding.tvInput.setSelection((cursorPosition - 1 + cursorOffset - functionLength).takeIf { it > 0 }
                ?: 0)
        }

    }

    fun squareButton(view: View) {

        updateDisplay(view, "√")

    }

    private fun updateDisplay(view: View, value: String) {

        val valueNoSeparators = value.replace(groupingSeparatorSymbol, "")

        val isValueInt = valueNoSeparators.toIntOrNull() != null

        if (isEqualLastAction) {
            if (isValueInt || value == decimalSeparatorSymbol) {
                binding.tvInput.setText("")
            } else {
                binding.tvInput.setSelection(binding.tvInput.text.length)
                binding.inputHorizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
            }
            isEqualLastAction = false
        }

        if (!binding.tvInput.isCursorVisible) {
            binding.tvInput.isCursorVisible = true
        }

        lifecycleScope.launch(Dispatchers.Default) {

            val formerValue = binding.tvInput.text.toString()
            val cursorPosition = binding.tvInput.selectionStart
            val leftValue = formerValue.subSequence(0, cursorPosition).toString()
            val leftValueFormatted =
                NumberFormatter.format(leftValue, decimalSeparatorSymbol, groupingSeparatorSymbol)
            val rightValue = formerValue.subSequence(cursorPosition, formerValue.length).toString()

            val newValue = leftValue + value + rightValue

            val newValueFormatted =
                NumberFormatter.format(newValue, decimalSeparatorSymbol, groupingSeparatorSymbol)

            withContext(Dispatchers.Main) {

                if (value == decimalSeparatorSymbol && decimalSeparatorSymbol in binding.tvInput.text.toString()) {
                    if (binding.tvInput.text.toString().isNotEmpty()) {

                        var lastNumberBefore = ""
                        if (cursorPosition > 0 && binding.tvInput.text.toString()
                                .substring(0, cursorPosition)
                                .last() in "0123456789\\$decimalSeparatorSymbol"
                        ) {
                            lastNumberBefore = NumberFormatter.extractNumbers(
                                binding.tvInput.text.toString().substring(0, cursorPosition),
                                decimalSeparatorSymbol
                            ).last()

                        }

                        var firstNumberAfter = ""
                        if (cursorPosition < binding.tvInput.text.length - 1) {
                            firstNumberAfter = NumberFormatter.extractNumbers(
                                binding.tvInput.text.toString()
                                    .substring(cursorPosition, binding.tvInput.text.length),
                                decimalSeparatorSymbol
                            ).first()
                        }

                        if (decimalSeparatorSymbol in lastNumberBefore || decimalSeparatorSymbol in firstNumberAfter) {
                            return@withContext
                        }

                    }
                }

                // Update Display
                binding.tvInput.setText(newValueFormatted)

                if (isValueInt) {
                    val cursorOffset = newValueFormatted.length - newValue.length
                    binding.tvInput.setSelection(cursorPosition + value.length + cursorOffset)

                } else {
                    binding.tvInput.setSelection(leftValueFormatted.length + value.length)
                }

            }

        }

    }


    fun percent(view: View) {
        updateDisplay(view, "%")
    }

    fun exponentButton(view: View) {
        updateDisplay(view, "^")
    }

    fun pointButton(view: View) {
        updateDisplay(view, decimalSeparatorSymbol)
    }

    fun sineButton(view: View) {

        view as Button;
        if (view.text.equals("sin")) {
            updateDisplay(view, "sin(")
        } else {
            updateDisplay(view, "sin⁻¹(")
        }

    }

    fun cosineButton(view: View) {
        view as Button;
        if (view.text.equals("cos")) {
            updateDisplay(view, "cos(")
        } else {
            updateDisplay(view, "cos⁻¹(")

        }

    }

    fun tangentButton(view: View) {

        view as Button;
        if (view.text.equals("tan")) {
            updateDisplay(view, "tan(")
        } else {
            updateDisplay(view, "tan⁻¹(")
        }

    }

    fun eButton(view: View) {
        updateDisplay(view, "e")
    }

    fun piButton(view: View) {
        updateDisplay(view, "π")
    }

    fun naturalLogarithmButton(view: View) {
        if (isInvButtonClicked) {
            updateDisplay(view, "ln(")
        } else {
            updateDisplay(view, "exp(")
        }
    }

    fun logarithmButton(view: View) {
//        if (isInvButtonClicked) {
        updateDisplay(view, "log(")
//        } else {
//            updateDisplay(view, "10^")

//        }
    }


    fun clearButton(view: View) {
        binding.tvInput.setText("")
        viewModel.setValueResult("")
        viewModel.setValueCal("")
//        binding.tvResult.text = ""
//        binding.tvCalculation.text = ""

    }


    override fun onOptionSelected(option: String) {

        when (option) {
            "caculator" -> binding.viewpager.currentItem = 0
            "unit" -> binding.viewpager.currentItem = 1
            "money" -> binding.viewpager.currentItem = 2
        }
    }

}

//https://github.com/SujalShah3234/Unit_Converter_app