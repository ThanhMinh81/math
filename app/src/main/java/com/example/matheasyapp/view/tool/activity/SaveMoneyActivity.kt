package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.matheasyapp.spinner.CustomSpinner
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.AdapterSpinner
import com.example.matheasyapp.databinding.ActivitySaveMoneyBinding
import com.example.matheasyapp.view.tool.keyboard.KeyboardTaxMoney
import java.text.DecimalFormat

enum class TypeSave(val displayNameResId: Int) {
    FIXED_DEPOSIT(R.string.fixed_deposit), INSTALLMENT_SAVINGS(R.string.installment_savings)
}

enum class Interest(val displayNameResId: Int) {
    SIMPLE_INTEREST(R.string.simple_interest), MONTHLY_COMPOUND(R.string.monthly_compound), QUARTERLY_COMPOUND(
        R.string.quarterly_compound
    ),
    SEMIANNUAL_COMPOUND(R.string.semiannual_compound), ANNUAL_COMPOUND(R.string.annual_compound)
}


enum class LevelSave(val displayNameResId: Int) {
    MARGIN_LEVEL(R.string.margin_level), GOAL_LEVEL(R.string.goal_level)
}

// month , yearth
enum class BillSave(val displayNameResId: Int) {
    YEAR(R.string.year), MONTH(R.string.month)
}

enum class TermType {
    MONTH, YEAR
}


class SaveMoneyActivity : AppCompatActivity(), KeyboardTaxMoney.CallBackFunction,
    CustomSpinner.OnSpinnerEventsListener {

    lateinit var binding: ActivitySaveMoneyBinding;

    // type Saving
    var typeSaving: String = "fixed"

    // interest frequency
    var typeInterest: String = "simple"

    // target level
    var targetLevel: String = "margin"


    // tern saving
    var ternSaving: String = "YEAR"


    var listTypeSave: Array<String> = arrayOf()
    var listInterest: Array<String> = arrayOf()
    var listLevelSave: Array<String> = arrayOf()
    var listBillSave: Array<String> = arrayOf()

    lateinit var df: DecimalFormat

    private var stateButton: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        df = DecimalFormat("#,###")

        stateButton = true
        binding.layoutResult.visibility = View.GONE

        listTypeSave = TypeSave.values().map { getString(it.displayNameResId) }.toTypedArray()

        listInterest = Interest.values().map { getString(it.displayNameResId) }.toTypedArray()
        listLevelSave = LevelSave.values().map { getString(it.displayNameResId) }.toTypedArray()
        listBillSave = BillSave.values().map { getString(it.displayNameResId) }.toTypedArray()


        setEnableButtonResult(0)
        setEnableButtonCancle(0)

        initWidget()
        onClick()


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

            Log.d("450294572355234", typeInterest.toString() + " - " + targetLevel.toString())

            if (stateButton) {


                calculateBasedOnType(
                    typeSaving = typeSaving,
                    typeInterest = typeInterest,
                    typeTarget = targetLevel
                )

                if (binding.layoutResult.visibility == View.VISIBLE) {
                    setEnableInput(false)
                    stateButton = false
                    checkStateButton()
                }


            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {

                // clear all
                binding.edTarget.setText("")
                binding.edInterestRate.setText("")
                binding.edSaving.setText("")
                binding.edTaxRate.setText("")
                checkEnableButtonResult()
                setEnableInput(true)
            } else {
                setEnableInput(true)
                stateButton = true
                checkStateButton()
                binding.layoutResult.visibility = View.GONE
            }
        }

    }

    private fun initWidget() {

        // spinner
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypeSave)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTypeSave.adapter = AdapterSpinner(this, listTypeSave.toList() , false)
        binding.spTypeSave.setSpinnerEventsListener(this)


//        val adapterInterest = ArrayAdapter(this, android.R.layout.simple_spinner_item, listInterest)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFrequencyInterest.adapter = AdapterSpinner(this, listInterest.toList() ,false)
        binding.spFrequencyInterest.setSpinnerEventsListener(this)


