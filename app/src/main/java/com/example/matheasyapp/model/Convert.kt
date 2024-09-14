package com.example.matheasyapp.model

class Convert  {

    var check : Boolean = false
    lateinit var name :  String
    lateinit var symbol : String
    var factorToBase : Double = 0.0
    lateinit var type : String

    constructor(name : String , symbol: String, factorToBase: Double)
    {
        this.name = name
        this.check = check
        this.factorToBase = factorToBase
    }

    constructor(name : String , symbol: String, check: Boolean , type : String)
    {
        this.name = name
        this.check = check
        this.symbol = symbol

        this.type = type
    }

    constructor(name : String , symbol: String, check: Boolean)
    {
        this.name = name
        this.check = check
        this.symbol = symbol

    }

}