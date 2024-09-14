package com.example.matheasyapp.bottomsheft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelSearch : ViewModel() {
    private var searchKey = MutableLiveData<String>("empty")

    fun getKeySearch() : MutableLiveData<String>{
        return this.searchKey
    }

    fun setKeyData(data: String) {
        searchKey.value = data
    }
}