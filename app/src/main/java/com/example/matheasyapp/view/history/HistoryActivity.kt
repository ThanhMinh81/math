package com.example.matheasyapp.view.history

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.AdapterHistory
import com.example.matheasyapp.databinding.ActivityHistoryBinding
import com.example.matheasyapp.databinding.LayoutEmptyHistoryBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.itf.HistoryItemClickListener
import com.example.matheasyapp.livedata.HistoryViewModel
import com.example.matheasyapp.model.History
import com.example.matheasyapp.view.dialog.HistoryDialogFragment.OnDeleteListener


class HistoryActivity : AppCompatActivity(), HistoryItemClickListener, OnDeleteListener {

    lateinit var binding: ActivityHistoryBinding

    lateinit var adapterHistory: AdapterHistory

    lateinit var historyList: ArrayList<History>

    private lateinit var database: HistoryDatabase

    var checkSelectedAll: Boolean = false

    var itemHistoryUnCheck: History? = null

    private lateinit var historyLiveData: HistoryViewModel

    lateinit var bindingEmpty: LayoutEmptyHistoryBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        database = HistoryDatabase.getDatabase(this)

        getData();

    }

    private fun getData() {
        val list = database.historyDao().allHistory

        if (list.size > 0) {

            binding = ActivityHistoryBinding.inflate(layoutInflater)

            historyLiveData = ViewModelProvider(this).get(HistoryViewModel::class.java)

            historyList = ArrayList();
            binding.rcvHistory.layoutManager = LinearLayoutManager(this)

            adapterHistory = AdapterHistory(historyList, this)
            adapterHistory.notifyDataSetChanged()
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
                    compoundButton.setText("Deselect all")
                    setEnableButton(adapterHistory.getListChecked().size)


                } else {

                    compoundButton.setText("Select All")

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

    override fun onItemClick(item: History, boolean: Boolean, sizeList: Int) {

        if (boolean) {
            // checkItem

            adapterHistory.addCheckItem(item)
            adapterHistory.notifyDataSetChanged()

            setEnableButton(adapterHistory.getListChecked().size)

            if ((sizeList + 1) == historyList.size) {
                binding.cbSwitchMode.isChecked = true
                binding.cbSwitchMode.setText("Deselect all")
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

    fun setEnableButton(size: Int) {
        if (size > 0) {
            binding.btnRemove.alpha = 1f
            binding.btnRemove.isEnabled = true
        } else {
            binding.btnRemove.alpha = 0.5f
            binding.btnRemove.isEnabled = false
        }

    }


    //  dialog callback
    override fun onDelete() {
        getData()
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

            val list: List<History> = adapterHistory.getListChecked().toList()
            database.historyDao().deleteHistories(list)
            getData()
            dialog.dismiss()
        }

// Set width và height của dialog là match_parent
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialog.window?.attributes = layoutParams
        dialog.show()


    }


}
