package com.example.matheasyapp.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.matheasyapp.R
import com.example.matheasyapp.bottomsheft.BottomSheftStateCaculator
import com.example.matheasyapp.databinding.FragmentCaculatorLayoutBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.livedata.CaculatorViewModel
import com.example.matheasyapp.model.History
import com.example.matheasyapp.view.calculate.CalculateFragment
import com.example.matheasyapp.view.calculate.CurrencyConverterFragment
import com.example.matheasyapp.view.calculate.UnitConverFragment
import com.example.matheasyapp.view.calculate.format.Calculator
import com.example.matheasyapp.view.calculate.format.Expression
import com.example.matheasyapp.view.calculate.format.MyPreferences
import com.example.matheasyapp.view.calculate.format.NumberFormatter
import com.example.matheasyapp.view.calculate.format.TextSizeAdjuster
import com.example.matheasyapp.view.calculate.format.division_by_0
import com.example.matheasyapp.view.calculate.format.domain_error
import com.example.matheasyapp.view.calculate.format.is_infinity
import com.example.matheasyapp.view.calculate.format.require_real_number
import com.example.matheasyapp.view.calculate.format.syntax_error
import com.example.matheasyapp.view.toast.showCustomToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private var isDegreeModeActivated = true // Set degree by default

    private lateinit var database: HistoryDatabase

    private var calculationResult = BigDecimal.ZERO

    private val decimalSeparatorSymbol =
        DecimalFormatSymbols.getInstance().decimalSeparator.toString()

    private val groupingSeparatorSymbol =
        DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private lateinit var binding: FragmentCaculatorLayoutBinding

    private lateinit var viewModel: CaculatorViewModel

    private var nameCurrent: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val textSizeAdjuster = TextSizeAdjuster(requireActivity())

        binding = FragmentCaculatorLayoutBinding.inflate(layoutInflater, container, false)
        iView = binding.root

        database = HistoryDatabase.getDatabase(requireActivity())

        binding.keyboard2.visibility = View.GONE

        viewModel = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)

        // Retrieve the Bundle
        val currentPage = arguments?.getString("keyString")

        Log.d("5352352352", currentPage.toString())

        if (currentPage == "currency") {
            nameCurrent = "money"
            replaceFragment(CurrencyConverterFragment(), "currency")
        } else if (currentPage == "unit") {
            nameCurrent = "unit"
            replaceFragment(UnitConverFragment(), "unit")
        } else {
            nameCurrent = "caculator"
            replaceFragment(CalculateFragment(),"caculator")
        }


        binding.btnNumber1.setOnClickListener { onNumberClick(it, "1") }
        binding.btnNumber2.setOnClickListener { onNumberClick(it, "2") }
        binding.btnNumber3.setOnClickListener { onNumberClick(it, "3") }
        binding.btnNumber4.setOnClickListener { onNumberClick(it, "4") }
        binding.btnNumber5.setOnClickListener { onNumberClick(it, "5") }
        binding.btnNumber6.setOnClickListener { onNumberClick(it, "6") }
        binding.btnNumber7.setOnClickListener { onNumberClick(it, "7") }
        binding.btnNumber8.setOnClickListener { onNumberClick(it, "8") }
        binding.btnNumber9.setOnClickListener { onNumberClick(it, "9") }
        binding.btnNumber0.setOnClickListener { onNumberClick(it, "0") }
        binding.btnNumber00.setOnClickListener { onNumberClick(it, "00") }

        binding.btnNumber0Inv.setOnClickListener { onNumberClick(it, "0") }
        binding.btnNumber1Inv.setOnClickListener { onNumberClick(it, "1") }
        binding.btnNumber2Inv.setOnClickListener { onNumberClick(it, "2") }
        binding.btnNumber3Inv.setOnClickListener { onNumberClick(it, "3") }
        binding.btnNumber4Inv.setOnClickListener { onNumberClick(it, "4") }
        binding.btnNumber5Inv.setOnClickListener { onNumberClick(it, "5") }
        binding.btnNumber6Inv.setOnClickListener { onNumberClick(it, "6") }
        binding.btnNumber7Inv.setOnClickListener { onNumberClick(it, "7") }
        binding.btnNumber8Inv.setOnClickListener { onNumberClick(it, "8") }
        binding.btnNumber9Inv.setOnClickListener { onNumberClick(it, "9") }
        binding.btnNumber0Inv.setOnClickListener { onNumberClick(it, "0") }
        binding.btnNumber00Inv.setOnClickListener { onNumberClick(it, "00") }

        binding.addButton.setOnClickListener { addSymbol(it, "+") }
        binding.subtractButton.setOnClickListener { addSymbol(it, "-") }
        binding.multiplyButton.setOnClickListener { addSymbol(it, getString(R.string.multiply)) }
        binding.divideButton.setOnClickListener { addSymbol(it, "÷") }

        binding.addButtonInv.setOnClickListener { addSymbol(it, "+") }
        binding.subtractButtonInv.setOnClickListener { addSymbol(it, "-") }
        binding.multiplyButtonInv.setOnClickListener { addSymbol(it, getString(R.string.multiply)) }
        binding.divideButtonInv.setOnClickListener { addSymbol(it, "÷") }
        binding.equalsButton.setOnClickListener { equalsButton(it) }
        binding.equalsButtonInv.setOnClickListener { equalsButton(it) }

        binding.sineButton.setOnClickListener { sineButton(it) }
        binding.cosineButton.setOnClickListener { cosineButton(it) }
        binding.tangentButton.setOnClickListener { tangentButton(it) }
        binding.buttonOpen.setOnClickListener { parenthesesButton(it) }
        binding.buttonClose.setOnClickListener { parenthesesButton(it) }
        binding.clearButton.setOnClickListener { clearButton(it) }
        binding.btnClearInv.setOnClickListener { clearButton(it) }
        binding.squareButton.setOnClickListener { squareButton(it) }
        binding.btnPoint.setOnClickListener { pointButton(it) }
