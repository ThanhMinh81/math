package com.example.matheasyapp.view.tipsscreen

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.databinding.ActivityTipsBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel


class TipsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTipsBinding

    private lateinit var keyBoardViewModel: KeyboardViewModel

    private var stateButton: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTipsBinding.inflate(layoutInflater)

        keyBoardViewModel = ViewModelProvider(this).get(KeyboardViewModel::class.java)

        stateButton = true

        iniView()
        onClickKeyBoard()
        handleClick()

        setContentView(binding.root)

    }

    private fun onClickKeyBoard() {

        binding.layoutMoneyBill.setOnClickListener {
            showKeyBoard("money_bill")
        }
        binding.tvMoneyBill.setOnClickListener {
            showKeyBoard("money_bill")

        }

        binding.layoutPersonAmount.setOnClickListener {
            showKeyBoard("person_amount")
        }
        binding.tvPersonAmount.setOnClickListener {
            showKeyBoard("person_amount")

        }

        binding.layoutMonneyGive.setOnClickListener {
            showKeyBoard("money_give")
        }
        binding.tvMoneyGive.setOnClickListener {
            showKeyBoard("money_give")

        }

        binding.layoutPercentMoneyGive.setOnClickListener {

            showKeyBoard("percent_give")
        }
        binding.tvPercentMoney.setOnClickListener {
            showKeyBoard("percent_give")

        }

        binding.layoutMonneyScot.setOnClickListener {
            showKeyBoard("money_scot")
        }
        binding.tvMoneyScot.setOnClickListener {
            showKeyBoard("money_scot")

        }

        binding.layoutPrecentVat.setOnClickListener {
            showKeyBoard("percent_vat")
        }
        binding.tvPrecentVat.setOnClickListener {
            showKeyBoard("percent_vat")

        }

        keyBoardViewModel.getValueMoneyBill().observe(this,
            Observer { value ->
                checkEnableButtonResult()
                binding.tvMoneyBill.setText(value)
            })

        keyBoardViewModel.getValueAmountPerson().observe(this,
            Observer { value ->
                checkEnableButtonResult()

                binding.tvPersonAmount.setText(value)
            })

        keyBoardViewModel.getValueAmountMoneyGive().observe(this,
            Observer { value ->
                checkEnableButtonResult()

                binding.tvMoneyGive.setText(value)

                if(binding.tvMoneyBill.text.length > 0){
                    val moneyBill = binding.tvMoneyBill.text.toString().toDouble()


                    if (value.length > 0 && moneyBill > 0) {

                        val percentGive = (value.toDouble() / moneyBill) * 100
                        binding.tvPercentMoney.setText(percentGive.toString())

                    } else {
                        binding.tvPercentMoney.setText("")
                    }
                }


            })

        keyBoardViewModel.getValuePercentMoneyGive().observe(this,
            Observer { newValue ->
                checkEnableButtonResult()

                binding.tvPercentMoney.setText(newValue)

                if (newValue.length > 0 && !newValue.equals("")) {
                    val value =
                        (newValue.toString().toDouble() / 100) * binding.tvMoneyBill.text.toString()
                            .toDouble()
                    binding.tvMoneyGive.setText(value.toString())
//                    Log.d("444444 ",value.toString())
                } else {
                    binding.tvMoneyGive.setText("")
                }

            })

        keyBoardViewModel.getValueAmountScot().observe(this,
            Observer { value ->
                binding.tvMoneyScot.setText(value)

                 if(binding.tvMoneyBill.text.length > 0){
                     val moneyBase = binding.tvMoneyBill.text.toString().toDouble()

                     if (value.length > 0 && value.isNotEmpty()) {
                         val valueVAT = (value.toDouble() / moneyBase) * 100
                         binding.tvPrecentVat.setText(valueVAT.toString())
                     } else {
                         binding.tvPrecentVat.setText("")
                     }
                 }

            })

        keyBoardViewModel.getValueAmountVAT().observe(this,
            Observer { value ->
                binding.tvPrecentVat.setText(value)
            })


    }

    private fun showKeyBoard(key: String) {
        keyBoardViewModel.setKeyArgumentValue(key)
        val keyBoard = BottomsheftKeyboard()

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    private fun iniView() {

        checkStateButton()
        checkEnableButtonResult()
        setEnableInput(true)


        binding.layoutResult.visibility = View.GONE
        binding.layoutMoneyBill.setOnClickListener {

        }
        binding.layoutContainerAddTips.visibility = View.GONE
        binding.layoutContainerNonVat.visibility = View.GONE
        binding.layoutNonTipsVat.visibility = View.GONE

        binding.btnResult.setOnClickListener {

            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()
                binding.layoutResult.visibility = View.VISIBLE

                val lastMoney: Double =
                    binding.tvMoneyBill.text.toString()
                        .toDouble() + binding.tvMoneyGive.text.toString()
                        .toDouble();
                val moneyPerson = lastMoney / binding.tvPersonAmount.text.toString().toDouble()
                binding.tvResultLastMoney.setText(lastMoney.toString())
                binding.tvResultPerson.setText(moneyPerson.toString())
            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {
                // clear all
                binding.tvMoneyBill.setText("")
                binding.tvPersonAmount.setText("")
                binding.tvMoneyGive.setText("")
                binding.tvPercentMoney.setText("")
                binding.tvMoneyScot.setText("")
                binding.tvPrecentVat.setText("")
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

    private fun checkStateButton() {
        if (stateButton) {

            binding.btnResult.setText("Xem kết quả")
            binding.btnCancle.setText("Xoá hết")

        } else {

            binding.btnResult.setText("Trang chủ")
            binding.btnCancle.setText("Chỉnh sửa dữ liệu")

        }
    }

    fun checkEnableButtonResult() {
        if ((binding.tvMoneyBill.text.toString().length > 0 && binding.tvPersonAmount.text.toString().length > 0
                    && binding.tvPersonAmount.text.toString().toDouble() > 0.0
                    && binding.tvMoneyGive.text.toString().length > 0 && binding.tvPercentMoney.text.toString().length > 0)
        ) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvMoneyBill.text.toString().length > 0 || binding.tvPersonAmount.text.toString().length > 0
                    || binding.tvMoneyGive.text.toString().length > 0 ||
                    binding.tvPersonAmount.text.toString().length > 0
                    || binding.tvPercentMoney.text.toString().length > 0)
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

    private fun handleClick() {

        binding.swAddTips.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.layoutContainerAddTips.visibility = View.VISIBLE
                binding.layoutNonTipsVat.visibility = View.VISIBLE
            } else {
                binding.layoutContainerAddTips.visibility = View.GONE
                binding.layoutNonTipsVat.visibility = View.GONE

            }
        })

        binding.scAddTipsVAT.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.layoutContainerNonVat.visibility = View.VISIBLE
            } else {
                binding.layoutContainerNonVat.visibility = View.GONE
            }
        })

    }

    private fun setEnableInput(boolean: Boolean) {
        binding.layoutMoneyBill.isEnabled = boolean
        binding.tvMoneyBill.isEnabled = boolean
        binding.spMoneyBill.isEnabled = boolean
        binding.layoutPersonAmount.isEnabled = boolean
        binding.tvPersonAmount.isEnabled = boolean
        binding.layoutMonneyGive.isEnabled = boolean
        binding.tvMoneyGive.isEnabled = boolean
        binding.spMoneyGive.isEnabled = boolean
        binding.layoutPercentMoneyGive.isEnabled = boolean
        binding.layoutMonneyScot.isEnabled = boolean
        binding.tvMoneyScot.isEnabled = boolean
        binding.spMoneyScot.isEnabled = boolean
        binding.layoutPrecentVat.isEnabled = boolean
        binding.tvPercentMoney.isEnabled = boolean
    }


}