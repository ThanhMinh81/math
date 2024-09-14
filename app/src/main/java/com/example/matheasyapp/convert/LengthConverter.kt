package com.example.matheasyapp.view.calculate

import java.math.BigDecimal
import java.math.RoundingMode


class LengthConverter(private val from: Unit, private val to: Unit) {

    enum class Unit(val symbol: String) {
        Millimeter("mm"), Centimeter("cm"), Meter("m"), Kilometer("km"), Inch("in"), Yard("yd"), USSurveyFoot(
            "ft-us"),
        Foot("ft"), Mile("mi");

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
            Unit.Millimeter -> when (to) {
                Unit.Centimeter -> 0.1
                Unit.Meter -> 0.001
                Unit.Kilometer -> 1e-6
                Unit.Inch -> 0.0393701
                Unit.Yard -> 0.00109361
                Unit.USSurveyFoot -> 0.00328084
                Unit.Foot -> 0.00328084
                Unit.Mile -> 6.2137e-7
                else -> 1.0
            }

            Unit.Centimeter -> when (to) {
                Unit.Millimeter -> 10.0
                Unit.Meter -> 0.01
                Unit.Kilometer -> 1e-5
                Unit.Inch -> 0.393701
                Unit.Yard -> 0.0109361
                Unit.USSurveyFoot -> 0.0328084
                Unit.Foot -> 0.0328084
                Unit.Mile -> 6.2137e-6
                else -> 1.0
            }

            Unit.Meter -> when (to) {
                Unit.Millimeter -> 1000.0
                Unit.Centimeter -> 100.0
                Unit.Kilometer -> 0.001
                Unit.Inch -> 39.3701
                Unit.Yard -> 1.09361
                Unit.USSurveyFoot -> 3.28084
                Unit.Foot -> 3.28084
                Unit.Mile -> 0.000621371
                else -> 1.0
            }

            Unit.Kilometer -> when (to) {
                Unit.Millimeter -> 1e6
                Unit.Centimeter -> 100000.0
                Unit.Meter -> 1000.0
                Unit.Inch -> 39370.1
                Unit.Yard -> 1093.61
                Unit.USSurveyFoot -> 3280.84
                Unit.Foot -> 3280.84
                Unit.Mile -> 0.621371
                else -> 1.0
            }

            Unit.Inch -> when (to) {
                Unit.Millimeter -> 25.4
                Unit.Centimeter -> 2.54
                Unit.Meter -> 0.0254
                Unit.Kilometer -> 2.54e-5
                Unit.Yard -> 0.0277778
                Unit.USSurveyFoot -> 0.0833333
                Unit.Foot -> 0.0833333
                Unit.Mile -> 1.5783e-5
                else -> 1.0
            }

            Unit.Yard -> when (to) {
                Unit.Millimeter -> 914.4
                Unit.Centimeter -> 91.44
                Unit.Meter -> 0.9144
                Unit.Kilometer -> 0.0009144
                Unit.Inch -> 36.0
                Unit.USSurveyFoot -> 3.0
                Unit.Foot -> 3.0
                Unit.Mile -> 0.000568182
                else -> 1.0
            }

            Unit.USSurveyFoot -> when (to) {
                Unit.Millimeter -> 304.8
                Unit.Centimeter -> 30.48
                Unit.Meter -> 0.3048
                Unit.Kilometer -> 0.0003048
                Unit.Inch -> 12.0
                Unit.Yard -> 0.333333
                Unit.Foot -> 1.0
                Unit.Mile -> 0.000189394
                else -> 1.0
            }

            Unit.Foot -> when (to) {
                Unit.Millimeter -> 304.8
                Unit.Centimeter -> 30.48
                Unit.Meter -> 0.3048
                Unit.Kilometer -> 0.0003048
                Unit.Inch -> 12.0
                Unit.Yard -> 0.333333
                Unit.USSurveyFoot -> 1.0
                Unit.Mile -> 0.000189394
                else -> 1.0
            }

            Unit.Mile -> when (to) {
                Unit.Millimeter -> 1.609e6
                Unit.Centimeter -> 160934.0
                Unit.Meter -> 1609.34
                Unit.Kilometer -> 1.60934
                Unit.Inch -> 63360.0
                Unit.Yard -> 1760.0
                Unit.USSurveyFoot -> 5280.0
                Unit.Foot -> 5280.0
                else -> 1.0
            }
        }
    }

    fun convert(value: BigDecimal): String {
        val result = value * multiplier.toBigDecimal()
        return convertBigDecimalToFull(result)
    }

    fun convertBigDecimalToFull(number: BigDecimal): String {
        return number.toPlainString()
    }

    fun provideUnitExample(): String {
        val exampleValue = BigDecimal(1)
        val convertedValue = convert(exampleValue)
        return "1 ${from.symbol} = $convertedValue ${to.symbol}"
    }


}