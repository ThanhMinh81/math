package com.example.matheasyapp.view.tool.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityResultBinding
import com.example.matheasyapp.view.tool.adapter.ResultAdapter
import com.example.matheasyapp.view.tool.model.LoanHistory
import com.example.matheasyapp.view.tool.model.LoanPayment


class ResultActivity : AppCompatActivity() {


    private lateinit var binding: ActivityResultBinding
    private lateinit var resultAdapter: ResultAdapter
    private lateinit var list: ArrayList<LoanPayment?>
    private lateinit var loanHistory: LoanHistory


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = ArrayList<LoanPayment?>()


        val listData: ArrayList<LoanPayment>? = intent.getParcelableArrayListExtra("listResult")
        loanHistory = intent.getParcelableExtra("object")!!

        list!!.addAll(listData!!)
        resultAdapter = ResultAdapter(this, list)
        binding.tvRcvResult.adapter = resultAdapter
        binding.tvRcvResult.layoutManager = LinearLayoutManager(this)

        Log.d("43525235", loanHistory.toString())

        binding.btnUpdate.setOnClickListener {

            val resultIntent = Intent()
            resultIntent.putExtra("object", loanHistory)

            setResult(RESULT_OK, resultIntent)
            finish()

        }

        binding.btnGohome.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.icShare.setOnClickListener {
            showInputDialog()
        }


    }

    fun showInputDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_share_loan)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancle: Button = dialog.findViewById(R.id.btnCancle)
        val btnOk: Button = dialog.findViewById(R.id.btnShare)

        val tvRepayment: TextView = dialog.findViewById(R.id.tvRepaymentMethod)
        val tvPrincipal: TextView = dialog.findViewById(R.id.tvPrincipal)
        val tvInterestPercent: TextView = dialog.findViewById(R.id.tvInterestPercent)
        val tvTenor: TextView = dialog.findViewById(R.id.tvTenor)
        val tvInterest: TextView = dialog.findViewById(R.id.tvInterest)
        val tvTotalPayment: TextView = dialog.findViewById(R.id.tvTotalPayment)

        tvRepayment.setText(loanHistory.paymentMethods)
        tvPrincipal.setText(loanHistory.borrowPrincipal)
        tvInterestPercent.setText(loanHistory.interestPercent + "%")
        tvTenor.setText(loanHistory.borrowTime + loanHistory.borrowTime)
        tvInterest.setText(loanHistory.paymentMethods)
        tvTotalPayment.setText(loanHistory.paymentSum)


        btnCancle.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            dialog.dismiss()
        }


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