package com.example.matheasyapp.bottomsheft

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.BottomSheetChangeCaculatorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheftStateCaculator : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChangeCaculatorBinding? = null
    private val binding get() = _binding!!

    lateinit var listener: callBackFunction

    interface callBackFunction {
        fun onOptionSelected(option: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        targetFragment?.let {
            if (it is callBackFunction) {
                listener = it
            } else {
                throw ClassCastException("$it must implement DialogListener")
            }
        }
    }

    companion object {
        fun newInstance(text: String): BottomSheftStateCaculator {
            val fragment = BottomSheftStateCaculator()
            val args = Bundle()
            args.putString("passedText", text)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetChangeCaculatorBinding.inflate(inflater, container, false)

        // Get the passed text from arguments
        val passedText = arguments?.getString("passedText")
        println("354i5234532 ${passedText}")
        if (passedText != null && passedText.length > 0) {
            if (passedText.equals("caculator")) {
                binding.icCheckCaculator.visibility = View.VISIBLE
                binding.icCheckUnit.visibility = View.GONE
                binding.icCheckMoney.visibility = View.GONE
            } else if (passedText.equals("unit")) {
                binding.icCheckCaculator.visibility = View.GONE
                binding.icCheckUnit.visibility = View.VISIBLE
                binding.icCheckMoney.visibility = View.GONE
            } else if (passedText.equals("money")) {
                binding.icCheckCaculator.visibility = View.GONE
                binding.icCheckUnit.visibility = View.GONE
                binding.icCheckMoney.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerCaculator.setOnClickListener {
            listener.onOptionSelected("caculator")
            dismiss()
        }

        binding.containerUnit.setOnClickListener {
            listener.onOptionSelected("unit")
            dismiss()
        }

        binding.containerMoney.setOnClickListener {
            listener.onOptionSelected("money")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}