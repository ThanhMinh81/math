package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaculatorViewModel  : ViewModel() {

    // MutableLiveData để cập nhật giá trị
    private val tvResult = MutableLiveData<String>()

    private val tvCalculation = MutableLiveData<String>()

    private val showTvResult = MutableLiveData<Boolean>(true)

    fun setShowTvResult(boolean: Boolean) {
        this.showTvResult.value = boolean
    }

    fun getShowTvResult(): MutableLiveData<Boolean> {
        return this.showTvResult
    }

    // Hàm để cập nhật giá trị của LiveData
    fun setValueResult(newText: String) {
        tvResult.value = newText
    }

    fun getLiveTvResult(): MutableLiveData<String> {
        return this.tvResult
    }


    // Hàm để cập nhật giá trị của LiveData
    fun setValueCal(newText: String) {
        tvCalculation.value = newText
    }

    fun getLiveTvCal(): MutableLiveData<String> {
        return this.tvCalculation
    }

}
