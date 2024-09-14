package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConvertViewModel : ViewModel() {

    private  var valueInit : MutableLiveData<String> = MutableLiveData()

    fun getValueInit() : MutableLiveData<String>{
        return this.valueInit
    }

    fun setValueInit(text : String)
    {
        this.valueInit.value = text
    }

}