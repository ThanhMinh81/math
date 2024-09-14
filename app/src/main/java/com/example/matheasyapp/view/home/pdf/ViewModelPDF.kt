package com.example.matheasyapp.view.home.pdf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matheasyapp.model.PdfFile

class ViewModelPDF : ViewModel() {

    var shareMode: MutableLiveData<String> = MutableLiveData<String>()

    var shareListPDF: MutableLiveData<List<PdfFile>> = MutableLiveData<List<PdfFile>>()

    var shareListPDFStart: MutableLiveData<ArrayList<PdfFile>> =
        MutableLiveData<ArrayList<PdfFile>>()

    var valueSearch: MutableLiveData<String> = MutableLiveData<String>()

    var valueModeSort: MutableLiveData<String> = MutableLiveData<String>()

    var scanPathPDF: MutableLiveData<PdfFile> = MutableLiveData()

    fun setValuePDFScan(pdf: PdfFile?) {
        this.scanPathPDF.value = pdf
    }

    fun getValuePDFScan(): MutableLiveData<PdfFile> {
        return this.scanPathPDF
    }


    fun setValueModeSort(value: String) {
        this.valueModeSort.value = value
    }

    fun getValueSort(): MutableLiveData<String> {
        return this.valueModeSort
    }

    fun setShareMode(value: String) {

        shareMode.value = value
    }

    fun getValue(): MutableLiveData<String> {
        return this.shareMode
    }

    fun setShareListPDF(value: List<PdfFile>) {
        this.shareListPDF.value = value
    }

    fun getListPdfShaed(): MutableLiveData<List<PdfFile>> {
        return this.shareListPDF
    }

    fun setListStart(value: ArrayList<PdfFile>) {
        this.shareListPDFStart.value = value
    }

    fun getListStart(): MutableLiveData<ArrayList<PdfFile>> {
        return this.shareListPDFStart
    }

    fun getSearchKeyWord(): MutableLiveData<String> {
        return this.valueSearch
    }

    fun setSearchKeyWord(value: String) {
        this.valueSearch.value = value
    }


}
