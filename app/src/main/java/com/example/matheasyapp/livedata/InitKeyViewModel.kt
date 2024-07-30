package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InitKeyViewModel : ViewModel() {
     private var initKey : MutableLiveData<String> = MutableLiveData()

    fun getInitKey() : MutableLiveData<String> {
        return  this.initKey
    }
    fun setInitKey(key : String) {
        this.initKey.value = key
    }


}