//        val adapterTime = ArrayAdapter(this, android.R.layout.simple_spinner_item, listBillSave)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSavingTern.adapter = AdapterSpinner(this, listBillSave.toList() , true)
        binding.spSavingTern.setSpinnerEventsListener(this)

//        val adapterLevel = ArrayAdapter(this, android.R.layout.simple_spinner_item, listLevelSave)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTargetLevel.adapter = AdapterSpinner(this, listLevelSave.toList() , true)
        binding.spTargetLevel.setSpinnerEventsListener(this)

        binding.spTypeSave.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                // Lấy chuỗi đã chọn từ Spinner
                val selectedItem = parent.getItemAtPosition(position).toString()

                // Tìm enum tương ứng với chuỗi đã chọn
                val selectedTypeSave =
                    TypeSave.values().find { getString(it.displayNameResId) == selectedItem }

                selectedTypeSave?.let {
                    when (it) {
                        TypeSave.FIXED_DEPOSIT -> {

                            typeSaving = "fixed"
                        }

                        TypeSave.INSTALLMENT_SAVINGS -> {

                            typeSaving = "installment"

                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        binding.spFrequencyInterest.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    // Lấy chuỗi đã chọn từ Spinner
                    val selectedItem = parent.getItemAtPosition(position).toString()

                    // Tìm enum tương ứng với chuỗi đã chọn
                    val selectedTypeSave =
                        Interest.values().find { getString(it.displayNameResId) == selectedItem }



                    selectedTypeSave?.let {
                        when (it) {
                            Interest.SIMPLE_INTEREST -> {
                                typeInterest = "simple"
                            }

                            Interest.MONTHLY_COMPOUND -> {
                                typeInterest = "month"
                                Log.d("34524525252", typeInterest.toString())

                            }

                            Interest.QUARTERLY_COMPOUND -> {
                                typeInterest = "quarterly"
                                Log.d("34524525252", typeInterest.toString())
                            }

                            Interest.SEMIANNUAL_COMPOUND -> {
                                typeInterest = "semiannual"
                                Log.d("34524525252", typeInterest.toString())
                            }

                            Interest.ANNUAL_COMPOUND -> {
                                typeInterest = "annual"
                                Log.d("34524525252", typeInterest.toString())
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }


        binding.spTargetLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {

                val selectedItem = parent.getItemAtPosition(position).toString()

                val levelSave =
                    LevelSave.values().find { getString(it.displayNameResId) == selectedItem }

                levelSave?.let {
                    when (it) {
                        LevelSave.GOAL_LEVEL -> {
                            targetLevel = "target"
                            Log.d("53523523534", targetLevel)
                            binding.tvTitleLevel.setText(selectedItem.toString())

                        }

                        LevelSave.MARGIN_LEVEL -> {
                            targetLevel = "margin"
                            Log.d("53523523534", targetLevel)
                            binding.tvTitleLevel.setText(selectedItem.toString())

                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        binding.spSavingTern.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                // Lấy chuỗi đã chọn từ Spinner
                val selectedItem = parent.getItemAtPosition(position).toString()

                // Tìm enum tương ứng với chuỗi đã chọn
                val selectedTypeSave =
                    BillSave.values().find { getString(it.displayNameResId) == selectedItem }


                selectedTypeSave?.let {
                    when (it) {
                        BillSave.YEAR -> {
                            ternSaving = "YEAR"
                            Log.d("34520-30-5", ternSaving)
                        }

                        BillSave.MONTH -> {
                            ternSaving = "MONTH"
                            Log.d("34520-30-5", ternSaving)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        binding.edTarget.setOnClickListener {
            showKeyBoard("target")
        }

        binding.edInterestRate.setOnClickListener {
            showKeyBoard("interest")
        }
        binding.edSaving.setOnClickListener {
            showKeyBoard("saving")
        }
        binding.edTaxRate.setOnClickListener {
            showKeyBoard("tax")

        }

    }


    private fun showKeyBoard(key: String) {

        val keyBoard = KeyboardTaxMoney.newInstance(key)

        keyBoard.show(supportFragmentManager, "my_bottomsheft")

    }

    private fun setEnableInput(boolean: Boolean) {

        binding.edTarget.isEnabled = boolean
        binding.edSaving.isEnabled = boolean
        binding.edInterestRate.isEnabled = boolean
        binding.edTaxRate.isEnabled = boolean
        binding.spTypeSave.isEnabled = boolean
        binding.spFrequencyInterest.isEnabled = boolean
        binding.spTargetLevel.isEnabled = boolean
        binding.spSelectedMoney.isEnabled = boolean
        binding.spSavingTern.isEnabled = boolean

    }

    private fun checkStateButton() {
        if (stateButton) {
            binding.btnCancle.visibility = View.VISIBLE
            binding.btnEditData.visibility = View.GONE

            binding.btnResult.setText("See Result")
            binding.btnCancle.setText("Delete All")

        } else {
            binding.btnCancle.visibility = View.GONE
            binding.btnEditData.visibility = View.VISIBLE

            binding.btnResult.setText("Home")
            binding.btnCancle.setText("C")

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

    fun checkEnableButtonResult() {

        if ((binding.edTarget.text.toString().isNotEmpty() && binding.edSaving.text.toString()
                .isNotEmpty() && binding.edTaxRate.text.toString()
                .isNotEmpty() && binding.edInterestRate.text.toString().isNotEmpty())
        ) {

            setEnableButtonResult(2)

        } else {
            setEnableButtonResult(0)
        }

        if ((binding.edTarget.text.toString().length > 0 || binding.edSaving.text.toString().length > 0 || binding.edTaxRate.text.toString().length > 0 || binding.edInterestRate.text.toString().length > 0)) {
            setEnableButtonCancle(2)

        } else {
            setEnableButtonCancle(0)
        }
    }

    override fun onClickNumber(mode: String, value: String) {
        when (mode) {

            "target" -> {

                val currentText = binding.edTarget.text.toString() ?: ""
                val concatenatedText = currentText + value
                if (!concatenatedText.startsWith("0")) {
                    val valueClear = clearDotsAndCommas(concatenatedText)
                    if (isNumberLengthValid(concatenatedText)) {
                        val format: String = try {
                            df.format(valueClear.toBigDecimal())
                        } catch (e: NumberFormatException) {
                            valueClear
                        }
                        binding.edTarget.setText(format.toString())
                        checkEnableButtonResult()
                    }
                } else {
                    binding.edTarget.setText("0")
                    if (!value.equals("0")) {
                        binding.edTarget.setText(value)
                    }
                }

            }

            "interest" -> {
                val currentText = binding.edInterestRate.text.toString() ?: ""
                val concatenatedText = currentText + value
                if (!concatenatedText.startsWith("0")) {
                    if (concatenatedText.isNotEmpty()) {
                        val checkInput: Boolean = checkValueInput(concatenatedText)
                        if (checkInput) {
                            binding.edInterestRate.setText(concatenatedText.toString())
                            checkEnableButtonResult()
                        } else {
                            binding.edInterestRate.setText("100")
                            checkEnableButtonResult()
                        }
                    }
                } else {
                    binding.edInterestRate.setText("0")
                    if (!value.equals("0")) {
                        binding.edInterestRate.setText(value)
                    }
                }
            }

            "saving" -> {
                val currentText = binding.edSaving.text.toString() ?: ""
                val concatenatedText = currentText + value
                if (!concatenatedText.startsWith("0")) {
                    binding.edSaving.setText(concatenatedText.toString())
                    checkEnableButtonResult()
                } else {
                    binding.edSaving.setText("0")
                    if (!value.equals("0")) {
                        binding.edSaving.setText(value)
                    }
                }
            }

            "tax" -> {
                val currentText = binding.edTaxRate.text.toString() ?: ""
                val concatenatedText = currentText + value

                if (!concatenatedText.startsWith("0")) {
                    if (concatenatedText.isNotEmpty()) {
                        val checkInput: Boolean = checkValueInput(concatenatedText)
                        if (checkInput) {
                            binding.edTaxRate.setText(concatenatedText.toString())
                            checkEnableButtonResult()
                        } else {
                            binding.edTaxRate.setText("100")
                            checkEnableButtonResult()

                        }
                    }
                } else {
                    binding.edTaxRate.setText("0")
                    if (!value.equals("0")) {
                        binding.edTaxRate.setText(value)
                    }
                }

            }
        }
    }

    override fun onClearItem(mode: String) {
        when (mode) {

            "target" -> {

                // xóa phải kiểm tra xem là nó đã clear dot chưa
                // sau đó mới format lại được
                val currentText = binding.edTarget.text.toString() ?: ""

                val clearDot = clearDotsAndCommas(currentText)
                if (currentText.isNotEmpty()) {
                    var value = clearDot.substring(0, clearDot.length - 1)
                    val format: String = try {
                        df.format(value.toBigDecimal())
                    } catch (e: NumberFormatException) {
                        value
                    }
                    binding.edTarget.setText(format)
                    checkEnableButtonResult()
                } else {
                    binding.edTarget.setText("")
                    checkEnableButtonResult()

                }

            }

            "interest" -> {
                val currentText = binding.edInterestRate.text ?: ""
                if (currentText.isNotEmpty()) {
                    var value = currentText.substring(0, currentText.length - 1)
                    binding.edInterestRate.setText(value)
                    checkEnableButtonResult()
                } else {
                    binding.edInterestRate.setText("")
                    checkEnableButtonResult()

                }
            }

            "saving" -> {
                val currentText = binding.edSaving.text ?: ""
                if (currentText.isNotEmpty()) {
                    var value = currentText.substring(0, currentText.length - 1)
                    binding.edSaving.setText(value)
                    checkEnableButtonResult()

                } else {
                    binding.edSaving.setText("")
                    checkEnableButtonResult()

                }
            }

            "tax" -> {
                val currentText = binding.edTaxRate.text ?: ""
                if (currentText.isNotEmpty()) {
                    var value = currentText.substring(0, currentText.length - 1)
                    binding.edTaxRate.setText(value)
                    checkEnableButtonResult()

                } else {
                    binding.edTaxRate.setText("")
                    checkEnableButtonResult()

                }
            }
        }
    }

    override fun onClearAll(mode: String) {
        when (mode) {

            "target" -> {
                binding.edTarget.setText("")
                checkEnableButtonResult()

            }

            "interest" -> {
                binding.edInterestRate.setText("")
                checkEnableButtonResult()

            }

            "saving" -> {
                binding.edSaving.setText("")
                checkEnableButtonResult()

            }

            "tax" -> {
                binding.edTaxRate.setText("")
                checkEnableButtonResult()

            }
        }
    }


    fun calculateBasedOnType(typeSaving: String, typeInterest: String, typeTarget: String) {
        when (typeSaving) {
            "fixed" -> {
                when (typeInterest) {
                    "simple" -> {
                        when (typeTarget) {
                            "target" -> {

                                var targetValue =
                                    clearDotsAndCommas(binding.edTarget.text.toString())

                                var value: Pair<Double, Double> = calculateSavingsWithTargetLevel(
                                    targetLevel = targetValue.toDouble(),
                                    interestRate = binding.edInterestRate.text.toString().toInt(),
                                    term = binding.edSaving.text.toString().toInt(),
                                    termType = ternSaving,
                                    interestIncomeTaxRate = binding.edTaxRate.text.toString()
                                        .toInt()
                                )

//                                Log.d("7657457400", value.first.toString() + "  -  " + value.second.toString())

                                binding.layoutResult.visibility = View.VISIBLE
                                var valueLastMoney: String =
                                    clearDotsAndCommas(value.first.toString())
                                var valueTax: String = clearDotsAndCommas(value.second.toString())
                                binding.tvResultLastMoney.setText(
                                    df.format(valueLastMoney.toBigDecimal()).toString()
                                )
                                binding.tvResultPerson.setText(
                                    df.format(valueTax.toBigDecimal()).toString()
                                )

                            }

                            "margin" -> {
                                var targetValue =
                                    clearDotsAndCommas(binding.edTarget.text.toString())

                                var value: Pair<Double, Double> = calculateSavingsWithMarginLevel(
                                    marginLevel = targetValue.toDouble(),
                                    interestRate = binding.edInterestRate.text.toString().toInt(),
                                    term = binding.edSaving.text.toString().toInt(),
                                    termType = ternSaving,
                                    interestIncomeTaxRate = binding.edTaxRate.text.toString()
                                        .toInt()
                                )

                                Log.d(
                                    "7657457400",
                                    value.first.toString() + "  -  " + value.second.toString()
                                )
                                binding.layoutResult.visibility = View.VISIBLE
                                var valueLastMoney: String =
                                    clearDotsAndCommas(value.first.toString())
                                var valueTax: String = clearDotsAndCommas(value.second.toString())
                                binding.tvResultLastMoney.setText(
                                    df.format(valueLastMoney.toBigDecimal()).toString()
                                )
                                binding.tvResultPerson.setText(
                                    df.format(valueTax.toBigDecimal()).toString()
                                )

                            }
                        }
                    }

                    "month" -> {
                        when (typeTarget) {
                            "margin" -> {

                                var targetValue =
                                    clearDotsAndCommas(binding.edTarget.text.toString())

                                var value: Pair<String, String> =
                                    calculateSavingsWithMonthlyGrossProfit(
                                        targetLevel = targetValue.toDouble(),
                                        interestRate = binding.edInterestRate.text.toString()
                                            .toInt(),
                                        term = binding.edSaving.text.toString().toInt(),
                                        termType = ternSaving,
                                        interestIncomeTaxRate = binding.edTaxRate.text.toString()
                                            .toInt()
                                    )

                                binding.layoutResult.visibility = View.VISIBLE
                                var valueLastMoney: String =
                                    clearDotsAndCommas(value.first.toString())
                                var valueTax: String = clearDotsAndCommas(value.second.toString())
                                binding.tvResultLastMoney.setText(
                                    df.format(valueLastMoney.toBigDecimal()).toString()
                                )
                                binding.tvResultPerson.setText(
                                    df.format(valueTax.toBigDecimal()).toString()
                                )

                            }
                        }
                    }

                    "quarterly" -> {
                        when (typeTarget) {
                            "margin" -> {
                                var targetValue =
                                    clearDotsAndCommas(binding.edTarget.text.toString())

                                var value: Pair<Double, Double> =
                                    calculateSavingsWithQuarterlyGrossProfit(
                                        targetLevel = targetValue.toDouble(),
                                        interestRate = binding.edInterestRate.text.toString()
                                            .toInt(),
                                        term = binding.edSaving.text.toString().toInt(),
                                        termType = ternSaving,
                                        interestIncomeTaxRate = binding.edTaxRate.text.toString()
                                            .toInt()
                                    )

                                binding.layoutResult.visibility = View.VISIBLE
                                var valueLastMoney: String =
                                    clearDotsAndCommas(value.first.toString())
                                var valueTax: String = clearDotsAndCommas(value.second.toString())
                                binding.tvResultLastMoney.setText(
                                    df.format(valueLastMoney.toBigDecimal()).toString()
                                )
                                binding.tvResultPerson.setText(
                                    df.format(valueTax.toBigDecimal()).toString()
                                )
                            }
                        }
                    }

                    "semiannual" -> {
                        when (typeTarget) {
                            "margin" -> {
                                var targetValue =
                                    clearDotsAndCommas(binding.edTarget.text.toString())

                                var value: Pair<String, String> =
                                    calculateSavingsWithHalfYearGrossProfit(
                                        targetValue.toDouble(),
                                        interestRate = binding.edInterestRate.text.toString()
                                            .toInt(),
                                        term = binding.edSaving.text.toString().toInt(),
                                        termType = ternSaving,
                                        interestIncomeTaxRate = binding.edTaxRate.text.toString()
                                            .toInt()
                                    )

                                binding.layoutResult.visibility = View.VISIBLE
                                var valueLastMoney: String =
                                    clearDotsAndCommas(value.first.toString())
                                var valueTax: String = clearDotsAndCommas(value.second.toString())
                                binding.tvResultLastMoney.setText(
                                    df.format(valueLastMoney.toBigDecimal()).toString()
                                )
                                binding.tvResultPerson.setText(
                                    df.format(valueTax.toBigDecimal()).toString()
                                )
                            }
                        }
                    }

                    "annual" -> {
                        when (typeTarget) {
                            "margin" -> {
                                var targetValue =
                                    clearDotsAndCommas(binding.edTarget.text.toString())

                                var value: Pair<String, String> =
                                    calculateSavingsWithAnnualGrossProfit(
                                        targetValue.toDouble(),
                                        interestRate = binding.edInterestRate.text.toString()
                                            .toInt(),
                                        term = binding.edSaving.text.toString().toInt(),
                                        termType = ternSaving,
                                        interestIncomeTaxRate = binding.edTaxRate.text.toString()
                                            .toInt()
                                    )

                                binding.layoutResult.visibility = View.VISIBLE
                                var valueLastMoney: String =
                                    clearDotsAndCommas(value.first.toString())
                                var valueTax: String = clearDotsAndCommas(value.second.toString())
                                binding.tvResultLastMoney.setText(
                                    df.format(valueLastMoney.toBigDecimal()).toString()
                                )
                                binding.tvResultPerson.setText(
                                    df.format(valueTax.toBigDecimal()).toString()
                                )
                            }
                        }
                    }
                }
            }

            "installment" -> {
                when (typeInterest) {
                    "simple" -> {}
                    "month" -> {}
                    "quarterly" -> {}
                    "semiannual" -> {}
                    "annual" -> {}
                }
            }
        }
    }

    fun checkValueInput(value: String): Boolean {
        if (value.isNotEmpty()) {
            if (value.toDouble() > 100) {
                return false
            } else {
                return true;
            }
        }
        return false
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

    // fixed

    fun calculateSavingsWithTargetLevel(
        targetLevel: Double,           // Số tiền gốc (Target Level)
        interestRate: Int,             // Lãi suất (phần trăm)
        term: Int,                     // Thời hạn gửi
        termType: String,              // Đơn vị thời gian (Month hoặc Year)
        interestIncomeTaxRate: Int     // Thuế suất thu nhập từ lãi suất (phần trăm)
    ): Pair<Double, Double> {

        // Log thông tin đầu vào
        Log.d("Input Values", "$targetLevel $interestRate $term $termType $interestIncomeTaxRate")

        // Chuyển đổi thời gian thành năm nếu đơn vị là tháng
        val years =
            if (termType.equals("MONTH", ignoreCase = true)) term / 12.0 else term.toDouble()

        Log.d("Years", years.toString())

        // Tính lãi suất đơn giản
        val simpleInterest = targetLevel * (interestRate / 100.0) * years

        Log.d("Simple Interest", simpleInterest.toString())

        // Tính thuế thu nhập từ lãi
        val incomeTax = simpleInterest * (interestIncomeTaxRate / 100.0)

        Log.d("Income Tax", incomeTax.toString())

        // Tính số dư tiết kiệm cuối cùng
        val finalSavingsBalance = targetLevel + simpleInterest - incomeTax

        Log.d("Final Savings Balance", finalSavingsBalance.toString())

        // Trả về số dư tiết kiệm cuối cùng và thuế thu nhập từ lãi
        return Pair(finalSavingsBalance, incomeTax)
    }

    // fixed

    fun calculateSavingsWithMarginLevel(
        marginLevel: Double,           // Số tiền gốc (Margin Level)
        interestRate: Int,          // Lãi suất
        term: Int,                     // Thời hạn gửi
        termType: String,            // Đơn vị thời gian (Month hoặc Year)
        interestIncomeTaxRate: Int  // Thuế suất thu nhập từ lãi suất
    ): Pair<Double, Double> {
        // Chuyển đổi thời gian thành năm nếu đơn vị là tháng
        val years = if (termType.equals(TermType.MONTH)) term / 12.0 else term.toDouble()

        // Tính lãi suất đơn giản
        val simpleInterest = marginLevel * (interestRate / 100.0) * years

        // Tính thuế thu nhập từ lãi
        val incomeTax = simpleInterest * (interestIncomeTaxRate / 100.0)

        // Tính số dư tiết kiệm cuối cùng
        val finalSavingsBalance = marginLevel + simpleInterest - incomeTax

        // Trả về thuế suất thu nhập từ lãi và số dư tiết kiệm cuối cùng
        return Pair(finalSavingsBalance, incomeTax)
    }


    // fixed , month , target level

    fun calculateSavingsWithMonthlyGrossProfit(
        targetLevel: Double,           // Số tiền gốc (Target Level)
        interestRate: Int,             // Lãi suất (phần trăm)
        term: Int,                     // Thời hạn gửi
        termType: String,              // Đơn vị thời gian (Month hoặc Year)
        interestIncomeTaxRate: Int     // Thuế suất thu nhập từ lãi suất (phần trăm)
    ): Pair<String, String> {

        // Chuyển đổi thời gian thành tháng
        val months = if (termType.equals("YEAR", ignoreCase = true)) term * 12 else term

        // Tính lãi suất hàng tháng
        val monthlyInterest = (targetLevel * interestRate) / (12 * 100)

        // Tính tổng lãi suất
        val totalInterest = monthlyInterest * months

        // Tính thuế thu nhập từ lãi
        val incomeTax = totalInterest * (interestIncomeTaxRate / 100.0)

        // Tính số dư tiết kiệm cuối cùng
        val finalSavingsBalance = targetLevel + totalInterest - incomeTax

        // Định dạng kết quả để chỉ hiển thị hai chữ số thập phân
        val formattedFinalSavingsBalance = String.format("%.2f", finalSavingsBalance)
        val formattedIncomeTax = String.format("%.2f", incomeTax)

        // Trả về số dư tiết kiệm cuối cùng và thuế thu nhập từ lãi dưới dạng chuỗi đã định dạng
        return Pair(formattedFinalSavingsBalance, formattedIncomeTax)
    }

    // fixed , quaterly , target level

    fun calculateSavingsWithQuarterlyGrossProfit(
        targetLevel: Double,            // Số tiền gốc (Target Level)
        interestRate: Int,              // Lãi suất (%)
        term: Int,                      // Thời hạn gửi
        termType: String,               // Đơn vị thời gian (Month hoặc Year)
        interestIncomeTaxRate: Int      // Thuế suất thu nhập từ lãi suất (%)
    ): Pair<Double, Double> {

        // Xác định số lượng quý trong kỳ hạn gửi
        val numberOfQuarters: Double =
            if (termType.equals("YEAR", ignoreCase = true)) term * 4.0 else term / 3.0

        // Tính lãi suất hàng quý
        val quarterlyInterestRate = interestRate / 4.0 / 100.0

        // Tính tổng lãi suất
        val totalInterest: Double = targetLevel * quarterlyInterestRate * numberOfQuarters

        // Tính thuế thu nhập từ lãi
        val incomeTax = totalInterest * (interestIncomeTaxRate / 100.0)

        // Tính số dư tiết kiệm cuối cùng
        val finalSavingsBalance = targetLevel + totalInterest - incomeTax

        // Trả về số dư tiết kiệm cuối cùng và thuế thu nhập từ lãi
        return Pair(finalSavingsBalance, incomeTax)
    }


    // fixed , half-year , target level

    fun calculateSavingsWithHalfYearGrossProfit(
        marginLevel: Double,           // Số tiền gốc (Margin Level)
        interestRate: Int,             // Lãi suất
        term: Int,                     // Thời hạn gửi
        termType: String,              // Đơn vị thời gian (Month hoặc Year)
        interestIncomeTaxRate: Int     // Thuế suất thu nhập từ lãi suất
    ): Pair<String, String> {   // Trả về kết quả là String với số chữ số cố định sau dấu thập phân

        val numberOfHalfYears =
            if (termType.equals("YEAR", ignoreCase = true)) term * 2.0 else term / 6.0
        val halfYearInterestRate = (interestRate / 2.0) / 100.0
        val totalInterest = marginLevel * halfYearInterestRate * numberOfHalfYears
        val incomeTax = totalInterest * (interestIncomeTaxRate / 100.0)
        val finalSavingsBalance = marginLevel + totalInterest - incomeTax

        // Định dạng kết quả với 5 chữ số sau dấu thập phân
        val decimalFormat = DecimalFormat("#.#####")
        val formattedFinalSavingsBalance = decimalFormat.format(finalSavingsBalance)
        val formattedIncomeTax = decimalFormat.format(incomeTax)

        return Pair(formattedFinalSavingsBalance, formattedIncomeTax)
    }


    // fixed , annual gross , target
    fun calculateSavingsWithAnnualGrossProfit(
        marginLevel: Double,           // Số tiền gốc (Margin Level)
        interestRate: Int,             // Lãi suất
        term: Int,                     // Thời hạn gửi
        termType: String,              // Đơn vị thời gian (Month hoặc Year)
        interestIncomeTaxRate: Int     // Thuế suất thu nhập từ lãi suất
    ): Pair<String, String> {   // Trả về kết quả là String với số chữ số cố định sau dấu thập phân

        // Chuyển đổi thời gian thành năm nếu đơn vị là tháng
        val years: Double =
            if (termType.equals("YEAR", ignoreCase = true)) term.toDouble() else term / 12.0
        val annualInterestRate = interestRate / 100.0
        val totalInterest = marginLevel * annualInterestRate * years
        val incomeTax = totalInterest * (interestIncomeTaxRate / 100.0)
        val finalSavingsBalance = marginLevel + totalInterest - incomeTax


        return Pair(finalSavingsBalance.toString(), incomeTax.toString())
    }

    override fun onPopupWindowOpened(spinner: Spinner?) {
        when (spinner?.id) {
            R.id.sp_type_save -> {
                binding.spTypeSave.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }

            R.id.sp_frequency_interest -> {
                binding.spFrequencyInterest.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }

            R.id.sp_target_level -> {
                binding.spTargetLevel.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }

            R.id.sp_saving_tern -> {
                binding.spSavingTern.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }
        }
    }

    override fun onPopupWindowClosed(spinner: Spinner?) {
        when (spinner?.id) {
            R.id.sp_type_save -> {
                binding.spTypeSave.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }

            R.id.sp_frequency_interest -> {
                binding.spFrequencyInterest.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }

            R.id.sp_target_level -> {
                binding.spTargetLevel.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }

            R.id.sp_saving_tern -> {
                binding.spSavingTern.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }
        }
    }

}
