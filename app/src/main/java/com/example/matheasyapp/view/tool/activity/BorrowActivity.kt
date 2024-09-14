package com.example.matheasyapp.view.tool.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.matheasyapp.spinner.CustomSpinner
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.AdapterSpinner
import com.example.matheasyapp.databinding.ActivityBorrowBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.view.history.HistoryPaymentActivity
import com.example.matheasyapp.view.tool.keyboard.KeyboardBorrow
import com.example.matheasyapp.view.tool.model.LoanHistory
import com.example.matheasyapp.view.tool.model.LoanPayment
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class BorrowActivity : AppCompatActivity(), KeyboardBorrow.CallBackFunction,
    CustomSpinner.OnSpinnerEventsListener {

//    val spinnerItems = arrayOf("Tổng số tiền thanh toán bằng nhau", "Thanh toán đầy đủ")

//    val spinnerBorrowTime = arrayOf("Năm", "Tháng")

    //    val spinnerItems = resources.getStringArray(R.array.spinner_items)
    var spinnerItems = emptyList<String>()
    var spinnerBorrowTime = emptyList<String>()
//    resources.getStringArray(R.array.spinner_borrow_time)


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBorrowBinding

    lateinit var df: DecimalFormat
    var fullSelectedTax: Boolean = true

    private var stateButton: Boolean = true

    private var repaymentMethod: String = "equals";
    private var tenorSP: String = "Years"
    private var interestSP: String = "Years"

    lateinit var listResult: ArrayList<LoanPayment>;

    private lateinit var database: HistoryDatabase

    private var loanHistoryUpdate: LoanHistory? = null
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                loanHistoryUpdate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Sử dụng getParcelableExtra với loại kiểu trên các phiên bản Android mới
                    data.getParcelableExtra("object", LoanHistory::class.java)
                } else {
                    // Sử dụng getParcelableExtra không có loại kiểu cho các phiên bản Android cũ hơn
                    data.getParcelableExtra("object")
                }

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBorrowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listResult = ArrayList<LoanPayment>()
        spinnerItems = resources.getStringArray(R.array.spinner_items).toList()
        spinnerBorrowTime = resources.getStringArray(R.array.spinner_borrow_time).toList()

        database = HistoryDatabase.getDatabase(this)

        stateButton = true
        df = DecimalFormat("#,###")



        setEnableButtonResult(0)
        setEnableButtonCancle(0)
        setUpView()

        // Đặt OnItemSelectedListener cho Spinner
        binding.spMethodPayment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedOption = spinnerItems[position]

                    if (position == 0) {
                        repaymentMethod = "equals"
                    } else if (position == 1) {
                        repaymentMethod = "total"
                    } else if (position == 2) {
                        repaymentMethod = "full"
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }



        binding.btnCancle.setOnClickListener {
            if (stateButton) {

                // clear all
                binding.tvInterest.setText("")
                binding.tvPrincipalamount.setText("")
                binding.tvBarrowTime.setText("")
                binding.tvPayInterest.setText("")

                checkEnableButtonResult()
                setEnableInput(true)

            } else {
                setEnableInput(true)
                // update data
                stateButton = true

            }

        }

        binding.btnResult.setOnClickListener {

            Log.d("45453452352", repaymentMethod.toString())

            if (repaymentMethod.equals("equals")) {
//                // thời hạn cho vay tính bằng tháng or năm
                val termType: String =
                    if (tenorSP.equals("Years")) "years" else "months"

                // thời hạn trả lãi tính bằng tháng or năm
                val intervalType: String =
                    if (interestSP.equals("Years")) "years" else "months"

                var valuePay: String = if (binding.tvBarrowTime.text.toString().toInt() ==
                    binding.tvPayInterest.text.toString().toInt()
                ) (binding.tvPayInterest.text.toString()
                    .toInt() - 1).toString() else binding.tvPayInterest.text.toString()

                val paymentSchedule = calculateLoanSumEquals(
                    clearDotsAndCommas(binding.tvPrincipalamount.text.toString()).toDouble(),
                    binding.tvInterest.text.toString().toDouble(),
                    binding.tvBarrowTime.text.toString().toInt(),
                    valuePay.toInt(),
                    termType,
                    intervalType
                )


                listResult.add(LoanPayment("N", "Vốn gốc", "Lãi suất", "Thanh toán", " Số dư "))

                listResult.addAll(paymentSchedule)

                val loanPayment: LoanHistory = addHistory()

                val intent = Intent(this, ResultActivity::class.java)
                intent.putParcelableArrayListExtra("listResult", listResult)
                intent.putExtra("object", loanPayment)

                activityResultLauncher.launch(intent);

                listResult.clear()
//
            } else if (repaymentMethod.equals("full")) {
//                 thời hạn cho vay tính bằng tháng or năm
                val termType: String =
                    if (tenorSP.toString().trim().equals("Years")) "years" else "months"

                // thời hạn trả lãi tính bằng tháng or năm
                val intervalType: String =
                    if (interestSP.toString().trim().equals("Years")) "years" else "months"


                val paymentSchedule = calculateLoanPaymentsFull(
                    clearDotsAndCommas(binding.tvPrincipalamount.text.toString()).toDouble(),
                    binding.tvInterest.text.toString().toDouble(),
                    binding.tvBarrowTime.text.toString().toInt(),
                    termType,
                    intervalType
                )

                listResult.add(LoanPayment("N", "Vốn gốc", "Lãi suất", "Thanh toán", " Số dư "))

                listResult.addAll(paymentSchedule)

                val loanPayment: LoanHistory = addHistory()

                val intent = Intent(this, ResultActivity::class.java)
                intent.putParcelableArrayListExtra("listResult", listResult)
                intent.putExtra("object", loanPayment)


                activityResultLauncher.launch(intent);

                listResult.clear()

            } else if (repaymentMethod.equals("total")) {

//                 thời hạn cho vay tính bằng tháng or năm
                val termType: String =
                    if (tenorSP.toString().trim().equals("Years")) "years" else "months"

                // thời hạn trả lãi tính bằng tháng or năm
                val intervalType: String =
                    if (interestSP.toString().trim().equals("Years")) "years" else "months"

                val paymentSchedule = totalAmountPaid(
                    clearDotsAndCommas(binding.tvPrincipalamount.text.toString()).toDouble(),
                    binding.tvInterest.text.toString().toDouble(),
                    binding.tvBarrowTime.text.toString().toInt(),
                    termType,
                    intervalType
                )

                listResult.add(LoanPayment("N", "Vốn gốc", "Lãi suất", "Thanh toán", " Số dư "))

                listResult.addAll(paymentSchedule)

                val loanPayment: LoanHistory = addHistory()

                val intent = Intent(this, ResultActivity::class.java)
                intent.putParcelableArrayListExtra("listResult", listResult)
                intent.putExtra("object", loanPayment)


                activityResultLauncher.launch(intent);

                listResult.clear()
            }


        }


        binding.icHistory.setOnClickListener {

            Log.d("45324523452", "%34523252")

            val intent = Intent(this, HistoryPaymentActivity::class.java)

            startActivity(intent)

        }


        onClick()

    }

    private fun checkTimeBorrow() {
        if (tenorSP.equals("Year")) {
            if (binding.tvBarrowTime.text.toString().toDouble() > 60) {
                binding.tvBarrowTime.setText("60")
            }
        } else if (tenorSP.equals("Month")) {
            if (binding.tvBarrowTime.text.toString().toDouble() > 600) {
                binding.tvBarrowTime.setText("600")
            }
        }
        if (binding.tvPayInterest.text.isNotEmpty() && binding.tvBarrowTime.text.isNotEmpty()) {
            if (binding.tvBarrowTime.text.toString()
                    .toDouble() < binding.tvPayInterest.text.toString().toDouble()
            ) {
                binding.tvPayInterest.setText("")
            }
        }
    }

    fun maxValueTax() {
        if (binding.tvInterest.text.toString().toDouble() >= 100) {
            Toast.makeText(this, "Max value is 100", Toast.LENGTH_SHORT).show()
            binding.tvInterest.setText("100")
        }


    }

    private fun setUpView() {


        // Đặt giá trị mặc định cho Spinner (ví dụ chọn "Option 2")
//        val defaultSelectionIndex = spinnerItems.indexOf("Tổng số tiền thanh toán bằng nhau")
//        if (defaultSelectionIndex != -1) {
//            binding.spMethodPayment.setSelection(defaultSelectionIndex)
//            repaymentMethod = "equals"
//        }

        binding.spinnerBorrowTime.adapter = AdapterSpinner(this, spinnerBorrowTime.toList(), true)

        binding.spinnerBorrowTime.setSpinnerEventsListener(this)


        binding.spinnerPayInterest.adapter = AdapterSpinner(this, spinnerBorrowTime.toList(), true)
        binding.spinnerPayInterest.setSpinnerEventsListener(this)


        binding.spMethodPayment.adapter = AdapterSpinner(this, spinnerItems.toList(), false)
        binding.spMethodPayment.setSpinnerEventsListener(this)


        // Thiết lập OnItemSelectedListener
        binding.spinnerBorrowTime.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position)
                    tenorSP = selectedItem.toString().trim()
                    if (position == 0) {
                        // year
                        fullSelectedTax = true

                    } else {
                        // month
                        fullSelectedTax = false
                        binding.spinnerPayInterest.setSelection(1)
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        binding.spinnerPayInterest.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (fullSelectedTax) {
                        val selectedItem = parent?.getItemAtPosition(position).toString()
                        interestSP = selectedItem.toString().trim()
                    } else {
                        val selectedItem = parent?.getItemAtPosition(1).toString()
                        interestSP = selectedItem
                        binding.spinnerPayInterest.setSelection(1)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }




        if (loanHistoryUpdate != null) {

            val indexPayment =
                spinnerItems.indexOfFirst { it -> it.equals(loanHistoryUpdate!!.paymentMethods.toString()) }
            binding.spMethodPayment.setSelection(indexPayment)

            binding.tvPrincipalamount.setText(loanHistoryUpdate!!.borrowPrincipal)
            binding.tvInterest.setText(loanHistoryUpdate!!.interest)
            binding.tvBarrowTime.setText(loanHistoryUpdate!!.borrow)

            val indexBorrowTime =
                spinnerBorrowTime.indexOfFirst { it -> it.equals(loanHistoryUpdate!!.borrowTime) }
            binding.spinnerBorrowTime.setSelection(indexBorrowTime)

            binding.tvPayInterest.setText(loanHistoryUpdate!!.interestTime)

            val indexInterestTime =
                spinnerBorrowTime.indexOfFirst { it -> it.equals(loanHistoryUpdate!!.interestTime) }
            binding.spinnerPayInterest.setSelection(indexInterestTime)


        }


    }


    private fun showKeyBoard(key: String) {

        val keyBoard = KeyboardBorrow.newInstance(key)

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    fun calculateLoanSumEquals(
        principal: Double,
        annualInterestRate: Double,
        loanTerm: Int,
        paymentInterval: Int,
        termType: String, // "months" or "years"
        intervalType: String // "months" or "years"
    ): ArrayList<LoanPayment> {
        val loanTermMonths = if (termType == "years") loanTerm * 12 else loanTerm
        val paymentIntervalMonths =
            if (intervalType == "years") paymentInterval * 12 else paymentInterval
        val monthlyInterestRate = annualInterestRate / 12 / 100
        var remainingPrincipal = principal

        val payments = ArrayList<LoanPayment>()
        val df = DecimalFormat("##,###.##", DecimalFormatSymbols(Locale.UK))


        var param1: Double = 0.0
        var param2: Double = 0.0
        var param3: Double = 0.0

        for (month in 1..loanTermMonths) {
            val interestPayment = remainingPrincipal * monthlyInterestRate
            val principalPayment = if (month > paymentIntervalMonths) {
                (principal / (loanTermMonths - paymentIntervalMonths))
            } else {
                0.0
            }
            val totalPayment = interestPayment + principalPayment
            remainingPrincipal -= principalPayment

            param1 += principalPayment
            param2 += interestPayment
            param3 += totalPayment


            payments.add(
                LoanPayment(

                    month.toString(),
                    df.format(principalPayment).toString(),
                    df.format(interestPayment).toString(),
                    df.format(totalPayment).toString(),
                    df.format(remainingPrincipal).toString(),

                    )
            )
        }



        payments.add(
            LoanPayment(
                ".toString()",
                df.format(param1).toString(),
                df.format(param2).toString(),
                df.format(param3).toString(),
                "0",
            )
        )

        return payments
    }


    private fun addHistory(): LoanHistory {

        val loanPayment: LoanPayment = listResult.last()

        val loanHistory: LoanHistory = LoanHistory(
            0,
            binding.spMethodPayment.selectedItem.toString(),
            binding.tvPrincipalamount.text.toString(),
            loanPayment.interest,
            binding.tvBarrowTime.text.toString(),
            binding.spinnerBorrowTime.selectedItem.toString(),
            binding.tvPayInterest.text.toString(),
            binding.spinnerPayInterest.selectedItem.toString(),
            loanPayment.payment,
            binding.tvInterest.text.toString()
        )

        val newID = database.loanHistoryDao().insert(loanHistory)


        return loanHistory


    }


    private fun onClick() {
        binding.tvPrincipalamount.setOnClickListener {
            showKeyBoard("principal_amount")
        }
        binding.tvInterest.setOnClickListener {
            showKeyBoard("interest")

        }
        binding.tvBarrowTime.setOnClickListener {
            showKeyBoard("barrowtime")
        }
        binding.tvPayInterest.setOnClickListener {
            showKeyBoard("payinterest")
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
        }


    }

    fun calculateEqualTotalPayment(
        principalLoan: Double,
        annualInterestRate: Double,
        tenor: Int,
        interestPaymentTerm: Int,
        isTenorInMonths: Boolean,
        isInterestPaymentTermInMonths: Boolean
    ): List<PaymentDetail> {
        val effectiveTenor = if (isTenorInMonths) tenor else tenor * 12
        val monthlyInterestRate = annualInterestRate / 12 / 100
        val monthlyPayment = principalLoan * (monthlyInterestRate * Math.pow(
            1 + monthlyInterestRate,
            effectiveTenor.toDouble()
        )) /
                (Math.pow(1 + monthlyInterestRate, effectiveTenor.toDouble()) - 1)

        val paymentDetails = mutableListOf<PaymentDetail>()
        var remainingPrincipal = principalLoan

        for (month in 1..effectiveTenor) {
            val interest = remainingPrincipal * monthlyInterestRate
            val principalPayment = monthlyPayment - interest
            val originalCapital = principalLoan - remainingPrincipal
            remainingPrincipal -= principalPayment

            paymentDetails.add(
                PaymentDetail(
                    month = month,
                    originalCapital = originalCapital,
                    interestRate = interest,
                    payments = monthlyPayment,
                    surplus = remainingPrincipal
                )
            )
        }

        return paymentDetails
    }

    fun totalAmountPaid(
        principal: Double,
        annualInterestRate: Double,
        term: Int,
        termType: String, // "months" or "years"
        intervalType: String // "months" or "years"
    ): ArrayList<LoanPayment> {
        val termMonths = if (termType == "years") term * 12 else term
        val intervalMonths = if (intervalType == "years") 12 else 1
        val monthlyInterestRate = annualInterestRate / 12 / 100
        val totalMonthlyPayment = principal * monthlyInterestRate /
                (1 - Math.pow(1 + monthlyInterestRate, -termMonths.toDouble()))

        var remainingPrincipal = principal
        val loanPayments = ArrayList<LoanPayment>()

        var totalPrincipal: Double = 0.0
        var totalInterest: Double = 0.0
        var totalPayment: Double = 0.0

        val df = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))

        for (month in 1..termMonths) {
            val interestPayment = remainingPrincipal * monthlyInterestRate
            val principalPayment = totalMonthlyPayment - interestPayment
            val newBalance = remainingPrincipal - principalPayment

            totalPrincipal += principalPayment
            totalInterest += interestPayment
            totalPayment += totalMonthlyPayment

            val loanPayment = LoanPayment(
                month = "$month",
                principal = df.format(principalPayment).toString(),
                interest = df.format(interestPayment).toString(),
                payment = df.format(totalMonthlyPayment).toString(),
                balance = df.format(newBalance).toString(),
            )

            loanPayments.add(loanPayment)

            remainingPrincipal = newBalance
        }

        loanPayments.add(
            LoanPayment(
                month = "Total",
                principal = df.format(totalPrincipal).toString(),
                interest = df.format(totalInterest).toString(),
                payment = df.format(totalPayment).toString(),
                balance = "0.00",
            )
        )

        return loanPayments
    }

    fun calculateLoanPaymentsFull(
        principal: Double,
        annualInterestRate: Double,
        term: Int,
        termType: String, // "months" or "years"
        intervalType: String // "months" or "years"
    ): ArrayList<LoanPayment> {
        val termMonths = if (termType == "years") term * 12 else term
        val intervalMonths = if (intervalType == "years") 12 else 1
        val monthlyInterestRate = annualInterestRate / 12 / 100
        val monthlyInterestPayment = principal * monthlyInterestRate

        var remainingPrincipal = principal
        val loanPayments = ArrayList<LoanPayment>()

        var totalPrincipal: Double = 0.0
        var totalInterest: Double = 0.0
        var totalPayment: Double = 0.0

        val df = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))

        for (month in 1..termMonths) {
            val interestPayment = monthlyInterestPayment
            val principalPayment = if (month % intervalMonths == 0) {
                if (month == termMonths) remainingPrincipal else 0.0
            } else {
                0.0
            }
            val totalMonthlyPayment = interestPayment + principalPayment

            if (month == termMonths) {
                remainingPrincipal = 0.0
            }

            totalPrincipal += principalPayment
            totalInterest += interestPayment
            totalPayment += totalMonthlyPayment

            val loanPayment = LoanPayment(
                month = "$month",
                principal = df.format(principalPayment).toString(),
                interest = df.format(interestPayment).toString(),
                payment = df.format(totalMonthlyPayment).toString(),
                balance = df.format(remainingPrincipal).toString(),
            )

            loanPayments.add(loanPayment)

            if (month < termMonths) {
                remainingPrincipal = principal
            }
        }

        loanPayments.add(
            LoanPayment(

                month = "sys",
                principal = df.format(totalPrincipal).toString(),
                interest = df.format(totalInterest).toString(),
                payment = df.format(totalPayment).toString(),
                "0",

                )
        )

        return loanPayments
    }

    override fun onClickNumber(mode: String, value: String) {
        when (mode) {
            "principal_amount" -> {
                val currentText = binding.tvPrincipalamount.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
//                    Log.d("5253523523532",concatenatedText.toString())
                    binding.tvPrincipalamount.setText(
                        df.format(concatenatedText.toBigDecimal()).toString()
                    )
                    checkEnableButtonResult()
                }
            }

            // %

            "interest" -> {
                val currentText = binding.tvInterest.text.toString() ?: ""
                val valueNew = currentText + value

                Log.d("354352345234523533", valueNew)
                if (valueNew.isNotEmpty()) {
                    if (valueNew.first().toString().equals("0")) {
                      if(isValidZeroDecimal(valueNew)){
                          binding.tvInterest.setText(valueNew)
                          maxValueTax()
                      }
                    }else
                    {
                        binding.tvInterest.setText(valueNew)
                        maxValueTax()
                    }
                } else {
                    var clearText: String =
                        if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                    val concatenatedText = clearText + value
                    if (isNumberLengthValid(concatenatedText)) {
                        binding.tvInterest.setText(concatenatedText)
                        checkEnableButtonResult()
                        maxValueTax()
                    }

                }




            }

            "barrowtime" -> {

                val currentText = binding.tvBarrowTime.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value

                binding.tvBarrowTime.setText(concatenatedText)
                checkEnableButtonResult()

                checkTimeBorrow()


            }

            "payinterest" -> {

                val currentText = binding.tvPayInterest.text.toString() ?: ""
                var clearText: String =
                    if (currentText.length > 0) clearDotsAndCommas(currentText) else ""
                val concatenatedText = clearText + value
                if (isNumberLengthValid(concatenatedText)) {
                    binding.tvPayInterest.setText(concatenatedText)
                    checkEnableButtonResult()

                    checkPayInterest()
                }

            }
        }

    }

    private fun checkPayInterest() {
        if (binding.tvBarrowTime.text.isNotEmpty()) {
            if (binding.tvPayInterest.text.toString()
                    .toDouble() > binding.tvBarrowTime.text.toString().toDouble()
            ) {
                binding.tvPayInterest.setText("")
                Toast.makeText(this, "Max value is 100", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClearItem(mode: String) {
        when (mode) {
            "principal_amount" -> {

                val currentText = binding.tvPrincipalamount.text ?: ""
                if (currentText.isNotEmpty()) {
                    var valueClear = clearDotsAndCommas(
                        (currentText.substring(0, currentText.length - 1)).toString()
                    )
                    if (valueClear.isNotEmpty()) binding.tvPrincipalamount.setText(
                        df.format(valueClear.toBigDecimal()).toString()
                    )
                    else binding.tvPrincipalamount.setText("")
                    checkEnableButtonResult()
                }


            }

            "interest" -> {
                val currentText = binding.tvInterest.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvInterest.setText(currentText.substring(0, currentText.length - 1))
                    checkEnableButtonResult()
                }
            }

            "barrowtime" -> {
                val currentText = binding.tvBarrowTime.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvBarrowTime.setText(currentText.substring(0, currentText.length - 1))
                    checkEnableButtonResult()

                    if (binding.tvBarrowTime.text.isNotEmpty()) {
                        checkTimeBorrow()
                    }
                }
            }

            "payinterest" -> {

                val currentText = binding.tvPayInterest.text ?: ""
                if (currentText.isNotEmpty()) {
                    binding.tvPayInterest.setText(currentText.substring(0, currentText.length - 1))
                    checkEnableButtonResult()
                    if (binding.tvPayInterest.text.isNotEmpty()) {
                        checkPayInterest()
                    }

                }

            }


        }

    }

    override fun onClearAll(mode: String) {
        when (mode) {
            "principal_amount" -> {
                binding.tvPrincipalamount.setText("")

            }

            "interest" -> {
                binding.tvInterest.setText("")
            }

            "barrowtime" -> {
                binding.tvBarrowTime.setText("")

            }

            "payinterest" -> {
                binding.tvPayInterest.setText("")
            }

        }
    }

    fun clearDotsAndCommas(input: String): String {
        if (input.isNotEmpty()) {
            return input.replace(".", "").replace(",", "").toString()
        } else
            return ""
    }

    fun isNumberLengthValid(text: String): Boolean {
        if (text.isNotEmpty()) {
            val text = text.toString()
            val digitsOnly = text.filter { it.isDigit() }
            return digitsOnly.length <= 15
        }
        return true
    }

    fun checkEnableButtonResult() {
        if ((binding.tvInterest.text.toString()
                .isNotEmpty() && binding.tvPrincipalamount.text.toString().isNotEmpty()
                    && binding.tvBarrowTime.text.toString()
                .isNotEmpty() && binding.tvPayInterest.text.toString().isNotEmpty())
        ) {
            Log.d("094532094502345230", "50234573024502")
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvInterest.text.toString().length > 0 || binding.tvPrincipalamount.text.toString().length > 0
                    || binding.tvBarrowTime.text.toString().length > 0)
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
        binding.tvPrincipalamount.isEnabled = boolean
        binding.tvInterest.isEnabled = boolean
        binding.tvBarrowTime.isEnabled = boolean
        binding.tvPayInterest.isEnabled = boolean
    }


    override fun onPopupWindowOpened(spinner: Spinner?) {
        when (spinner?.id) {
            R.id.spMethodPayment -> {
                binding.spMethodPayment.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }

            R.id.spinner_borrow_time -> {
                binding.spinnerBorrowTime.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }

            R.id.spinner_pay_interest -> {
                binding.spinnerPayInterest.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit)
            }
        }
    }

    override fun onPopupWindowClosed(spinner: Spinner?) {
        when (spinner?.id) {
            R.id.spMethodPayment -> {
                binding.spMethodPayment.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }

            R.id.spinner_borrow_time -> {
                binding.spinnerBorrowTime.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }

            R.id.spinner_pay_interest -> {
                binding.spinnerPayInterest.setBackgroundResource(com.example.matheasyapp.R.drawable.bg_spinner_fruit_up)
            }

        }
    }

    fun isValidZeroDecimal(input: String): Boolean {
        // Sử dụng Regex để kiểm tra
        val regex = "^0(\\.\\d*)?$".toRegex()
        return regex.matches(input)
    }

}


data class PaymentDetail(
    val month: Int,
    val originalCapital: Double,
    val interestRate: Double,
    val payments: Double,
    val surplus: Double
)


