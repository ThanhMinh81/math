package com.example.matheasyapp.view.tool.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.databinding.ActivityResultBinding
import com.example.matheasyapp.view.tool.adapter.ResultAdapter
import com.example.matheasyapp.view.tool.model.LoanHistory
import com.example.matheasyapp.view.tool.model.LoanPayment


class ResultActivity : AppCompatActivity() {


    private lateinit var binding: ActivityResultBinding
    private lateinit var resultAdapter: ResultAdapter
    private lateinit var list: ArrayList<LoanPayment?>
    private  lateinit var loanHistory : LoanHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = ArrayList<LoanPayment?>()

        // Nhận dữ liệu từ Intent trong Activity thứ 2
        val listData: ArrayList<LoanPayment>? = intent.getParcelableArrayListExtra("list")
        loanHistory = intent.getParcelableExtra("object")!!

        Log.d("53523525" ,  loanHistory.paymentMethods.toString())

        list!!.addAll(listData!!)
        resultAdapter = ResultAdapter(this, list)
        binding.tvRcvResult.adapter = resultAdapter
        binding.tvRcvResult.layoutManager = LinearLayoutManager(this)

        binding.btnUpdate.setOnClickListener {

            val resultIntent = Intent()
            resultIntent.putExtra("object",loanHistory )

            setResult(RESULT_OK, resultIntent)
            finish()

        }

        binding.btnGohome.setOnClickListener {

        }


    }


}