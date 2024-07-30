package com.example.matheasyapp.view.tool.activity

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.matheasyapp.databinding.ActivityBorrowBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.model.LoanPayment
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class BorrowActivity : AppCompatActivity() {

    val spinnerItems = arrayOf("Tổng số tiền thanh toán bằng nhau", "Thanh toán đầy đủ")

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBorrowBinding

    private lateinit var viewModelKeyBoard: KeyboardViewModel

    private var selectedMode: String = "";

    var list = ArrayList<LoanPayment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBorrowBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spMethodPayment.adapter = adapter

        // Đặt giá trị mặc định cho Spinner (ví dụ chọn "Option 2")
        val defaultSelectionIndex = spinnerItems.indexOf("Tổng số tiền thanh toán bằng nhau")
        if (defaultSelectionIndex != -1) {
            binding.spMethodPayment.setSelection(defaultSelectionIndex)
            selectedMode = "equals"
        }

        viewModelKeyBoard = ViewModelProvider(this).get(KeyboardViewModel::class.java)

        viewModelKeyBoard.getPrincipalLoanAmountValue()
            .observe(this, Observer { newValue -> binding.tvPrincipalamount.text = newValue })

        viewModelKeyBoard.getInterestValue()
            .observe(this, Observer { newValue -> binding.tvInterest.text = newValue })

        viewModelKeyBoard.getBorrowTimeValue().observe(this, Observer { newValue ->
            binding.tvBarrowTime.text = newValue
        })

        viewModelKeyBoard.getPayInterestValue().observe(this, Observer { newValue ->
            binding.tvPayInterest.text = newValue
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
                        selectedMode = "all"
                    }

                    // Thực hiện hành động với selectedOption

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Xử lý sự kiện khi không có item nào được chọn
                    println("No option selected")
                }
            }



        list.add(
            LoanPayment(
                "N",
                "Vốn gốc",
                "Lãi suất",
                "Thanh toán",
                " Số dư "
            )
        )

        binding.btnResult.setOnClickListener {
             if(selectedMode.equals("all")){
                 val principal = binding.tvPrincipalamount.text.toString().toDouble()
                 val annualInterestRate = binding.tvInterest.text.toString().toDouble()
                 val loanTermMonths = binding.tvBarrowTime.text.toString().toInt()
                 val loanPayments: ArrayList<LoanPayment> = calculatorLoan(principal, annualInterestRate, loanTermMonths)

                 val intent = Intent(this, ResultActivity::class.java)
                 intent.putParcelableArrayListExtra("myObjects", loanPayments)
                 startActivity(intent)

             }else{
//                 val principal = binding.tvPrincipalamount.text.toString().toDouble()
//                 val annualInterestRate = binding.tvInterest.text.toString().toDouble()
//                 val loanTermMonths = binding.tvBarrowTime.text.toString().toInt()


//                 val loanPayments: ArrayList<LoanPayment> =
//                     calculateLoanPayments(principal, annualInterestRate, loanTermMonths)
//
//                 val intent = Intent(this, ResultActivity::class.java)
//                 intent.putParcelableArrayListExtra("myObjects", loanPayments)
//                 startActivity(intent)


                 val principal = 100000.0
                 val annualInterestRate = 10.0
                 val loanTermMonths = 10
                 val paymentIntervalMonths = 1

                 var paymentSchedule : ArrayList<LoanPayment> = calculateLoanPayments2222(
                     principal,
                     annualInterestRate,
                     loanTermMonths,

                 )
//                 paymentSchedule

                 for (payment in paymentSchedule) {
                     println("Month ${payment.month}: Principal Payment ${payment.principal} , " +
                             "Interest Payment ${payment.interest} , Total Payment ${payment.payment} , " +
                             "Remaining Principal ${payment.balance}")
                 }

             }
        }



        onClick()

        //c2
//        val principal1 = 200.000
//        val annualInterestRate1 = 5.0
//        val termMonths = 6
//        val interestPaymentFrequency = 1
//
//        calculateLoanPayments(principal1, annualInterestRate1, termMonths, interestPaymentFrequency)


    }

    fun calculateLoanPayments2222(
        principal: Double,
        annualInterestRate: Double,
        loanTermMonths: Int,

        ): ArrayList<LoanPayment> {
        val monthlyInterestRate = annualInterestRate / 12 / 100
        var remainingPrincipal = principal

        var payments = ArrayList<LoanPayment>()

        for (month in 1..loanTermMonths) {
            val interestPayment = remainingPrincipal * monthlyInterestRate

            val principalPayment = if (month == loanTermMonths) remainingPrincipal else 0.0
            val totalPayment = interestPayment + principalPayment

            remainingPrincipal -= principalPayment

            payments.add(
                LoanPayment(
                    month.toString(),
                    "%.2f".format(principalPayment).toString(),
                    "%.2f".format(interestPayment).toString(),
                    "%.2f".format(totalPayment).toString(),
                    "%.2f".format(remainingPrincipal).toString()
                )
            )
        }

        return payments
    }

    fun removeSymbols(input: String): String {
        // Loại bỏ dấu phân cách thập phân và phân cách nghìn
        val cleanedString = input.replace(Regex("[.,]"), "")
        return cleanedString
    }




    fun calculatorLoan(
        principalValue: Double,
        interest: Double,
        timeBarrow: Int
    ): ArrayList<LoanPayment> {
        val df = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))

        val interestValue = (interest / 100 / 12) * principalValue

