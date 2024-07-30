package com.example.matheasyapp.view.tool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeyboardViewModel : ViewModel() {


    private var keyArguments = MutableLiveData<String>()

    // Các biến MutableLiveData để lưu trữ các giá trị
    private var principalLoanAmountValue = MutableLiveData<String>()
    private var interestValue = MutableLiveData<String>()
    private var borrowTimeValue = MutableLiveData<String>()
    private var payInterestValue = MutableLiveData<String>()

    fun setKeyArgumentValue(value: String) {
        this.keyArguments.value = value
    }

    fun getKeyArgumentValue(): String {
        return this.keyArguments.value.toString()
    }

    // Getter cho principalLoanAmountValue
    fun getPrincipalLoanAmountValue(): MutableLiveData<String> {
        return principalLoanAmountValue
    }

    // Setter cho principalLoanAmountValue
    fun setPrincipalLoanAmountValue(value: String) {
        principalLoanAmountValue.value = value
    }

    // Getter cho interestValue
    fun getInterestValue(): MutableLiveData<String> {
        return interestValue
    }

    // Setter cho interestValue
    fun setInterestValue(value: String) {
        interestValue.value = value
    }

    // Getter cho borrowTimeValue
    fun getBorrowTimeValue(): MutableLiveData<String> {
        return borrowTimeValue
    }

    // Setter cho borrowTimeValue
    fun setBorrowTimeValue(value: String) {
        borrowTimeValue.value = value
    }

    // Getter cho payInterestValue
    fun getPayInterestValue(): MutableLiveData<String> {
        return payInterestValue
    }

    // Setter cho payInterestValue
    fun setPayInterestValue(value: String) {
        payInterestValue.value = value
    }
}
