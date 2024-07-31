package com.example.matheasyapp.itf

import com.example.matheasyapp.view.tool.model.LoanHistory



interface OnclickPaymentHistory {

    fun onClickPaymentHistory(
        item: LoanHistory, boolean: Boolean,
        sizeList: Int
    )

}