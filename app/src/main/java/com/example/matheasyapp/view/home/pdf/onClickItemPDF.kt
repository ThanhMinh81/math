package com.example.matheasyapp.view.home.pdf

import com.example.matheasyapp.model.PdfFile

interface onClickItemPDF {

    fun onClickItem(item: PdfFile)

    fun onClickItemStart(item: PdfFile, start: Boolean)

    fun onClickItemEditPDF(item: PdfFile)

}