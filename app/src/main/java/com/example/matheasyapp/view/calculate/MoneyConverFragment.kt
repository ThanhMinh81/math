package com.example.matheasyapp.view.calculate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.bottomsheft.BottomSheftUnitFromConver
import com.example.matheasyapp.bottomsheft.BottomSheftUnitToConver
import com.example.matheasyapp.bottomsheft.money.BottomSheftFromMoney
import com.example.matheasyapp.bottomsheft.money.BottomSheftToMoney
import com.example.matheasyapp.databinding.FragmentMoneyConverBinding
import com.example.matheasyapp.livedata.ConvertMoneyViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel

class MoneyConverFragment : Fragment() {

    private lateinit var binding: FragmentMoneyConverBinding

    private lateinit var selectedViewModel: ConvertMoneyViewModel


    private lateinit var dialogFrom: BottomSheftFromMoney

    private lateinit var dialogTo: BottomSheftToMoney
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoneyConverBinding.inflate(layoutInflater, container, false)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())


        if ((sharedPreferencesHelper.getString("fromMoney", "null").equals("null"))) {

            sharedPreferencesHelper.saveValueTo("fromMoney", "USD")
            sharedPreferencesHelper.saveValueTo("toMoney", "AED")

        }


        binding.tvValueFrom.text = sharedPreferencesHelper.getString("fromMoney", "USD").toString()
        binding.tvValueTo.text = sharedPreferencesHelper.getString("toMoney", "AED").toString()

        selectedViewModel = ViewModelProvider(requireActivity()).get(ConvertMoneyViewModel::class.java)
        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        binding.wrapperUnitFrom.setOnClickListener {
            try {
                dialogFrom = BottomSheftFromMoney()
                dialogFrom.show(parentFragmentManager, "MyDialogFragment")
            } catch (e: Exception) {
                println("looiioio ${e}")
            }
        }

        selectedViewModel.getLiveDataValueMoneyFrom()
            .observe(requireActivity(), Observer { valueFrom ->
                binding.tvValueFrom.text  = valueFrom
            })

        selectedViewModel.getOnClickCloseFrom().observe(requireActivity(),
            Observer { value ->
                dialogFrom.dismiss()
                // ham chuyen doi
            })


        binding.wrapUnitTo.setOnClickListener {
            // Tooo
            dialogTo = BottomSheftToMoney()
            dialogTo.show(parentFragmentManager, "MyDialogFragment")
        }

        selectedViewModel.getLiveDataValueMoneyTo().observe(
            requireActivity(), Observer { valueFrom ->
                binding.tvValueTo.text = valueFrom
//                binding.tvValueTo.setText(sharedPreferencesHelper.getString("toMoney", ""))

            }
        )
        selectedViewModel.getOnClickCloseTo().observe(requireActivity(),
            Observer { value ->
                dialogTo.dismiss()
                // ham chuyen doi

            })


        return binding.root

    }


}