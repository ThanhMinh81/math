package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matheasyapp.model.Currency

class ConvertMoneyViewModel : ViewModel() {
    private val selectedMoneyFrom = MutableLiveData<String>()

    private val selectedMoneyTo = MutableLiveData<String>()

    private val onClickCloseFrom = MutableLiveData<String>()
    private val onClickCloseTo = MutableLiveData<String>()

//    private val listDataCurrency = MutableLiveData<List<Currency>>()
//
//    fun setListDataCurrency(value: List<Currency>){
//        this.listDataCurrency.value = value
//    }
//
//    fun getListDataCurrency() : MutableLiveData<List<Currency>>{
//        return  this.listDataCurrency
//    }

    fun setOnClickCloseFrom(value: String) {
        this.onClickCloseFrom.value = value
    }

    fun getOnClickCloseFrom(): MutableLiveData<String> {
        return this.onClickCloseFrom
    }

    fun setOnClickCloseTo(value: String) {
        this.onClickCloseTo.value = value
    }

    fun getOnClickCloseTo(): MutableLiveData<String> {
        return this.onClickCloseTo
    }


    fun getLiveDataValueMoneyFrom(): MutableLiveData<String> {
        return this.selectedMoneyFrom
    }

    fun setValueMoneyFrom(value: String) {
        this.selectedMoneyFrom.value = value
    }


    fun getLiveDataValueMoneyTo(): MutableLiveData<String> {
        return this.selectedMoneyTo
    }

    fun setValueMoneyTo(value: String) {
        this.selectedMoneyTo.value = value
    }


}
