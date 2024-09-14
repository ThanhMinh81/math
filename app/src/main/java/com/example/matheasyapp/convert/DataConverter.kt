package com.example.matheasyapp.convert

import java.math.BigDecimal

class DataConverter(private val from: DataUnit, private val to: DataUnit) {

    enum class DataUnit(val symbol: String) {
        Bit("b"), Kilobit("Kb"), Megabit("Mb"), Gigabit("Gb"), Terabit("Tb"),
        Byte("B"), Kilobyte("KB"), Megabyte("MB"), Gigabyte("GB"), Terabyte("TB");

        companion object {
            fun fromString(symbol: String): DataUnit {
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
            DataUnit.Bit -> when (to) {
                DataUnit.Kilobit -> 1 / 1000.0
                DataUnit.Megabit -> 1 / 1_000_000.0
                DataUnit.Gigabit -> 1 / 1_000_000_000.0
                DataUnit.Terabit -> 1 / 1_000_000_000_000.0
                DataUnit.Byte -> 1 / 8.0
                DataUnit.Kilobyte -> 1 / 8_000.0
                DataUnit.Megabyte -> 1 / 8_000_000.0
                DataUnit.Gigabyte -> 1 / 8_000_000_000.0
                DataUnit.Terabyte -> 1 / 8_000_000_000_000.0
                else -> 1.0
            }

            DataUnit.Kilobit -> when (to) {
                DataUnit.Bit -> 1_000.0
                DataUnit.Megabit -> 1 / 1_000.0
                DataUnit.Gigabit -> 1 / 1_000_000.0
                DataUnit.Terabit -> 1 / 1_000_000_000.0
                DataUnit.Byte -> 125.0
                DataUnit.Kilobyte -> 1 / 8.0
                DataUnit.Megabyte -> 1 / 8_000.0
                DataUnit.Gigabyte -> 1 / 8_000_000.0
                DataUnit.Terabyte -> 1 / 8_000_000_000.0
                else -> 1.0
            }

            DataUnit.Megabit -> when (to) {
                DataUnit.Bit -> 1_000_000.0
                DataUnit.Kilobit -> 1_000.0
                DataUnit.Gigabit -> 1 / 1_000.0
                DataUnit.Terabit -> 1 / 1_000_000.0
                DataUnit.Byte -> 125_000.0
                DataUnit.Kilobyte -> 125.0
                DataUnit.Megabyte -> 1 / 8.0
                DataUnit.Gigabyte -> 1 / 8_000.0
                DataUnit.Terabyte -> 1 / 8_000_000.0
                else -> 1.0
            }

            DataUnit.Gigabit -> when (to) {
                DataUnit.Bit -> 1_000_000_000.0
                DataUnit.Kilobit -> 1_000_000.0
                DataUnit.Megabit -> 1_000.0
                DataUnit.Terabit -> 1 / 1_000.0
                DataUnit.Byte -> 125_000_000.0
                DataUnit.Kilobyte -> 125_000.0
                DataUnit.Megabyte -> 125.0
                DataUnit.Gigabyte -> 1 / 8.0
                DataUnit.Terabyte -> 1 / 8_000.0
                else -> 1.0
            }

            DataUnit.Terabit -> when (to) {
                DataUnit.Bit -> 1_000_000_000_000.0
                DataUnit.Kilobit -> 1_000_000_000.0
                DataUnit.Megabit -> 1_000_000.0
                DataUnit.Gigabit -> 1_000.0
                DataUnit.Byte -> 125_000_000_000.0
                DataUnit.Kilobyte -> 125_000_000.0
                DataUnit.Megabyte -> 125_000.0
                DataUnit.Gigabyte -> 125.0
                DataUnit.Terabyte -> 1 / 8.0
                else -> 1.0
            }

            DataUnit.Byte -> when (to) {
                DataUnit.Bit -> 8.0
                DataUnit.Kilobit -> 8_000.0
                DataUnit.Megabit -> 8_000_000.0
                DataUnit.Gigabit -> 8_000_000_000.0
                DataUnit.Terabit -> 8_000_000_000_000.0
                DataUnit.Kilobyte -> 1 / 1_000.0
                DataUnit.Megabyte -> 1 / 1_000_000.0
                DataUnit.Gigabyte -> 1 / 1_000_000_000.0
                DataUnit.Terabyte -> 1 / 1_000_000_000_000.0
                else -> 1.0
            }

            DataUnit.Kilobyte -> when (to) {
                DataUnit.Bit -> 8_000.0
                DataUnit.Kilobit -> 8_000_000.0
                DataUnit.Megabit -> 8_000_000_000.0
                DataUnit.Gigabit -> 8_000_000_000_000.0
                DataUnit.Terabit -> 8_000_000_000_000_000.0
                DataUnit.Byte -> 1_000.0
                DataUnit.Megabyte -> 1 / 1_000.0
                DataUnit.Gigabyte -> 1 / 1_000_000.0
                DataUnit.Terabyte -> 1 / 1_000_000_000.0
                else -> 1.0
            }

            DataUnit.Megabyte -> when (to) {
                DataUnit.Bit -> 8_000_000.0
                DataUnit.Kilobit -> 8_000_000_000.0
                DataUnit.Megabit -> 8_000_000_000_000.0
                DataUnit.Gigabit -> 8_000_000_000_000_000.0
                DataUnit.Terabit -> 8_000_000_000_000_000_000.0
                DataUnit.Byte -> 1_000_000.0
                DataUnit.Kilobyte -> 1_000.0
                DataUnit.Gigabyte -> 1 / 1_000.0
                DataUnit.Terabyte -> 1 / 1_000_000.0
                else -> 1.0
            }

            DataUnit.Gigabyte -> when (to) {
                DataUnit.Bit -> 8_000_000_000.0
                DataUnit.Kilobit -> 8_000_000_000_000.0
                DataUnit.Megabit -> 8_000_000_000_000_000.0
                DataUnit.Gigabit -> 8_000_000_000_000_000_000.0
                DataUnit.Terabit -> 8_000_000_000_000_000_000_000.0
                DataUnit.Byte -> 1_000_000_000.0
                DataUnit.Kilobyte -> 1_000_000.0
                DataUnit.Megabyte -> 1_000.0
                DataUnit.Terabyte -> 1 / 1_000.0
                else -> 1.0
            }

            DataUnit.Terabyte -> when (to) {
                DataUnit.Bit -> 8_000_000_000_000.0
                DataUnit.Kilobit -> 8_000_000_000_000_000.0
                DataUnit.Megabit -> 8_000_000_000_000_000_000.0
                DataUnit.Gigabit -> 8_000_000_000_000_000_000_000.0
                DataUnit.Terabit -> 8_000_000_000_000_000_000_000_000.0
                DataUnit.Byte -> 1_000_000_000_000.0
                DataUnit.Kilobyte -> 1_000_000_000.0
                DataUnit.Megabyte -> 1_000_000.0
                DataUnit.Gigabyte -> 1_000.0
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
