package com.example.matheasyapp.view.tool.activity

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityCostMaterialBinding
import com.example.matheasyapp.view.tool.bottomsheft.BottomsheftKeyboard
import com.example.matheasyapp.view.tool.viewmodel.KeyboardViewModel

class CostMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCostMaterialBinding

    private lateinit var keyBoardViewModel: KeyboardViewModel

    private var stateButton: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCostMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stateButton = true


        keyBoardViewModel = ViewModelProvider(this).get(KeyboardViewModel::class.java)

        binding.layoutResult.visibility  = View.GONE

        setEnableButtonResult(0)
        setEnableButtonCancle(0)


        binding.tvDistance.setOnClickListener {
            showKeyBoard("value_distance")
        }

        binding.tvValueMaterial.setOnClickListener {
            showKeyBoard("value_material")
        }

        binding.tvValueGas.setOnClickListener {
            showKeyBoard("value_gas")
        }


        keyBoardViewModel.getValueMaterial().observe(this, Observer { value ->
            checkEnableButtonResult()
            binding.tvValueMaterial.setText(value.toString())
        })

        keyBoardViewModel.getValueGas().observe(this, Observer { value ->
            checkEnableButtonResult()
            binding.tvValueGas.setText(value.toString())

        })

        keyBoardViewModel.getValueDistance().observe(this, Observer { value ->
            checkEnableButtonResult()
            binding.tvDistance.setText(value.toString())

        })

        onClick()

    }


    private fun showKeyBoard(key: String) {
        keyBoardViewModel.setKeyArgumentValue(key)
        val keyBoard = BottomsheftKeyboard()

        keyBoard.show(supportFragmentManager, "my_bottomsheft")
    }

    fun checkEnableButtonResult() {
        if ((binding.tvValueMaterial.text.toString().length > 0 && binding.tvValueGas.text.toString().length > 0
                    && binding.tvDistance.text.toString().length > 0)
        ) {
            setEnableButtonResult(2)
        } else {
            setEnableButtonResult(0)
        }

        if ((binding.tvValueMaterial.text.toString().length > 0 || binding.tvValueGas.text.toString().length > 0
                    || binding.tvDistance.text.toString().length > 0)
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

    private fun setEnableInput(boolean: Boolean) {
        binding.tvDistance.isEnabled = boolean
        binding.tvValueMaterial.isEnabled = boolean
        binding.tvValueGas.isEnabled = boolean

    }

    private fun checkStateButton() {

             costMaterial()

        if (stateButton) {

            binding.btnResult.setText("Xem kết quả")
            binding.btnCancle.setText("Xoá hết")

        } else {

            binding.btnResult.setText("Trang chủ")
            binding.btnCancle.setText("Chỉnh sửa dữ liệu")

        }
    }

    private fun costMaterial() {

        if(binding.tvValueMaterial.text.length > 0 && binding.tvDistance.text.length > 0 &&
            binding.tvValueGas.text.length > 0){

            val valueMaterial = (binding.tvDistance.text.toString().toDouble() / binding.tvValueMaterial.text.toString().toDouble())

            var valueCost =  valueMaterial * binding.tvValueGas.text.toString().toDouble()

            binding.tvResultLastMoney.setText(valueCost.toString())
            binding.tvResultPerson.setText(valueMaterial.toString())

        }

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
                binding.tvDistance.setText("")
                binding.tvValueMaterial.setText("")
                binding.tvValueGas.setText("")

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


}