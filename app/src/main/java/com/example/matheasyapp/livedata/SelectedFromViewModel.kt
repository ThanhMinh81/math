package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedFromViewModel : ViewModel() {

    private val selectedFrom = MutableLiveData<String>()

    private val selectedTo = MutableLiveData<String>()

    private val selectedOnClick = MutableLiveData<String>()

    private  val  setOnClickCloseBottomTo = MutableLiveData<String>()

    fun getOnClickCloseDialogTo() : MutableLiveData<String> {
        return this.setOnClickCloseBottomTo
    }

    fun setOnClickCloseDialogTo(value : String) {
        this.setOnClickCloseBottomTo.value = value
    }

    fun getSelectedCloseDialogTo() : MutableLiveData<String>{
        return this.selectedOnClick
    }

    fun setSelectedCloseDialogTo(value : String) {
        this.selectedOnClick.value = value
    }

    fun getLiveDataValueFrom() : MutableLiveData<String>{
        return  this.selectedFrom
    }

    fun setValueFrom(value : String) {
        this.selectedFrom.value = value
    }


    fun getLiveDataValueTo() : MutableLiveData<String> {
        return this.selectedTo
    }

    fun setValueTo(value : String) {
        this.selectedTo.value = value
    }

}