//        println("sdfashfa  $interestValue")
        var param1: Double = 0.0
        var param2: Double = 0.0
        var param3: Double = 0.0

        for (i in 1..timeBarrow) {
            if (i == timeBarrow) {
                val valuePayment = interestValue + principalValue
                param1 += principalValue
                param2 += interestValue
                param3 += valuePayment

                list.add(
                    LoanPayment(
                        month = df.format(timeBarrow).toString(),
                        principal = df.format(principalValue).toString(),
                        interest = df.format(interestValue).toString(),
                        payment = df.format(valuePayment).toString(),
                        balance = "0"
                    )
                )
                println("aaaaaaa ${param2}")

            } else {
                param2 += interestValue
                param3 += interestValue

                list.add(
                    LoanPayment(
                        month = i.toString(),
                        principal = "0",
                        interest = df.format(interestValue).toString(),
                        payment = df.format(interestValue).toString(),
                        balance = df.format(principalValue).toString()
                    )
                )
                println("aaaaaaa ${param2}")

            }
        }

        list.add(
            LoanPayment(
                month = "sys",
                principal = df.format(param1).toString(),
                interest = df.format(param2).toString(),
                payment = df.format(param3).toString(),
                "0"

            )
        )

        return list
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





//    fun calculateLoanPayments(
//        principal: Double,
//        annualInterestRate: Double,
//        termMonths: Int,
//
//    ): ArrayList<LoanPayment> {
//        val monthlyInterestRate = annualInterestRate / 12 / 100
//        val monthlyInterestPayment = principal * monthlyInterestRate
//
//        var remainingPrincipal = principal
//        val loanPayments = ArrayList<LoanPayment>()
//
//        var param1 : Double = 0.0
//        var param2 : Double = 0.0
//
//        var param3 : Double = 0.0
//        val df = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))
//
//
//
//        for (month in 1..termMonths) {
//
//            val interestPayment = monthlyInterestPayment
//            val principalPayment = if (month == termMonths) remainingPrincipal else 0.0
//            val totalMonthlyPayment = interestPayment + principalPayment
//
//            if (month == termMonths) {
//                remainingPrincipal = 0.0
//            }
//
//            param1 += principalPayment
//            param2 += interestPayment
//            param3 += totalMonthlyPayment
//
//            // Tạo đối tượng LoanPayment và thêm vào danh sách
//            val loanPayment = LoanPayment(
//                month = "$month",
//                principal = df.format(principalPayment).toString(),
//                interest = df.format(interestPayment).toString(),
//                payment = df.format(totalMonthlyPayment).toString(),
//                balance = df.format(remainingPrincipal).toString()
//            )
//
//
//
//            // Cập nhật số dư nợ còn lại cho các tháng tiếp theo
//            if (month < termMonths) {
//                remainingPrincipal = principal
//            }
//        }
//
//        loanPayments.add(
//            LoanPayment(
//                month = "sys",
//                principal = df.format(param1).toString(),
//                interest = df.format(param2).toString(),
//                payment = df.format(param3).toString(),
//                "0"
//
//            )
//        )
//
//        return loanPayments
//    }




}


//phương thức trả nợ là Thanh toán đầy đủ , khoản vay gốc là 10000 .
//lãi suất là là 55% thời hạn vay là 5 tháng , thời hạn trả lãi là 1 tháng
//hãy tính các thông số sau phải trả theo từng tháng : vốn gốc từng tháng ,
//lãi suất từng tháng , thanh toán từng tháng , và số dư từng tháng

//Thông số của khoản vay:
//
//Khoản vay gốc (P) = 10,000 đơn vị tiền tệ
//Lãi suất hàng tháng (r) = 55% / 12 = 0.55 / 12 = 0.0458333 (lãi suất hàng tháng là lãi suất hàng năm chia cho 12 để tính tháng)
//Thời hạn vay:
//
//Thời hạn vay (n) = 5 tháng
//Thời hạn trả lãi đầu (m) = 1 tháng
//
//Tháng 1:
//
//Số dư ban đầu (B₀) = 10,000 đơn vị tiền tệ
//Lãi suất hàng tháng (r) = 0.0458333
//Số tiền trả lãi hàng tháng (I₁) = B₀ * r = 10,000 * 0.0458333 = 458.333 đơn vị tiền tệ