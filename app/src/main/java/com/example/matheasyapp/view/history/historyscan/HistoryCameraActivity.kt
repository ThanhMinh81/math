package com.example.matheasyapp.view.history.historyscan

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityHistoryCameraBinding
import com.example.matheasyapp.databinding.LayoutEmptyHistoryBinding
import com.example.matheasyapp.db.CameraHistory
import com.example.matheasyapp.db.HistoryDatabase
import kotlinx.coroutines.launch


class HistoryCameraActivity : AppCompatActivity() ,EventOnClickHistory {

    private lateinit var binding: ActivityHistoryCameraBinding

    lateinit var adapterHistory: AdapterHistoryCamera

    lateinit var historyList: ArrayList<CameraHistory>

    private lateinit var database: HistoryDatabase

    var checkSelectedAll: Boolean = false

    var itemHistoryUnCheck: CameraHistory? = null

//    private lateinit var historyLiveData: HistoryCameraViewModel

    private lateinit var bindingEmpty: LayoutEmptyHistoryBinding

    private var currentPage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityHistoryCameraBinding.inflate(layoutInflater)

        database = HistoryDatabase.getDatabase(this)


        getData();

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {

            try {
                val list = database.cameraHistoryDao().getAllLoanHistory()

                Log.d("5352345234",list.size.toString())

                if (list.isNotEmpty()) {

                    binding = ActivityHistoryCameraBinding.inflate(layoutInflater)
                    binding.rcvHistory.visibility = View.VISIBLE

//            historyLiveData = ViewModelProvider(this).get(HistoryCameraViewModel::class.java)
                    historyList = ArrayList();
                    binding.rcvHistory.layoutManager = LinearLayoutManager(this)

                    adapterHistory = AdapterHistoryCamera(historyList, this)
                    binding.rcvHistory.adapter = adapterHistory
                    historyList.addAll(list)
                    adapterHistory.notifyDataSetChanged()

                    binding.cbSwitchMode.visibility = View.GONE
                    binding.bottomNavHistory.visibility = View.GONE

                    eventClick();

                    binding.btnCancle.setOnClickListener {

                        binding.icTrash.visibility = View.VISIBLE
                        binding.cbSwitchMode.visibility = View.GONE
                        binding.bottomNavHistory.visibility = View.GONE

                        setEnableButton(0)

                        adapterHistory.clearAllItemChecked()
                        adapterHistory.notifyDataSetChanged()
                        adapterHistory.showCheckbox(false)

                    }
                    binding.cbSwitchMode.setOnCheckedChangeListener { compoundButton, b ->

                        compoundButton as CheckBox;

                        if (b) {
                            // selected all checkbox
                            adapterHistory.clearAllItemChecked()
                            adapterHistory.selectedAllItemChecked()
                            adapterHistory.notifyDataSetChanged()
                            compoundButton.setText("Bỏ chọn tất cả")
                            setEnableButton(adapterHistory.getListChecked().size)


                        } else {

                            compoundButton.setText("Chọn tất cả")

                            if (checkSelectedAll) {
                                // unCheck all

                                adapterHistory.clearAllItemChecked()
                                adapterHistory.notifyDataSetChanged()
                                setEnableButton(adapterHistory.getListChecked().size)


                            } else {
                                // uncheck Item

                                adapterHistory.removeCheckItem(itemHistoryUnCheck)
                                adapterHistory.notifyDataSetChanged()
                                setEnableButton(adapterHistory.getListChecked().size)

                            }

                        }

                    }

                    setContentView(binding.root)
                } else {
                    bindingEmpty = LayoutEmptyHistoryBinding.inflate(layoutInflater)

                    bindingEmpty.icBack.setOnClickListener {
                        onBackPressed()
                        finish()
                    }
                    bindingEmpty.btnAddHistory.setOnClickListener {
                        onBackPressed()
                        finish()
                    }

                    setContentView(bindingEmpty.root)
                }



            } catch (e: Exception) {

                Log.d("5352345234 sdfsf",e.toString())

                e.printStackTrace()
                // Xử lý lỗi

            }



    }


    private fun eventClick() {

        binding.icTrash.setOnClickListener {
            binding.icTrash.visibility = View.GONE
            binding.cbSwitchMode.visibility = View.VISIBLE
            binding.bottomNavHistory.visibility = View.VISIBLE

            setEnableButton(0)

            binding.cbSwitchMode.isChecked = false;

            adapterHistory.showCheckbox(true)
            adapterHistory.clearAllItemChecked()
            adapterHistory.notifyDataSetChanged()

        }

        binding.btnRemove.setOnClickListener {

            showInputDialog()


//            val dialogFragment = HistoryDialogFragment()
//            historyLiveData.setValue(adapterHistory.getListChecked())
//            dialogFragment.show(supportFragmentManager, "DialogFragment")
        }

        binding.icBack.setOnClickListener {

            if (binding.cbSwitchMode.visibility == View.VISIBLE) {
                // an cac view
                binding.cbSwitchMode.visibility = View.GONE
                binding.bottomNavHistory.visibility = View.GONE
                binding.icTrash.visibility = View.VISIBLE
                adapterHistory.clearAllItemChecked()
                adapterHistory.showCheckbox(false)
                adapterHistory.notifyDataSetChanged()
            } else {

                adapterHistory.clearAllItemChecked()
                adapterHistory.notifyDataSetChanged()
                onBackPressed()

            }
        }

        binding.cbSwitchMode.setOnClickListener {
            checkSelectedAll = true
        }

    }

    fun setEnableButton(size: Int) {
        if (size > 0) {
            binding.btnRemove.alpha = 1f
            binding.btnRemove.isEnabled = true
        } else {
            binding.btnRemove.alpha = 0.5f
            binding.btnRemove.isEnabled = false
        }
    }

    fun showInputDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_confirm_history)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancle: Button = dialog.findViewById(R.id.btnCancle)
        val btnOk: Button = dialog.findViewById(R.id.btnOk)

        btnCancle.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {

            val list: List<CameraHistory> = adapterHistory.getListChecked()

            database.cameraHistoryDao().deleteList(list)
            getData()
            dialog.dismiss()

        }

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialog.window?.attributes = layoutParams
        dialog.show()


    }



    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    override fun onClickItem(item: CameraHistory, boolean: Boolean, sizeList: Int) {

        if (boolean) {
            // checkItem

            adapterHistory.addCheckItem(item)
            adapterHistory.notifyDataSetChanged()

            setEnableButton(adapterHistory.getListChecked().size)

            if ((sizeList + 1) == historyList.size) {
                binding.cbSwitchMode.isChecked = true
                binding.cbSwitchMode.setText("Bỏ chọn tất cả")
            }

        } else {

            // selected all

            itemHistoryUnCheck = item

            if (binding.cbSwitchMode.isChecked) {
                checkSelectedAll = false
                binding.cbSwitchMode.isChecked = false

                setEnableButton(adapterHistory.getListChecked().size)

            } else {

                adapterHistory.removeCheckItem(item)
                adapterHistory.notifyDataSetChanged()

                setEnableButton(adapterHistory.getListChecked().size)
            }

        }
    }

}

