package com.example.matheasyapp.view.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.databinding.ActivityHistoryPaymentBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.view.tool.adapter.AdapterHistoryPayment
import com.example.matheasyapp.view.tool.model.LoanHistory


class HistoryPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryPaymentBinding

    lateinit var adapterHistoryPayment: AdapterHistoryPayment

    lateinit var paymentList: ArrayList<LoanHistory>

    private lateinit var database: HistoryDatabase

    var checkSelectedAll: Boolean = false

    var itemHistoryUnCheck: LoanHistory? = null

//    private lateinit var historyLiveData: HistoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentList = ArrayList();
        binding.rcvHistory.layoutManager = LinearLayoutManager(this)

        adapterHistoryPayment = AdapterHistoryPayment(paymentList, this)


        adapterHistoryPayment.notifyDataSetChanged()
        binding.rcvHistory.adapter = adapterHistoryPayment

        database = HistoryDatabase.getDatabase(this)

        getData();



        eventClick()

    }


    private fun eventClick() {

    }


    private fun getData() {
        paymentList.clear()

        val list: List<LoanHistory> = database.loanHistoryDao().getAllLoanHistory()

        var loanPay: LoanHistory = LoanHistory(
            0, "", "", "Vốn gốc", "Lãi suất",
            "Thanh toán", "", "", "", ""
        )

        paymentList.add(loanPay)
        paymentList.addAll(list)

        adapterHistoryPayment.notifyDataSetChanged()
    }


}