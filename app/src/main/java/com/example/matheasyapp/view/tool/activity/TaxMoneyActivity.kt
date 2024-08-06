package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityBorrowBinding
import com.example.matheasyapp.databinding.ActivityTaxMoneyBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel

class TaxMoneyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaxMoneyBinding

    private lateinit var keyBoardViewModel: KeyboardViewModel

    private var stateButton: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaxMoneyBinding.inflate(layoutInflater)

        stateButton = true

        setContentView(binding.root)

        setEnableButtonResult(0)
        setEnableButtonCancle(0)

        onClick()

        keyBoardViewModel = ViewModelProvider(this).get(KeyboardViewModel::class.java)

        keyBoardViewModel.getValueTaxt().observe(this, Observer { value ->

            checkEnableButtonResult()
            binding.tvValueTax.setText(value.toString())

        })

        keyBoardViewModel.getValueCost().observe(this, Observer { value ->
            checkEnableButtonResult()
            binding.tvValueCost.setText(value.toString())

        })


        binding.tvValueTax.setOnClickListener {
            showKeyBoard("value_tax")
        }
        binding.tvValueCost.setOnClickListener {
            showKeyBoard("value_cost")

        }


    }

    private fun onClick() {

        binding.btnResult.setOnClickListener {
            if (stateButton) {
                setEnableInput(false)
                stateButton = false
                checkStateButton()
            } else {
                onBackPressed()
            }

        }

        binding.btnCancle.setOnClickListener {

            if (stateButton) {

                // clear all
                binding.tvValueTax.setText("")
                binding.tvValueCost.setText("")
                checkEnableButtonResult()
                setEnableInput(true)

            } else {
                setEnableInput(true)
                // update data
                stateButton = true
                checkStateButton()
//                binding.layoutResult.visibility = View.GONE
            }
        }


    }




    fun checkEnableButtonResult() {
        if ((binding.tvValueTax.text.toString().length > 0 && binding.tvValueCost.text.toString().length > 0)
        ) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvValueTax.text.toString().length > 0 || binding.tvValueCost.text.toString().length > 0)
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


    private fun showKeyBoard(key: String) {
        keyBoardViewModel.setKeyArgumentValue(key)
        val keyBoard = BottomsheftKeyboard()

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    private fun setEnableInput(boolean: Boolean) {
        binding.tvValueTax.isEnabled = boolean
        binding.tvValueCost.isEnabled = boolean
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


}