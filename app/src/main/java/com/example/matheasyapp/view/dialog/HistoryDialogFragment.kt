package com.example.matheasyapp.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.DialogDeleteHistoryBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.livedata.HistoryViewModel
import com.example.matheasyapp.model.History

class HistoryDialogFragment : DialogFragment() {

    private lateinit var btnDele: Button
    private lateinit var btnCancle: Button

    private lateinit var database: HistoryDatabase

    private lateinit var listener: OnDeleteListener

    private lateinit var liveData: HistoryViewModel

    interface OnDeleteListener {
        fun onDelete( )
    }

//    companion object {
//        private const val ARG_LIST_DATA = "listResult"
//
//        fun newInstance(listData: ArrayList<History>): HistoryDialogFragment {
//            val fragment = HistoryDialogFragment()
//            val args = Bundle()
//            args.putParcelableArrayList("listResult", listData)
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun onAttach(context: Context) {
        listener = requireContext() as OnDeleteListener
        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = HistoryDatabase.getDatabase(requireContext())
        liveData = ViewModelProvider(requireActivity()).get(HistoryViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_delete_history, null)

        btnDele = dialogView.findViewById(R.id.btnDele)
        btnCancle = dialogView.findViewById(R.id.btnCancle)

        builder.setView(dialogView)

        val alertDialog = builder.create()

        // cancle
        btnCancle.setOnClickListener {
            alertDialog.dismiss()
        }

        //clear
        btnDele.setOnClickListener {

            val list: List<History> =  liveData.getValueList().toList()
            database.historyDao().deleteHistories(list)
            listener.onDelete()

            alertDialog.dismiss()
        }

        return alertDialog
    }


    fun setOnDeleteListener(listener: OnDeleteListener) {
        this.listener = listener
    }

}