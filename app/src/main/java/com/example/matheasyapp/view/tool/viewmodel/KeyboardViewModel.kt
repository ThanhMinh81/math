package com.example.matheasyapp.view.tool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeyboardViewModel : ViewModel() {

    private var keyArguments = MutableLiveData<String>()

    private var principalLoanAmountValue = MutableLiveData<String>()
    private var interestValue = MutableLiveData<String>()
    private var borrowTimeValue = MutableLiveData<String>()
    private var payInterestValue = MutableLiveData<String>()

    private var valueMoneyBill = MutableLiveData<String>()
    private var valueAmountPerson = MutableLiveData<String>()
    private var valueAmountMoneyGive = MutableLiveData<String>()
    private var valuePercentMoneyGive = MutableLiveData<String>()
    private var valueAmountScot = MutableLiveData<String>()
    private var valueAmountVAT = MutableLiveData<String>()

    fun setKeyArgumentValue(value: String) {
        this.keyArguments.value = value
    }

    fun getKeyArgumentValue(): String {
        return this.keyArguments.value.toString()
    }

    // Getter và Setter cho principalLoanAmountValue
    fun getPrincipalLoanAmountValue(): MutableLiveData<String> {
        return principalLoanAmountValue
    }

    fun setPrincipalLoanAmountValue(value: String) {
        principalLoanAmountValue.value = value
    }

    // Getter và Setter cho interestValue
    fun getInterestValue(): MutableLiveData<String> {
        return interestValue
    }

    fun setInterestValue(value: String) {
        interestValue.value = value
    }

    // Getter và Setter cho borrowTimeValue
    fun getBorrowTimeValue(): MutableLiveData<String> {
        return borrowTimeValue
    }

    fun setBorrowTimeValue(value: String) {
        borrowTimeValue.value = value
    }

    // Getter và Setter cho payInterestValue
    fun getPayInterestValue(): MutableLiveData<String> {
        return payInterestValue
    }

    fun setPayInterestValue(value: String) {
        payInterestValue.value = value
    }

    // Getter và Setter cho valueMoneyBill
    fun getValueMoneyBill(): MutableLiveData<String> {
        return valueMoneyBill
    }

    fun setValueMoneyBill(value: String) {
        valueMoneyBill.value = value
    }

    // Getter và Setter cho valueAmountPerson
    fun getValueAmountPerson(): MutableLiveData<String> {
        return valueAmountPerson
    }

    fun setValueAmountPerson(value: String) {
        valueAmountPerson.value = value
    }

    // Getter và Setter cho valueAmountMoneyGive
    fun getValueAmountMoneyGive(): MutableLiveData<String> {
        return valueAmountMoneyGive
    }

    fun setValueAmountMoneyGive(value: String) {
        valueAmountMoneyGive.value = value
    }

    // Getter và Setter cho valuePercentMoneyGive
    fun getValuePercentMoneyGive(): MutableLiveData<String> {
        return valuePercentMoneyGive
    }

    fun setValuePercentMoneyGive(value: String) {
        valuePercentMoneyGive.value = value
    }

    // Getter và Setter cho valueAmountScot
    fun getValueAmountScot(): MutableLiveData<String> {
        return valueAmountScot
    }

    fun setValueAmountScot(value: String) {
        valueAmountScot.value = value
    }

    // Getter và Setter cho valueAmountVAT
    fun getValueAmountVAT(): MutableLiveData<String> {
        return valueAmountVAT
    }

    fun setValueAmountVAT(value: String) {
        valueAmountVAT.value = value
    }
}