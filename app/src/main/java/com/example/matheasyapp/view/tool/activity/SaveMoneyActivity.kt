package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.databinding.ActivitySaveMoneyBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel


class SaveMoneyActivity : AppCompatActivity() {

    lateinit var binding: ActivitySaveMoneyBinding;

    val listTypeSave = arrayOf("Tiền gửi cố định", "Tiết kiệm trả góp")

    val listInterest = arrayOf(
        "Điều quan tâm đơn giản ", "Lợi nhuận gộp hàng tháng", "Lợi nhuận gộp hàng quý ",
        "Lợi nhuận gộp nửa năm", "Lợi nhuận gộp hằng nam"
    )

    val listLevelSave = arrayOf("Mức ký quỹ", "Cấp độ mục tiêu")

    val listBillSave = arrayOf("Tháng", "Năm")

    private var stateButton: Boolean = true


    private lateinit var viewModelKeyBoard: KeyboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySaveMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stateButton = true
        binding.layoutResult.visibility = View.GONE
        setEnableButtonResult(0)
        setEnableButtonCancle(0)


        initWidget()
        onClick()

    }

    private fun onClick() {

        binding.btnResult.setOnClickListener {
            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()
                binding.layoutResult.visibility = View.VISIBLE

            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {

                // clear all
                binding.tvValueMoneyLevel.setText("")
                binding.tvValueInterest.setText("")
                binding.tvValueMoneySave.setText("")
                binding.tvValueTaxInterest.setText("")
                checkEnableButtonResult()
                setEnableInput(true)

            } else {
                setEnableInput(true)
                // update data
                stateButton = true
                checkStateButton()
                binding.layoutResult.visibility = View.GONE
            }
        }


    }

    private fun initWidget() {

        // spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypeSave)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTypeSave.adapter = adapter


        val adapterInterest = ArrayAdapter(this, android.R.layout.simple_spinner_item, listInterest)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFrequencyInterest.adapter = adapterInterest

        val adapterTime = ArrayAdapter(this, android.R.layout.simple_spinner_item, listBillSave)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTimeSaveBill.adapter = adapterTime

        val adapterLevel = ArrayAdapter(this, android.R.layout.simple_spinner_item, listLevelSave)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLevelMoney.adapter = adapterLevel

        binding.spLevelMoney.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String

                binding.tvTitleLevel.setText(selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        })

        viewModelKeyBoard = ViewModelProvider(this).get(KeyboardViewModel::class.java)


        viewModelKeyBoard.getValueMoneyLevel().observe(this, Observer { value ->
            checkEnableButtonResult()

            binding.tvValueMoneyLevel.setText(value.toString())


        })

        viewModelKeyBoard.getInterestValue().observe(this, Observer { value ->
            checkEnableButtonResult()

            binding.tvValueInterest.setText(value.toString())

        })

        viewModelKeyBoard.getValueMoneyBillSave().observe(this, Observer { value ->
            checkEnableButtonResult()

            binding.tvValueMoneySave.setText(value.toString())


        })

        viewModelKeyBoard.getValueTaxInterest().observe(this, Observer { value ->
            checkEnableButtonResult()

            binding.tvValueTaxInterest.setText(value.toString())


            1.211
            2.211


        })

        binding.tvValueMoneyLevel.setOnClickListener {
            showKeyBoard("money_level_save")
        }

        binding.tvValueInterest.setOnClickListener {
            showKeyBoard("interest_save")
        }
        binding.tvValueMoneySave.setOnClickListener {
            showKeyBoard("value_money_bill_save")
        }
        binding.tvValueTaxInterest.setOnClickListener {
            showKeyBoard("value_tax_interest")

        }

    }


    private fun showKeyBoard(key: String) {
        viewModelKeyBoard.setKeyArgumentValue(key)
        val keyBoard = BottomsheftKeyboard()

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    private fun setEnableInput(boolean: Boolean) {

        binding.tvValueMoneyLevel.isEnabled = boolean
        binding.tvValueInterest.isEnabled = boolean
        binding.tvValueMoneySave.isEnabled = boolean
        binding.tvValueTaxInterest.isEnabled = boolean
        binding.spTypeSave.isEnabled = boolean
        binding.spFrequencyInterest.isEnabled = boolean
        binding.spLevelMoney.isEnabled = boolean
        binding.spSelectedMoney.isEnabled = boolean
        binding.spTimeSaveBill.isEnabled = boolean


    }

    private fun checkStateButton() {
        if (stateButton) {

            binding.btnResult.setText("Xem kết quả")
            binding.btnCancle.setText("Xoá hết")

        } else {


            binding.btnResult.setText("Trang chủ")
            binding.btnCancle.setText("Chỉnh sửa dữ liệu")

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


        if ((binding.tvValueMoneyLevel.text.toString().length > 0 && binding.tvValueInterest.text.toString().length > 0
                    && binding.tvValueMoneySave.text.toString().length > 0 && binding.tvValueTaxInterest.text.toString().length > 0)
        ) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvValueMoneyLevel.text.toString().length > 0 || binding.tvValueInterest.text.toString().length > 0
                    || binding.tvValueMoneySave.text.toString().length > 0 || binding.tvValueTaxInterest.text.toString().length > 0)
        ) {
            setEnableButtonCancle(2)

        } else {
            setEnableButtonCancle(0)
        }
    }

}