package com.example.matheasyapp.convert

import java.math.BigDecimal

class VolumeConverter(private val from: Unit, private val to: Unit) {

    enum class Unit(val symbol: String) {

        Milliliter("ml"),
        Centiliter("cl"),
        Deciliter("dl"),
        Litre("l"),
        Hectoliter("hl"),
        Kiloliter("kl"),
        CubicMillimeter("mm3"),
        CubicCentimeter("cm3"),
        CubicDecimeter("dm3"),
        CubicMeter("m3"),
        CubicInch("in3"),
        CubicFoot("ft3"),
        CubicYard("yd3"),
        Gallon("gal"),
        LiquidQuart("qt"),
        Panh("pt"),
        Cup("cup"),
        FluidOunce("fl-oz"),
        Tablespoon("tbsp"),
        Teaspoon("tsp"),
        OilBarrel("bbl");

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
            Unit.Milliliter -> when (to) {
                Unit.Centiliter -> 0.1
                Unit.Deciliter -> 0.01
                Unit.Litre -> 0.001
                Unit.Hectoliter -> 1e-5
                Unit.Kiloliter -> 1e-6
                Unit.CubicMillimeter -> 1000.0
                Unit.CubicCentimeter -> 1.0
                Unit.CubicDecimeter -> 0.001
                Unit.CubicMeter -> 1e-6
                Unit.CubicInch -> 0.0610237
                Unit.CubicFoot -> 3.5315e-5
                Unit.CubicYard -> 1.30795e-6
                Unit.Gallon -> 0.000264172
                Unit.LiquidQuart -> 0.00105669
                Unit.Panh -> 0.00211338
                Unit.Cup -> 0.00422675
                Unit.FluidOunce -> 0.033814
                Unit.Tablespoon -> 0.067628
                Unit.Teaspoon -> 0.202884
                Unit.OilBarrel -> 6.2898e-6
                else -> 1.0
            }
            Unit.Litre -> when (to) {
                Unit.Milliliter -> 1000.0
                Unit.Centiliter -> 100.0
                Unit.Deciliter -> 10.0
                Unit.Hectoliter -> 0.01
                Unit.Kiloliter -> 0.001
                Unit.CubicMillimeter -> 1e6
                Unit.CubicCentimeter -> 1000.0
                Unit.CubicDecimeter -> 1.0
                Unit.CubicMeter -> 0.001
                Unit.CubicInch -> 61.0237
                Unit.CubicFoot -> 0.035315
                Unit.CubicYard -> 0.001308
                Unit.Gallon -> 0.264172
                Unit.LiquidQuart -> 1.05669
                Unit.Panh -> 2.11338
                Unit.Cup -> 4.22675
                Unit.FluidOunce -> 33.814
                Unit.Tablespoon -> 67.628
                Unit.Teaspoon -> 202.884
                Unit.OilBarrel -> 0.0062898
                else -> 1.0
            }
            // Add additional conversions for other units here
            else -> 1.0
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
}
