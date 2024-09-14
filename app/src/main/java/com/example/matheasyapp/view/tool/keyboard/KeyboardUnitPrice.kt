package com.example.matheasyapp.view.tool.keyboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matheasyapp.databinding.KeyboardToolLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KeyboardUnitPrice : BottomSheetDialogFragment() {

    interface CallBackFunction {
        fun onClickNumber(mode: String, value: String)
        fun onClearItem(mode: String)
        fun onClearAll(mode: String)
    }


    private lateinit var typeInput: String
    lateinit var listener: CallBackFunction
    lateinit var  binding : KeyboardToolLayoutBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CallBackFunction) {
            listener = context
        } else {
            throw RuntimeException("$context must implement BottomSheetListener")
        }

    }

    companion object {
        fun newInstance(text: String): KeyboardUnitPrice {
            val fragment = KeyboardUnitPrice()
            val args = Bundle()
            args.putString("key", text)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = KeyboardToolLayoutBinding.inflate(inflater,container,false)

         typeInput = arguments?.getString("key").toString()

        Log.d("7574574252",typeInput.toString())


        binding.btn0.setOnClickListener { onNumberButtonClick("0") }
        binding.btn1.setOnClickListener { onNumberButtonClick("1") }
        binding.btn2.setOnClickListener { onNumberButtonClick("2") }
        binding.btn3.setOnClickListener { onNumberButtonClick("3") }
        binding.btn4.setOnClickListener { onNumberButtonClick("4") }
        binding.btn5.setOnClickListener { onNumberButtonClick("5") }
        binding.btn6.setOnClickListener { onNumberButtonClick("6") }
        binding.btn7.setOnClickListener { onNumberButtonClick("7") }
        binding.btn8.setOnClickListener { onNumberButtonClick("8") }
        binding.btn9.setOnClickListener { onNumberButtonClick("9") }

        binding.btn00.setOnClickListener { onNumberButtonClick("00") }

        binding.btnClear.setOnClickListener { onClearClick() }
        binding.btnAC.setOnClickListener { clearAllClick() }


        return binding.root
    }



    private fun onNumberButtonClick(number: String) {
        listener.onClickNumber(typeInput,number)

    }

    private fun clearAllClick() {
        listener.onClearAll(typeInput)
    }

    private fun onClearClick() {
      listener.onClearItem(typeInput)
    }


}