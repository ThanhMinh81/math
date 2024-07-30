package com.example.matheasyapp.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matheasyapp.model.History

class HistoryViewModel : ViewModel() {

    // MutableLiveData để cho phép cập nhật giá trị
    private val liveData: MutableLiveData<ArrayList<History>> = MutableLiveData()

    // Hàm để lấy giá trị của liveData
    fun getValue(): MutableLiveData<ArrayList<History>> {
        return liveData
    }

    fun getValueList() : ArrayList<History> {
        return this.liveData.value!!
    }

    // Hàm để đặt giá trị của liveData
    fun setValue(value: ArrayList<History>) {
        liveData.value = value
    }

    // Hàm để thêm một phần tử vào liveData
//    fun addHistory(history: History) {
//        val currentList = liveData.value ?: arrayListOf()
//        currentList.add(history)
//        liveData.value = currentList
//    }

}
