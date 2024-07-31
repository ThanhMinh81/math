package com.example.matheasyapp.itf

import com.example.matheasyapp.model.History

interface HistoryItemClickListener {
    fun onItemClick(item: History, boolean: Boolean, sizeList: Int)

}