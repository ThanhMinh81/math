package com.example.matheasyapp.view.calculate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.FragmentCalculateBinding
import com.example.matheasyapp.livedata.CaculatorViewModel

class CalculateFragment : Fragment() {

    private lateinit var binding: FragmentCalculateBinding
    private lateinit var viewModel: CaculatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalculateBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)

        viewModel.getShowTvResult().observe(viewLifecycleOwner, Observer { showTv ->
            if (showTv) binding.tvResult.visibility =
                View.VISIBLE else binding.tvResult.visibility = View.GONE
        })

        viewModel.getLiveTvCal().observe(
            viewLifecycleOwner,
            Observer {
                value ->
                Log.d("54252345","523454223")
                binding.tvCalculation.setText(value.toString())
            }
        )

        viewModel.getLiveTvResult().observe(
            viewLifecycleOwner,
            Observer { newValue ->
                    binding.tvResult.setText(newValue)
            })

        return binding.root

    }



}