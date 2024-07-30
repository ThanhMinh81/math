package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetectVisibleViewModel : ViewModel() {

    private var isVisible = MutableLiveData<Boolean>()

    fun getIsVisible(): MutableLiveData<Boolean> {
        return this.isVisible
    }

    fun setIsVisible(boolean: Boolean) {
        this.isVisible.value = boolean
    }

}