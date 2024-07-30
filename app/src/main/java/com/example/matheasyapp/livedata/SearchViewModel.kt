package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private var searchValue : MutableLiveData<String>  = MutableLiveData()

    fun getSearchValue() : MutableLiveData<String> {
        return this.searchValue
    }

    fun setSearchValue(value : String) {
        this.searchValue.value = value
    }

}