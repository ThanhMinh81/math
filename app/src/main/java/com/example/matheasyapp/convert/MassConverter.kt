package com.example.matheasyapp.convert

import java.math.BigDecimal
import java.math.RoundingMode

class MassConverter(private val from: Unit, private val to: Unit) {

    enum class Unit(val symbol: String) {
        Microgram("mcg"),
        Milligram("mg"),
        Gram("g"),
        Kilogram("kg"),
        MetricTonne("mt"),
        Ounce("oz"),
        Pound("lb"),
        Ton("t");

        companion object {
            fun fromString(symbol: String): Unit {
                for (unit in values()) {
                    if (unit.symbol.equals(symbol, ignoreCase = true)) {
                        return unit
                    }
                }
                throw IllegalArgumentException("Cannot find a value for $symbol")
            }
        }
    }

    private val multiplier: Double

    init {
        multiplier = when (from) {
            Unit.Microgram -> when (to) {
                Unit.Milligram -> 0.001
                Unit.Gram -> 1e-6
                Unit.Kilogram -> 1e-9
                Unit.MetricTonne -> 1e-12
                Unit.Ounce -> 3.5274e-8
                Unit.Pound -> 2.2046e-9
                Unit.Ton -> 1.1023e-9
                else -> 1.0
            }
            Unit.Milligram -> when (to) {
                Unit.Microgram -> 1000.0
                Unit.Gram -> 0.001
                Unit.Kilogram -> 1e-6
                Unit.MetricTonne -> 1e-9
                Unit.Ounce -> 3.5274e-5
                Unit.Pound -> 2.2046e-6
                Unit.Ton -> 1.1023e-6
                else -> 1.0
            }
            Unit.Gram -> when (to) {
                Unit.Microgram -> 1e6
                Unit.Milligram -> 1000.0
                Unit.Kilogram -> 0.001
                Unit.MetricTonne -> 1e-6
                Unit.Ounce -> 0.035274
                Unit.Pound -> 0.00220462
                Unit.Ton -> 1.1023e-6
                else -> 1.0
            }
            Unit.Kilogram -> when (to) {
                Unit.Microgram -> 1e9
                Unit.Milligram -> 1e6
                Unit.Gram -> 1000.0
                Unit.MetricTonne -> 0.001
                Unit.Ounce -> 35.274
                Unit.Pound -> 2.20462
                Unit.Ton -> 1.1023e-3
                else -> 1.0
            }
            Unit.MetricTonne -> when (to) {
                Unit.Microgram -> 1e12
                Unit.Milligram -> 1e9
                Unit.Gram -> 1e6
                Unit.Kilogram -> 1000.0
                Unit.Ounce -> 35274.0
                Unit.Pound -> 2204.62
                Unit.Ton -> 1.0
                else -> 1.0
            }
            Unit.Ounce -> when (to) {
                Unit.Microgram -> 2.8225e7
                Unit.Milligram -> 28349.5
                Unit.Gram -> 28.3495
                Unit.Kilogram -> 0.0283495
                Unit.MetricTonne -> 2.83495e-5
                Unit.Pound -> 0.0625
                Unit.Ton -> 2.83495e-5
                else -> 1.0
            }
            Unit.Pound -> when (to) {
                Unit.Microgram -> 4.5359e9
                Unit.Milligram -> 453592.37
                Unit.Gram -> 453.59237
                Unit.Kilogram -> 0.45359237
                Unit.MetricTonne -> 0.00045359237
                Unit.Ounce -> 16.0
                Unit.Ton -> 0.00045359237
                else -> 1.0
            }
            Unit.Ton -> when (to) {
                Unit.Microgram -> 1.0e12
                Unit.Milligram -> 1.0e9
                Unit.Gram -> 1.0e6
                Unit.Kilogram -> 1.0e3
                Unit.MetricTonne -> 1.0
                Unit.Ounce -> 35274.0
                Unit.Pound -> 2204.62
                else -> 1.0
            }
        }
    }

    fun convert(value: BigDecimal): BigDecimal {
        val result = value * multiplier.toBigDecimal()
        return result
    }


    fun provideUnitExample(): String {
        val exampleValue = BigDecimal(1)
        val convertedValue = convert(exampleValue)
        return "1 ${from.symbol} = $convertedValue ${to.symbol}"
    }

//    fun roundValue(value: BigDecimal): BigDecimal {
//        val threshold = BigDecimal("0.0001")
//        val fractionalPart = value.remainder(BigDecimal.ONE)
//
//        return if (fractionalPart <= threshold) {
//            value.setScale(0, RoundingMode.DOWN)
//        } else {
//            value.setScale(0, RoundingMode.HALF_UP)
//        }
//    }

}
