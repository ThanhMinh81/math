package com.example.matheasyapp.view.tool.activity

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.matheasyapp.databinding.ActivityBorrowBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.model.LoanHistory
import com.example.matheasyapp.view.tool.model.LoanPayment
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class BorrowActivity : AppCompatActivity() {

    val spinnerItems = arrayOf("Tổng số tiền thanh toán bằng nhau", "Thanh toán đầy đủ")

    val spinnerBorrowTime = arrayOf("Năm", "Tháng")
    val spinnerPayInterest = arrayOf("Năm", "Tháng")

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBorrowBinding

    private lateinit var viewModelKeyBoard: KeyboardViewModel

    private var selectedMode: String = "";
    private var valueSpBorrow: String = "Năm"
    private var valueSpPay: String = "Năm"

    private val REQUEST_CODE: Int = 1

    lateinit var list: ArrayList<LoanPayment>;

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

        list = ArrayList<LoanPayment>()

        database = HistoryDatabase.getDatabase(this)

        setUpView()

        viewModelKeyBoard = ViewModelProvider(this).get(KeyboardViewModel::class.java)

        viewModelKeyBoard.getPrincipalLoanAmountValue()
            .observe(this, Observer { newValue -> binding.tvPrincipalamount.setText(newValue) })

        viewModelKeyBoard.getInterestValue()
            .observe(this, Observer { newValue -> binding.tvInterest.setText(newValue) })

        viewModelKeyBoard.getBorrowTimeValue().observe(this, Observer { newValue ->
            binding.tvBarrowTime.setText(newValue)
        })

        viewModelKeyBoard.getPayInterestValue().observe(this, Observer { newValue ->
            binding.tvPayInterest.setText(newValue)
        })


        // Đặt OnItemSelectedListener cho Spinner
        binding.spMethodPayment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    // Xử lý sự kiện khi một item được chọn

                    val selectedOption = spinnerItems[position]
                    println("Selected: $selectedOption")

                    if (selectedOption.equals("Tổng số tiền thanh toán bằng nhau")) {
                        selectedMode = "equals"
                    } else {
                        selectedMode = "full"
                    }

                    // Thực hiện hành động với selectedOption

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Xử lý sự kiện khi không có item nào được chọn
                    println("No option selected")
                }
            }


        binding.btnCancle.setOnClickListener {


        }

        binding.btnResult.setOnClickListener {

            if (binding.spMethodPayment.selectedItem.toString()
                    .equals("Tổng số tiền thanh toán bằng nhau")
            ) {
                // thời hạn cho vay tính bằng tháng or năm
                val termType: String =
                    if (binding.spinnerBorrowTime.selectedItem.equals("Năm")) "years" else "months"

                // thời hạn trả lãi tính bằng tháng or năm
                val intervalType: String =
                    if (binding.spinnerPayInterest.selectedItem.equals("Năm")) "years" else "months"

                val paymentSchedule = calculateLoanSumEquals(
                    binding.tvPrincipalamount.text.toString().toDouble(),
                    binding.tvInterest.text.toString().toDouble(),
                    binding.tvBarrowTime.text.toString().toInt(),
                    binding.tvPayInterest.text.toString().toInt(),
                    termType,
                    intervalType
                )


                list.add(LoanPayment("N", "Vốn gốc", "Lãi suất", "Thanh toán", " Số dư "))

                list.addAll(paymentSchedule)

                val loanPayment: LoanHistory = addHistory()

                val intent = Intent(this, ResultActivity::class.java)
                intent.putParcelableArrayListExtra("list", list)
                intent.putExtra("object", loanPayment)

                activityResultLauncher.launch(intent);

                list.clear()

//                startActivity(intent)

            } else {
                // thời hạn cho vay tính bằng tháng or năm
                val termType: String =
                    if (binding.spinnerBorrowTime.selectedItem.equals("Năm")) "years" else "months"

                // thời hạn trả lãi tính bằng tháng or năm
                val intervalType: String =
                    if (binding.spinnerPayInterest.selectedItem.equals("Năm")) "years" else "months"


                val paymentSchedule = calculateLoanPaymentsFull(
                    binding.tvPrincipalamount.text.toString().toDouble(),
                    binding.tvInterest.text.toString().toDouble(),
                    binding.tvBarrowTime.text.toString().toInt(),
                    termType,
                    intervalType
                )

                list.add(LoanPayment("N", "Vốn gốc", "Lãi suất", "Thanh toán", " Số dư "))

                list.addAll(paymentSchedule)

                val loanPayment: LoanHistory = addHistory()

                val intent = Intent(this, ResultActivity::class.java)
                intent.putParcelableArrayListExtra("list", list)
                intent.putExtra("object", loanPayment)

                activityResultLauncher.launch(intent);

                list.clear()


            }

        }


        binding.icHistoryBorrow.setOnClickListener {

            val intent = Intent(this, HistoryPaymentActivity::class.java)

            startActivity(intent)
        }


        onClick()

    }

    private fun setUpView() {


        // Đặt giá trị mặc định cho Spinner (ví dụ chọn "Option 2")
        val defaultSelectionIndex = spinnerItems.indexOf("Tổng số tiền thanh toán bằng nhau")
        if (defaultSelectionIndex != -1) {
            binding.spMethodPayment.setSelection(defaultSelectionIndex)
            selectedMode = "equals"
        }


        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterBarrow = ArrayAdapter(this, R.layout.simple_spinner_item, spinnerBorrowTime)
        binding.spinnerBorrowTime.adapter = adapterBarrow

        val adapterInterest = ArrayAdapter(this, R.layout.simple_spinner_item, spinnerBorrowTime)
        binding.spinnerPayInterest.adapter = adapterInterest

        val adapterPay = ArrayAdapter(this, R.layout.simple_spinner_item, spinnerPayInterest)
        binding.spMethodPayment.adapter = adapterPay

        binding.spMethodPayment.adapter = adapter

        // Thiết lập OnItemSelectedListener
        binding.spinnerBorrowTime.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
//                println(" 44444  $selectedItem")
                    valueSpBorrow = selectedItem
                    println("aaaaa $valueSpBorrow")
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
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    valueSpPay = selectedItem
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
        val df = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))


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


        val loanPayment: LoanPayment = list.last()

        val loanHistory: LoanHistory = LoanHistory(
            0,
            binding.spMethodPayment.selectedItem.toString(),
            binding.tvPrincipalamount.text.toString(),
            loanPayment.interest,
            binding.tvBarrowTime.text.toString(),
            binding.spinnerBorrowTime.selectedItem.toString(),
            binding.tvPayInterest.text.toString(),
            binding.spinnerPayInterest.selectedItem.toString(),
            loanPayment.payment
        )

        val newID = database.loanHistoryDao().insert(loanHistory)

        if (newID != -1L) {
            Log.d("453535", "Insert successful, new ID: $newID")
        } else {
            println("Insert failed")
        }

        return loanHistory


    }


    private fun onClick() {
        binding.layoutPrincipalAmount.setOnClickListener {
            viewModelKeyBoard.setKeyArgumentValue("principalAmount")
            val keyBoard = BottomsheftKeyboard()

            keyBoard.show(supportFragmentManager, "my_bottomsheft")
        }
        binding.layoutInterest.setOnClickListener {
            viewModelKeyBoard.setKeyArgumentValue("interest")
            val keyBoard = BottomsheftKeyboard()
            keyBoard.show(supportFragmentManager, "my_bottomsheft")
        }

        binding.layoutBarrowTime.setOnClickListener {
//            checkInputCorrect()

            viewModelKeyBoard.setKeyArgumentValue("barrowtime")
            val keyBoard = BottomsheftKeyboard()
            keyBoard.show(supportFragmentManager, "my_bottomsheft")
        }
        binding.layoutPayInterest.setOnClickListener {

            viewModelKeyBoard.setKeyArgumentValue("payinterest")
            val keyBoard = BottomsheftKeyboard()
            keyBoard.show(supportFragmentManager, "my_bottomsheft")
        }
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

            val loanPayment = LoanPayment(month = "$month", principal = df.format(principalPayment).toString(), interest = df.format(interestPayment).toString(), payment = df.format(totalMonthlyPayment).toString(), balance = df.format(remainingPrincipal).toString(),)

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



}


