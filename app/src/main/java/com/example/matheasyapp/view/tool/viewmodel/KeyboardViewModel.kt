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

    // cost

    private var  valueTax = MutableLiveData<String>()
    private var valueCost = MutableLiveData<String>()


    // material
    private var valueDistance  = MutableLiveData<String>()
    private var valueMaterial = MutableLiveData<String>()
    private var valueGas = MutableLiveData<String>()


    // save money
    private var valueMoneyLevel = MutableLiveData<String>()
    private  var  valueInterestSave= MutableLiveData<String>()
    private var valueMoneyBillSave = MutableLiveData<String>()
    private  var  valueTaxInterest = MutableLiveData<String>()


    // Unit Price
    private var valuePriceA = MutableLiveData<String>()
    private  var  valueQuangityA= MutableLiveData<String>()
    private var valueUnitA = MutableLiveData<String>()

    private var valuePriceB = MutableLiveData<String>()
    private  var  valueQuangityB= MutableLiveData<String>()
    private var valueUnitB = MutableLiveData<String>()

    private var valuePriceC = MutableLiveData<String>()
    private  var  valueQuangityC = MutableLiveData<String>()
    private var valueUnitC = MutableLiveData<String>()

    private var valuePriceD = MutableLiveData<String>()
    private  var  valueQuangityD = MutableLiveData<String>()
    private var valueUnitD = MutableLiveData<String>()


    // Giá trị cho A
    fun setValuePriceA(value: String) {
        valuePriceA.value = value
    }

    fun getValuePriceA(): MutableLiveData<String> {
        return valuePriceA
    }

    fun setValueQuantityA(value: String) {
        valueQuangityA.value = value
    }

    fun getValueQuantityA(): MutableLiveData<String> {
        return valueQuangityA
    }

    fun setValueUnitA(value: String) {
        valueUnitA.value = value
    }

    fun getValueUnitA(): MutableLiveData<String> {
        return valueUnitA
    }

    // Giá trị cho B
    fun setValuePriceB(value: String) {
        valuePriceB.value = value
    }

    fun getValuePriceB(): MutableLiveData<String> {
        return valuePriceB
    }

    fun setValueQuantityB(value: String) {
        valueQuangityB.value = value
    }

    fun getValueQuantityB(): MutableLiveData<String> {
        return valueQuangityB
    }

    fun setValueUnitB(value: String) {
        valueUnitB.value = value
    }

    fun getValueUnitB(): MutableLiveData<String> {
        return valueUnitB
    }

    // Giá trị cho C
    fun setValuePriceC(value: String) {
        valuePriceC.value = value
    }

    fun getValuePriceC(): MutableLiveData<String> {
        return valuePriceC
    }

    fun setValueQuantityC(value: String) {
        valueQuangityC.value = value
    }

    fun getValueQuantityC(): MutableLiveData<String> {
        return valueQuangityC
    }

    fun setValueUnitC(value: String) {
        valueUnitC.value = value
    }

    fun getValueUnitC(): MutableLiveData<String> {
        return valueUnitC
    }

    // Giá trị cho D
    fun setValuePriceD(value: String) {
        valuePriceD.value = value
    }

    fun getValuePriceD(): MutableLiveData<String> {
        return valuePriceD
    }

    fun setValueQuantityD(value: String) {
        valueQuangityD.value = value
    }

    fun getValueQuantityD(): MutableLiveData<String> {
        return valueQuangityD
    }

    fun setValueUnitD(value: String) {
        valueUnitD.value = value
    }

    fun getValueUnitD(): MutableLiveData<String> {
        return valueUnitD
    }




    // Getter và Setter cho valueMoneyLevel
    fun setValueMoneyLevel(value: String) {
        this.valueMoneyLevel.value = value
    }

    fun getValueMoneyLevel(): MutableLiveData<String> {
        return valueMoneyLevel
    }


    // Getter và Setter cho valueMoneyBillSave
    fun setValueMoneyBillSave(value: String) {
        this.valueMoneyBillSave.value = value
    }

    fun getValueMoneyBillSave(): MutableLiveData<String> {
        return valueMoneyBillSave
    }

    // Getter và Setter cho valueTaxInterest
    fun setValueTaxInterest(value: String) {
        this.valueTaxInterest.value = value
    }

    fun getValueTaxInterest(): MutableLiveData<String> {
        return valueTaxInterest
    }


    fun setValueDistance(value : String){
        this.valueDistance.value = value
    }
    fun getValueDistance() : MutableLiveData<String>{
        return valueDistance
    }

    fun setValueMaterial(value: String){
        this.valueMaterial.value = value
    }

    fun getValueMaterial() : MutableLiveData<String>{
        return  valueMaterial
    }

    fun setValueGas(value: String){
        this.valueGas.value = value
    }

    fun getValueGas() : MutableLiveData<String>{
        return  valueGas
    }


    fun setValueCost(value : String){
        this.valueCost.value = value
    }
    fun getValueCost() : MutableLiveData<String>{
        return valueCost
    }

    fun setValueTax(value : String){
        this.valueTax.value = value
    }
    fun getValueTaxt() : MutableLiveData<String>{
        return valueTax
    }


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