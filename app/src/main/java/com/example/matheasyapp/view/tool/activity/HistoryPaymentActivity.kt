package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.databinding.ActivityHistoryPaymentBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.itf.OnclickPaymentHistory
import com.example.matheasyapp.view.tool.adapter.AdapterHistoryPayment
import com.example.matheasyapp.view.tool.model.LoanHistory
import com.example.matheasyapp.view.tool.model.LoanPayment


class HistoryPaymentActivity : AppCompatActivity(), OnclickPaymentHistory {

    lateinit var binding: ActivityHistoryPaymentBinding

    lateinit var adapterHistoryPayment: AdapterHistoryPayment

    lateinit var paymentList: ArrayList<LoanHistory>

    private lateinit var database: HistoryDatabase

    var checkSelectedAll: Boolean = false

    var itemHistoryUnCheck: LoanHistory? = null

//    private lateinit var historyLiveData: HistoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHistoryPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentList = ArrayList();
        binding.rcvHistory.layoutManager = LinearLayoutManager(this)

        adapterHistoryPayment = AdapterHistoryPayment(paymentList, this)
        adapterHistoryPayment.notifyDataSetChanged()
        binding.rcvHistory.adapter = adapterHistoryPayment

        database = HistoryDatabase.getDatabase(this)

        getData();

        binding.cbSwitchMode.visibility = View.GONE
        binding.bottomNavHistory.visibility = View.GONE

        binding.btnCancle.setOnClickListener {

            binding.icTrash.visibility = View.VISIBLE
            binding.cbSwitchMode.visibility = View.GONE
            binding.bottomNavHistory.visibility = View.GONE

            setEnableButton(0)

            adapterHistoryPayment.clearAllItemChecked()
            adapterHistoryPayment.notifyDataSetChanged()
            adapterHistoryPayment.showCheckbox(false)

        }

        eventClick()

    }

    fun setEnableButton(size: Int) {
        if (size > 0) {
            binding.btnRemove.alpha = 1f
            binding.btnRemove.isEnabled = true
        } else {
            binding.btnRemove.alpha = 0.5f
            binding.btnRemove.isEnabled = false
        }

    }


    private fun eventClick() {

        binding.icTrash.setOnClickListener {
            binding.icTrash.visibility = View.GONE
            binding.cbSwitchMode.visibility = View.VISIBLE
            binding.bottomNavHistory.visibility = View.VISIBLE

            setEnableButton(0)

            binding.cbSwitchMode.isChecked = false;

            adapterHistoryPayment.showCheckbox(true)
            adapterHistoryPayment.clearAllItemChecked()
            adapterHistoryPayment.notifyDataSetChanged()

        }

        // show dialog
//        binding.btnRemove.setOnClickListener {
//            val dialogFragment = HistoryDialogFragment()
//            historyLiveData.setValue(adapterHistory.getListChecked())
//            dialogFragment.show(supportFragmentManager, "DialogFragment")
//        }

        binding.icBack.setOnClickListener {

            if (binding.cbSwitchMode.visibility == View.VISIBLE) {
                // an cac view
                binding.cbSwitchMode.visibility = View.GONE
                binding.bottomNavHistory.visibility = View.GONE
                binding.icTrash.visibility = View.VISIBLE
                adapterHistoryPayment.clearAllItemChecked()
                adapterHistoryPayment.showCheckbox(false)
                adapterHistoryPayment.notifyDataSetChanged()
            } else {

                adapterHistoryPayment.clearAllItemChecked()
                adapterHistoryPayment.notifyDataSetChanged()
                onBackPressed()

            }
        }

        binding.cbSwitchMode.setOnClickListener {
            checkSelectedAll = true
        }



        binding.btnCancle.setOnClickListener {

            binding.icTrash.visibility = View.VISIBLE
            binding.cbSwitchMode.visibility = View.GONE
            binding.bottomNavHistory.visibility = View.GONE

            setEnableButton(0)

            adapterHistoryPayment.clearAllItemChecked()
            adapterHistoryPayment.notifyDataSetChanged()
            adapterHistoryPayment.showCheckbox(false)

        }

        binding.cbSwitchMode.setOnCheckedChangeListener { compoundButton, b ->

            compoundButton as CheckBox;

            if (b) {
                // selected all checkbox
                adapterHistoryPayment.clearAllItemChecked()
                adapterHistoryPayment.selectedAllItemChecked()
                adapterHistoryPayment.notifyDataSetChanged()
                compoundButton.setText("Bỏ chọn tất cả")
                setEnableButton(adapterHistoryPayment.getListChecked().size)


            } else {

                compoundButton.setText("Chọn tất cả")

                if (checkSelectedAll) {
                    // unCheck all

                    adapterHistoryPayment.clearAllItemChecked()
                    adapterHistoryPayment.notifyDataSetChanged()
                    setEnableButton(adapterHistoryPayment.getListChecked().size)


                } else {
                    // uncheck Item

                    adapterHistoryPayment.removeCheckItem(itemHistoryUnCheck)
                    adapterHistoryPayment.notifyDataSetChanged()
                    setEnableButton(adapterHistoryPayment.getListChecked().size)

                }

            }

        }

    }


    private fun getData() {
        paymentList.clear()

//        val listLoanTitle: List<LoanPayment> = listOf()
//        val resultPaymentitle: ResultPaymentEntity = ResultPaymentEntity(
//            sumPrincipal = "N",
//            sumInterest = "Vốn gốc",
//            sumPayment = "Lãi suất",
//            sumBalance = "Thanh toán"
//        )
//        val resultPaymentTitle : ResultPayment = ResultPayment(resultPaymentitle,listLoanTitle)
        val list : List<LoanHistory> = database.loanHistoryDao().getAllLoanHistory()

         var loanPay : LoanHistory = LoanHistory(0,"","" , "Vốn gốc","Lãi suất",
             "Thanh toán","","","")

         paymentList.add(loanPay)
        paymentList.addAll(list)

        adapterHistoryPayment.notifyDataSetChanged()
    }

    override fun onClickPaymentHistory(item: LoanHistory, boolean: Boolean, sizeList: Int) {
        if (boolean) {
            // checkItem

            adapterHistoryPayment.addCheckItem(item)
            adapterHistoryPayment.notifyDataSetChanged()

            setEnableButton(adapterHistoryPayment.getListChecked().size)

            if ((sizeList + 1) == paymentList.size) {
                binding.cbSwitchMode.isChecked = true
                binding.cbSwitchMode.setText("Bỏ chọn tất cả")
            }

        } else {

            // selected all

            itemHistoryUnCheck = item

            if (binding.cbSwitchMode.isChecked) {
                checkSelectedAll = false
                binding.cbSwitchMode.isChecked = false

                setEnableButton(adapterHistoryPayment.getListChecked().size)

            } else {

                adapterHistoryPayment.removeCheckItem(item)
                adapterHistoryPayment.notifyDataSetChanged()

                setEnableButton(adapterHistoryPayment.getListChecked().size)
            }

        }
    }
}