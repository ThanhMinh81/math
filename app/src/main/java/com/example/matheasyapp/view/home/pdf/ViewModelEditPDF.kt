package com.example.matheasyapp.view.home.pdf

import androidx.camera.core.impl.MutableTagBundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matheasyapp.model.PdfFile

class ViewModelEditPDF : ViewModel() {

    var readName: MutableLiveData<PdfFile?> = MutableLiveData<PdfFile?>()
    var delete: MutableLiveData<PdfFile?> = MutableLiveData<PdfFile?>()
    var share: MutableLiveData<String> = MutableLiveData<String>()
    var start: MutableLiveData<PdfFile?> = MutableLiveData<PdfFile?>()

    fun setStartPDF(pdf: PdfFile?) {
        this.start.value = pdf
    }

    fun getStartPDF(): MutableLiveData<PdfFile?> {
        return this.start
    }

    fun setReadName(pdf: PdfFile?) {
        this.readName.value = pdf
    }

    fun getReadNameFile(): MutableLiveData<PdfFile?> {
        return this.readName
    }

    fun setDeleteFile(value: PdfFile?) {
        this.delete.value = value
    }

    fun getDeleteFile(): MutableLiveData<PdfFile?> {
        return this.delete
    }


    fun setShareFile(value: String) {
        this.share.value = value
    }

    fun getShareFile(): MutableLiveData<String> {
        return this.share
    }


}