package com.example.matheasyapp.view.dialog

import android.app.ProgressDialog
import android.content.Context


class ProgressDialogManager {
    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(context: Context?) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage("Loading...")
        }
        progressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }
}