//        binding.btnPointInv.setOnClickListener { pointButton(it) }

        binding.logarithmButton.setOnClickListener { logarithmButton(it) }
        binding.naturalLogarithmButton.setOnClickListener { naturalLogarithmButton(it) }

        binding.divideBy100Button.setOnClickListener { percent(it) }
        binding.divideBy100ButtonInv.setOnClickListener { percent(it) }

        binding.eButton.setOnClickListener { eButton(it) }
        binding.piButton.setOnClickListener { piButton(it) }
        binding.btnPhi.setOnClickListener { phiButton(it) }
        binding.btnSinh.setOnClickListener { sinhButton(it) }
        binding.backspaceButton.setOnClickListener { backspaceButton(it) }
        binding.exponentButton.setOnClickListener { exponentButton(it) }
        binding.degreeButton.setOnClickListener { degreeButton(it) }

        binding.btnline.setOnClickListener { pressLine(it) }

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

//            val currentItem = binding.viewpager.currentItem

            val dialog = BottomSheftStateCaculator.newInstance(nameCurrent)
            dialog.setTargetFragment(this, 1)
            dialog.show(parentFragmentManager, "MyDialogFragment")

        }

        binding.tvInput.addTextChangedListener(object : TextWatcher {
            private var beforeTextLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeTextLength = s?.length ?: 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isExponentGreaterThanOrEqualTo419(s.toString()) && !require_real_number && !syntax_error && !domain_error) {
                    viewModel.setShowTvResult(true)
                    is_infinity = false
                } else {
//                    viewModel.setShowTvResult(false)
                    is_infinity = true
                }
            }

            override fun afterTextChanged(s: Editable?) {

                updateResultDisplay()

                textSizeAdjuster.adjustTextSize(
                    binding.tvInput,
                    TextSizeAdjuster.AdjustableTextType.Input,
                    50
                )

                s?.let {

                    if (it.isNotEmpty()) {
                        val lastChar = it.last()

                        viewModel.setValueCal(binding.tvInput.text.toString())
                        if (lastChar.isDigit()) {
                            if (isExponentGreaterThanOrEqualTo419(binding.tvInput.text.toString()) && !require_real_number && !syntax_error && !domain_error) {
                                // close
                                viewModel.setShowTvResult(true)

                            } else {
                                viewModel.setShowTvResult(false)
                            }

                        } else if (lastChar.toString().trim().equals("e") || lastChar.toString()
                                .trim()
                                .equals(requireActivity().resources.getString(R.string.pi)) || lastChar.toString()
                                .trim()
                                .equals(requireActivity().resources.getString(R.string.phi)) || lastChar.toString()
                                .trim().equals("%") || it.last() == '(' || it.last() == ')'
                        ) {

                            if (!require_real_number) {
                                viewModel.setShowTvResult(true)
                            }

                        } else {
                            viewModel.setShowTvResult(false)
                        }
                    }
                }

            }
        })

        return iView
    }

    private fun pressLine(view: View) {

        if (binding.tvInput.text.isNotEmpty()) {
            var value: String = resources.getString(R.string.multiply) + "|"
            updateDisplay(view, value)
        } else {
            updateDisplay(view, "|")
        }

    }

    @SuppressLint("SetTextI18n")
    fun degreeButton(view: View) {

        toggleDegreeMode()
        updateResultDisplay()
    }

    private fun toggleDegreeMode() {
        if (isDegreeModeActivated) binding.degreeButton.text = getString(R.string.radian)
        else binding.degreeButton.text = getString(R.string.degree)

//        binding.degreeTextView.text = binding.degreeButton.text

        // Flip the variable afterwards
        isDegreeModeActivated = !isDegreeModeActivated
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


    private fun changeStateCaculatorInv(invButtonClicked: Boolean) {
        if (invButtonClicked) {
            binding.sineButton.setText(R.string.sine)
            binding.cosineButton.setText(R.string.cosine)
            binding.tangentButton.setText(R.string.tangent)
            binding.logarithmButton.setText(R.string.logarithm)
            binding.naturalLogarithmButton.setText(R.string.naturalLogarithm)
            binding.btnPhi.setText(R.string.phi)

        } else {
            binding.sineButton.setText(R.string.sineInv)
            binding.cosineButton.setText(R.string.cosineInv)
            binding.tangentButton.setText(R.string.tangentInv)
            binding.logarithmButton.setText(R.string.sinhInv)
            binding.naturalLogarithmButton.setText(R.string.coshInv)
            binding.btnPhi.setText(R.string.tanhInv)

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
            "÷",
            "|",
            "×|"
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


    fun onNumberClick(view: View, value: String) {

        if (checkLastPartLength(binding.tvInput.text.toString())) {
            updateDisplay(view, value)
        } else {
            showCustomToast(
                requireActivity(),
                "You cannot enter more than 15 digits or 10 digits after the comma"
            )
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
                                ".", decimalSeparatorSymbol
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


                        if (containsArithmeticOperators(
                                binding.tvInput.text.toString().trim()
                            ) || containsSpecialElement(binding.tvInput.text.toString().trim())
                        ) {
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
                        if (syntax_error) {
                            clearButton(view)
                            showCustomToast(
                                requireActivity(), "Vui lòng nhập một biểu thức hợp lệ "
                            )
                        }
                        if (domain_error) {
                            clearButton(view)
                            showCustomToast(
                                requireActivity(), "Vui lòng nhập một biểu thức hợp lệ "
                            )

                        }

                        if (is_infinity) {
                            clearButton(view)
                            viewModel.setShowTvResult(false)
                            is_infinity = false
                        }

                    }
                }

            } else {
//                withContext(Dispatchers.Main) { binding.tvResult.text = "" }
            }
        }
    }

    fun containsSpecialElement(input: String): Boolean {

        val specialStrings = arrayOf(
            "log(",
            "ln(",
            "Φ",
            "sin(",
            "cos(",
            "tan(",
            "^",
            "sinh(",
            "cosh(",
            "tanh(",
            "sin⁻¹",
            "cos⁻¹",
            "tan⁻¹",
            "e",
            "π",
            "√",
            "%"
        )


        return specialStrings.any { input.contains(it) }
    }

    fun containsArithmeticOperators(input: String): Boolean {
        val arithmeticOperators = arrayOf('+', '-', '×', '÷', '(', ')')
        return arithmeticOperators.any { input.contains(it) }
    }

    private fun addHistory(result: String, calculation: String) {

        lifecycleScope.launch {
            var history = History(result, calculation + " = ")
            val newId = database.historyDao().insert(history)


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
                is_infinity = false
                require_real_number = false

                // importance
                val calculationTmp = Expression().getCleanExpression(
                    binding.tvInput.text.toString(), decimalSeparatorSymbol, groupingSeparatorSymbol
                )


                calculationResult =
                    Calculator(MyPreferences(requireActivity()).numberPrecision!!.toInt()).evaluate(
                        calculationTmp, isDegreeModeActivated
                    )

                // If result is a number and it is finite

                if (!(division_by_0 || domain_error || syntax_error || is_infinity || require_real_number)) {

                    // Round
                    calculationResult = roundResult(calculationResult)

                    var formattedResult = NumberFormatter.format(
                        calculationResult.toString().replace(".", decimalSeparatorSymbol),
                        decimalSeparatorSymbol,
                        groupingSeparatorSymbol
                    )

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
                                    ".", decimalSeparatorSymbol
                                ), decimalSeparatorSymbol, groupingSeparatorSymbol
                            )
                        }
                    }


                    withContext(Dispatchers.Main) {
                        if (formattedResult != calculation) {
                            Log.d("530452353255", viewModel.getShowTvResult().value.toString())
                            viewModel.setValueResult(formattedResult)
                        } else {
                            viewModel.setValueResult(binding.tvInput.text.toString())
                            viewModel.setValueResult(formattedResult)
                        }
                    }

                } else withContext(Dispatchers.Main) {

                    if (division_by_0) {
                        println("callaalalal ${binding.tvInput.toString()}")

                        if (binding.tvInput.text.toString().contains("÷0")) {
                            showCustomToast(requireActivity(), "Bạn không thể chia cho 0")
                            viewModel.setShowTvResult(false)
                        }


                    }
                    // close
//                    else {
//                        withContext(Dispatchers.Main) {
//
//                            viewModel.setValueResult("")
//                        }
//                    }
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
                && currentSymbol != nextChar && previousChar != "√" // No symbol can be added on an empty square root
                && previousChar != decimalSeparatorSymbol // Ensure that the previous character is not a comma
                && (previousChar != "(" // Ensure that we are not at the beginning of a parenthesis
                        || currentSymbol == "-")
            ) { // Minus symbol is an override
                // If previous character is a symbol, replace it
                if (previousChar.matches("[+\\-÷×^]".toRegex())) {

//                    keyVibration(view)

                    val leftString =
                        binding.tvInput.text.subSequence(0, cursorPosition - 1).toString()
                    val rightString =
                        binding.tvInput.text.subSequence(cursorPosition, textLength).toString()

                    // Add a parenthesis if there is another symbol before minus
                    if (currentSymbol == "-") {

                        if (previousChar in "+-") {
                            binding.tvInput.setText(leftString + currentSymbol + rightString)
                            binding.tvInput.setSelection(cursorPosition)
                        } else {
                            binding.tvInput.setText(leftString + previousChar + currentSymbol + rightString)
                            binding.tvInput.setSelection(cursorPosition + 1)
                        }
                    } else if (cursorPosition > 1 && binding.tvInput.text[cursorPosition - 2] != '(') {
                        binding.tvInput.setText(leftString + currentSymbol + rightString)
                        binding.tvInput.setSelection(cursorPosition)
                    } else if (currentSymbol == "+") {
                        print("dayla dau + fas");

                        binding.tvInput.setText(leftString + rightString)
                        binding.tvInput.setSelection(cursorPosition - 1)
                    }

                }
//                 If next character is a symbol, replace it
                else if (nextChar.matches("[+\\-÷×^%!]".toRegex()) && currentSymbol != "%") {
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
        if (allCharsEqualToZero || newResult.toString().startsWith("0E")) {
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

        if (!(textLength > cursorPosition && binding.tvInput.text.toString()[cursorPosition] in "×÷+-^") && (openParentheses == closeParentheses || binding.tvInput.text.toString()[cursorPosition - 1] == '(' || binding.tvInput.text.toString()[cursorPosition - 1] in "×÷+-^")) {
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
            val functionsList = listOf(
                "cos⁻¹(",
                "sin⁻¹(",
                "tan⁻¹(",
                "cos(",
                "sin(",
                "tan(",
                "ln(",
                "log(",
                "exp(",
                "sinh(",
                "tanh(",
                "cosh(",
                "|",
                "×|",
            )
            for (function in functionsList) {
                val leftPart = binding.tvInput.text.subSequence(0, cursorPosition).toString()
                if (leftPart.endsWith(function)) {
                    newValue = binding.tvInput.text.subSequence(0, cursorPosition - function.length)
                        .toString() + binding.tvInput.text.subSequence(cursorPosition, textLength)
                        .toString()
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
                    .toString() + binding.tvInput.text.subSequence(cursorPosition, textLength)
                    .toString()

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
                if (immediateRightDigits > 3) rightSideCommas = immediateRightDigits / 3
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

    private fun sinhButton(it: View?) {
        updateDisplay(it!!, "sinh(")

    }


    fun naturalLogarithmButton(view: View) {
        view as Button;
        if (view.text.equals("ln")) {
            updateDisplay(view, "ln(")
        } else {
            updateDisplay(view, "cosh(")
        }
    }

    fun logarithmButton(view: View) {

        view as Button;
        if (view.text.equals("log")) {
            updateDisplay(view, "log(")
        } else {
            updateDisplay(view, "sinh(")
        }
    }

    fun phiButton(view: View) {
        view as Button;
        if (view.text.toString().trim()
                .equals(requireActivity().resources.getString(R.string.phi))
        ) {
            updateDisplay(view, requireActivity().resources.getString(R.string.phi))
        } else {
            updateDisplay(view, "tanh(")
        }

    }

    fun clearButton(view: View) {
        binding.tvInput.setText("")
        viewModel.setValueResult("")
        viewModel.setValueCal("")

    }


    override fun onOptionSelected(option: String) {
        when (option) {
            "caculator" -> replaceFragment(CalculateFragment(), "Calculator")
            "unit" -> replaceFragment(UnitConverFragment(), "unit")
            "money" -> replaceFragment(CurrencyConverterFragment(), "currency")
        }
    }

    fun replaceFragment(fragment: Fragment, pageCurrent: String) {

        if (pageCurrent.equals("currency")) {
            nameCurrent = "money"
            binding.tvTitleCurrentPage.setText("Currency")
            binding.icCurrentMode.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_currency, null
                )
            )
        } else if (pageCurrent.equals("unit")) {
            nameCurrent = "unit"
            binding.tvTitleCurrentPage.setText("Unit")
            binding.icCurrentMode.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_unit, null
                )
            )
        } else if (pageCurrent.equals("Calculator")) {
            nameCurrent = "caculator"

            binding.tvTitleCurrentPage.setText("Calculator")
            binding.icCurrentMode.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_calculator, null)
            )
        }

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

}

