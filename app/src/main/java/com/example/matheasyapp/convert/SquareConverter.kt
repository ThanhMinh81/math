package com.example.matheasyapp.convert

import java.math.BigDecimal

class AreaConverter(private val from: AreaUnit, private val to: AreaUnit) {

    enum class AreaUnit(val symbol: String) {
        SquareMillimeter("mm2"), SquareCentimeter("cm2"), SquareMeter("m2"), Hectare("ha"),
        SquareKilometer("km2"), SquareInch("in2"), SquareYard("yd2"), SquareFoot("ft2"),
        Acre("ac"), SquareMile("mi2");

        companion object {
            fun fromString(symbol: String): AreaUnit {
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
            AreaUnit.SquareMillimeter -> when (to) {
                AreaUnit.SquareCentimeter -> 0.01
                AreaUnit.SquareMeter -> 1e-6
                AreaUnit.Hectare -> 1e-8
                AreaUnit.SquareKilometer -> 1e-12
                AreaUnit.SquareInch -> 0.0015500031
                AreaUnit.SquareYard -> 1.19599e-6
                AreaUnit.SquareFoot -> 1.07639e-5
                AreaUnit.Acre -> 2.471e-10
                AreaUnit.SquareMile -> 3.861e-13
                else -> 1.0
            }

            AreaUnit.SquareCentimeter -> when (to) {
                AreaUnit.SquareMillimeter -> 100.0
                AreaUnit.SquareMeter -> 1e-4
                AreaUnit.Hectare -> 1e-6
                AreaUnit.SquareKilometer -> 1e-10
                AreaUnit.SquareInch -> 0.15500031
                AreaUnit.SquareYard -> 1.19599e-4
                AreaUnit.SquareFoot -> 1.07639e-3
                AreaUnit.Acre -> 2.471e-7
                AreaUnit.SquareMile -> 3.861e-10
                else -> 1.0
            }

            AreaUnit.SquareMeter -> when (to) {
                AreaUnit.SquareMillimeter -> 1e6
                AreaUnit.SquareCentimeter -> 1e4
                AreaUnit.Hectare -> 1e-4
                AreaUnit.SquareKilometer -> 1e-6
                AreaUnit.SquareInch -> 1550.0031
                AreaUnit.SquareYard -> 1.19599
                AreaUnit.SquareFoot -> 10.7639
                AreaUnit.Acre -> 2.47105e-4
                AreaUnit.SquareMile -> 3.861e-7
                else -> 1.0
            }

            AreaUnit.Hectare -> when (to) {
                AreaUnit.SquareMillimeter -> 1e8
                AreaUnit.SquareCentimeter -> 1e6
                AreaUnit.SquareMeter -> 1e4
                AreaUnit.SquareKilometer -> 1e-2
                AreaUnit.SquareInch -> 1.5500031e4
                AreaUnit.SquareYard -> 1.19599e2
                AreaUnit.SquareFoot -> 1.07639e1
                AreaUnit.Acre -> 2.47105
                AreaUnit.SquareMile -> 3.861e-4
                else -> 1.0
            }

            AreaUnit.SquareKilometer -> when (to) {
                AreaUnit.SquareMillimeter -> 1e12
                AreaUnit.SquareCentimeter -> 1e10
                AreaUnit.SquareMeter -> 1e6
                AreaUnit.Hectare -> 1e4
                AreaUnit.SquareInch -> 1.5500031e8
                AreaUnit.SquareYard -> 1.19599e6
                AreaUnit.SquareFoot -> 1.07639e5
                AreaUnit.Acre -> 2.47105e-2
                AreaUnit.SquareMile -> 3.861e-1
                else -> 1.0
            }

            AreaUnit.SquareInch -> when (to) {
                AreaUnit.SquareMillimeter -> 645.16
                AreaUnit.SquareCentimeter -> 6.4516
                AreaUnit.SquareMeter -> 0.00064516
                AreaUnit.Hectare -> 6.4516e-8
                AreaUnit.SquareKilometer -> 6.4516e-10
                AreaUnit.SquareYard -> 0.000771605
                AreaUnit.SquareFoot -> 0.00694444
                AreaUnit.Acre -> 1.584e-7
                AreaUnit.SquareMile -> 2.490e-10
                else -> 1.0
            }

            AreaUnit.SquareYard -> when (to) {
                AreaUnit.SquareMillimeter -> 836127.36
                AreaUnit.SquareCentimeter -> 8361.2736
                AreaUnit.SquareMeter -> 0.83612736
                AreaUnit.Hectare -> 8.3612736e-5
                AreaUnit.SquareKilometer -> 8.3612736e-8
                AreaUnit.SquareInch -> 1296.0
                AreaUnit.SquareFoot -> 9.0
                AreaUnit.Acre -> 0.000206612
                AreaUnit.SquareMile -> 0.000000258
                else -> 1.0
            }

            AreaUnit.SquareFoot -> when (to) {
                AreaUnit.SquareMillimeter -> 92903.04
                AreaUnit.SquareCentimeter -> 929.0304
                AreaUnit.SquareMeter -> 0.09290304
                AreaUnit.Hectare -> 9.290304e-6
                AreaUnit.SquareKilometer -> 9.290304e-9
                AreaUnit.SquareInch -> 144.0
                AreaUnit.SquareYard -> 0.111111
                AreaUnit.Acre -> 0.0000229568
                AreaUnit.SquareMile -> 2.29568e-8
                else -> 1.0
            }

            AreaUnit.Acre -> when (to) {
                AreaUnit.SquareMillimeter -> 4.04686e6
                AreaUnit.SquareCentimeter -> 404686.0
                AreaUnit.SquareMeter -> 4046.86
                AreaUnit.Hectare -> 0.404686
                AreaUnit.SquareKilometer -> 0.000404686
                AreaUnit.SquareInch -> 6273000.0
                AreaUnit.SquareYard -> 4840.0
                AreaUnit.SquareFoot -> 43560.0
                AreaUnit.SquareMile -> 0.0015625
                else -> 1.0
            }

            AreaUnit.SquareMile -> when (to) {
                AreaUnit.SquareMillimeter -> 2.58999e9
                AreaUnit.SquareCentimeter -> 25899900.0
                AreaUnit.SquareMeter -> 2589990.0
                AreaUnit.Hectare -> 258.999
                AreaUnit.SquareKilometer -> 2.58999
                AreaUnit.SquareInch -> 4.0149e10
                AreaUnit.SquareYard -> 27878400.0
                AreaUnit.SquareFoot -> 27878400.0
                AreaUnit.Acre -> 640.0
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