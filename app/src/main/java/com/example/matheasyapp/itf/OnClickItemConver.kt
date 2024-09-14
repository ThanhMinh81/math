package com.example.matheasyapp.itf

import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency

interface OnClickItemConver {

    fun onClickCurrency(item : Currency)
    fun onClick(item:Convert)

}