package com.example.matheasyapp.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelToolbar : ViewModel() {

    private val tagCurrent = MutableLiveData<String>()

    fun getCurrentTag(): MutableLiveData<String> {
        return this.tagCurrent
    }

    fun setCurrentTag(value: String) {
        this.tagCurrent.value = value
    }

}