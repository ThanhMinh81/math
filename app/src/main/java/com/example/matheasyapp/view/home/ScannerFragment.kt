package com.example.matheasyapp.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.matheasyapp.R
import com.example.matheasyapp.broadcast.BroadcastReceiverInternet
import com.example.matheasyapp.broadcast.registerNetworkReceiver
import com.example.matheasyapp.broadcast.unregisterNetworkReceiver
import com.example.matheasyapp.databinding.FragmentScannerBinding
import com.example.matheasyapp.view.history.historyscan.HistoryCameraActivity

class ScannerFragment : Fragment() {

    private lateinit var iView: View

    private lateinit var binding: FragmentScannerBinding

    private var currentPage: String = "Scan"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScannerBinding.inflate(layoutInflater, container, false)

        binding.rbScan.isChecked = true

        replaceFragment(CameraFragment())
        changeBackgroundToolbar("camera")
        binding.underlineToolbar.visibility = View.GONE

        onClick()


        return binding.root

    }

    private fun onClick() {
        binding.icHistory.setOnClickListener {

            val intent : Intent = Intent(requireActivity(), HistoryCameraActivity::class.java)
            intent.putExtra("key_screen", currentPage)
            startActivity(intent)

        }
        binding.rgScanner.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rbScan -> {

                    replaceFragment(CameraFragment())
                    changeBackgroundToolbar("camera")
                    currentPage = "Scan"
                    binding.underlineToolbar.visibility = View.GONE

                }

                R.id.rbDraw -> {
                    replaceFragment(PaintFragment())
                    changeBackgroundToolbar("paint")
                    currentPage = "Paint"
                    binding.underlineToolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    fun changeBackgroundToolbar(value: String) {
        when (value) {
            "camera" -> {
                binding.layoutToolbar.setBackgroundColor(resources.getColor(R.color.opacity))
                binding.icSetting.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_setting_white,null))
                binding.icTutorial.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_question_white,null))

                binding.icHistory.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_history_white, null))
            }

            "paint" -> {
                binding.layoutToolbar.setBackgroundColor(resources.getColor(R.color.bg_toolbar))

                binding.icSetting.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_setting,null))
                binding.icTutorial.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_question,null))

                binding.icHistory.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_history,null))
            }
        }
    }


    // replace fragments
    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.wrapper_fm, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }



}
