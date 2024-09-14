package com.example.matheasyapp.bottomsheft

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.BottomSheetChangeCaculatorBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheftStateCaculator : BottomSheetDialogFragment() {

    private var binding: BottomSheetChangeCaculatorBinding? = null


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
        binding = BottomSheetChangeCaculatorBinding.inflate(inflater, container, false)

//       binding!!.root.post {
//           val peekHeightPx = -80.dpToPx(requireContext())
//           (binding!!.root.parent as View).translationY = peekHeightPx.toFloat()
//       }

        val passedText = arguments?.getString("passedText")
        println("354i5234532 ${passedText}")
        if (passedText != null && passedText.length > 0) {
            if (passedText.equals("caculator")) {
                binding!!.icCheckCaculator.visibility = View.VISIBLE
                binding!!.icCheckUnit.visibility = View.GONE
                binding!!.icCheckMoney.visibility = View.GONE
            } else if (passedText.equals("unit")) {
                binding!!.icCheckCaculator.visibility = View.GONE
                binding!!.icCheckUnit.visibility = View.VISIBLE
                binding!!.icCheckMoney.visibility = View.GONE
            } else if (passedText.equals("money")) {
                binding!!.icCheckCaculator.visibility = View.GONE
                binding!!.icCheckUnit.visibility = View.GONE
                binding!!.icCheckMoney.visibility = View.VISIBLE
            }
        }

//        binding!!.root.post {
//            // Convert 50dp to pixels
//            val peekHeightPx = 90.dpToPx(requireContext())
//
//            // Adjust BottomSheetBehavior to place the bottom of the sheet just above the BottomNavigationView
//            val bottomSheet = (binding!!.root.parent as View)
//            val behavior = BottomSheetBehavior.from(bottomSheet)
//
//            behavior.peekHeight = peekHeightPx
//        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding!!.containerCaculator.setOnClickListener {
            listener.onOptionSelected("caculator")
            dismiss()
        }

        binding!!.containerUnit.setOnClickListener {
            listener.onOptionSelected("unit")
            dismiss()
        }

        binding!!.containerMoney.setOnClickListener {
            listener.onOptionSelected("money")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
}
