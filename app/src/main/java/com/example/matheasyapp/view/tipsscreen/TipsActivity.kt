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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTipsBinding.inflate(layoutInflater)

        keyBoardViewModel = ViewModelProvider(this).get(KeyboardViewModel::class.java)

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
                binding.tvMoneyBill.setText(value)
            })

        keyBoardViewModel.getValueAmountPerson().observe(this,
            Observer { value ->
                binding.tvPersonAmount.setText(value)
            })

        keyBoardViewModel.getValueAmountMoneyGive().observe(this,
            Observer { value ->
                binding.tvMoneyGive.setText(value)
            })

        keyBoardViewModel.getValuePercentMoneyGive().observe(this,
            Observer { value -> binding.tvPercentMoney.setText(value)  })

        keyBoardViewModel.getValueAmountScot().observe(this,
            Observer { value ->
                binding.tvMoneyScot.setText(value)
            })

        keyBoardViewModel.getValueAmountVAT().observe(this,
            Observer { value ->
                binding.tvPrecentVat.setText(value)
            })



    }

    private fun showKeyBoard(key :String) {
        keyBoardViewModel.setKeyArgumentValue(key)
        val keyBoard = BottomsheftKeyboard()

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    private fun iniView() {

        binding.layoutMoneyBill.setOnClickListener {

        }
        binding.layoutContainerAddTips.visibility = View.GONE
        binding.layoutContainerNonVat.visibility = View.GONE
        binding.layoutNonTipsVat.visibility = View.GONE


//        keyBoardViewModel.valueMoneyBill.observe(
//            this,
//            Observer { newValue -> binding.tvMoneyBill.setText(newValue) })

//        viewModelKeyBoard.getInterestValue()
//            .observe(this, Observer { newValue -> binding.tvInterest.setText(newValue) })
//
//        viewModelKeyBoard.getBorrowTimeValue().observe(this, Observer { newValue ->
//            binding.tvBarrowTime.setText(newValue)
//        })
//
//        viewModelKeyBoard.getPayInterestValue().observe(this, Observer { newValue ->
//            binding.tvPayInterest.setText(newValue)
//        })


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
}