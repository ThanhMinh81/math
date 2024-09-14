package com.example.matheasyapp.convert

import java.math.BigDecimal

class TimeConverter(private val from: TimeUnit, private val to: TimeUnit) {

    enum class TimeUnit(val symbol: String) {
        Nanosecond("ns"), Microsecond("mu"), Millisecond("ms"), Second("s"),
        Minute("min"), Hour("h"), Day("d"), Week("week"), Month("month"), Year("year");

        companion object {
            fun fromString(symbol: String): TimeUnit {
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
            TimeUnit.Nanosecond -> when (to) {
                TimeUnit.Microsecond -> 1e-3
                TimeUnit.Millisecond -> 1e-6
                TimeUnit.Second -> 1e-9
                TimeUnit.Minute -> 1e-9 / 60
                TimeUnit.Hour -> 1e-9 / 3600
                TimeUnit.Day -> 1e-9 / 86400
                TimeUnit.Week -> 1e-9 / 604800
                TimeUnit.Month -> 1e-9 / (30.436875 * 86400)
                TimeUnit.Year -> 1e-9 / (365.25 * 86400)
                else -> 1.0
            }

            TimeUnit.Microsecond -> when (to) {
                TimeUnit.Nanosecond -> 1e3
                TimeUnit.Millisecond -> 1e-3
                TimeUnit.Second -> 1e-6
                TimeUnit.Minute -> 1e-6 / 60
                TimeUnit.Hour -> 1e-6 / 3600
                TimeUnit.Day -> 1e-6 / 86400
                TimeUnit.Week -> 1e-6 / 604800
                TimeUnit.Month -> 1e-6 / (30.436875 * 86400)
                TimeUnit.Year -> 1e-6 / (365.25 * 86400)
                else -> 1.0
            }

            TimeUnit.Millisecond -> when (to) {
                TimeUnit.Nanosecond -> 1e6
                TimeUnit.Microsecond -> 1e3
                TimeUnit.Second -> 1e-3
                TimeUnit.Minute -> 1e-3 / 60
                TimeUnit.Hour -> 1e-3 / 3600
                TimeUnit.Day -> 1e-3 / 86400
                TimeUnit.Week -> 1e-3 / 604800
                TimeUnit.Month -> 1e-3 / (30.436875 * 86400)
                TimeUnit.Year -> 1e-3 / (365.25 * 86400)
                else -> 1.0
            }

            TimeUnit.Second -> when (to) {
                TimeUnit.Nanosecond -> 1e9
                TimeUnit.Microsecond -> 1e6
                TimeUnit.Millisecond -> 1e3
                TimeUnit.Minute -> 1 / 60.0
                TimeUnit.Hour -> 1 / 3600.0
                TimeUnit.Day -> 1 / 86400.0
                TimeUnit.Week -> 1 / 604800.0
                TimeUnit.Month -> 1 / (30.436875 * 86400)
                TimeUnit.Year -> 1 / (365.25 * 86400)
                else -> 1.0
            }

            TimeUnit.Minute -> when (to) {
                TimeUnit.Nanosecond -> 60e9
                TimeUnit.Microsecond -> 60e6
                TimeUnit.Millisecond -> 60e3
                TimeUnit.Second -> 60.0
                TimeUnit.Hour -> 1 / 60.0
                TimeUnit.Day -> 1 / 1440.0
                TimeUnit.Week -> 1 / 10080.0
                TimeUnit.Month -> 1 / (30.436875 * 1440)
                TimeUnit.Year -> 1 / (365.25 * 1440)
                else -> 1.0
            }

            TimeUnit.Hour -> when (to) {
                TimeUnit.Nanosecond -> 3600e9
                TimeUnit.Microsecond -> 3600e6
                TimeUnit.Millisecond -> 3600e3
                TimeUnit.Second -> 3600.0
                TimeUnit.Minute -> 60.0
                TimeUnit.Day -> 1 / 24.0
                TimeUnit.Week -> 1 / 168.0
                TimeUnit.Month -> 1 / (30.436875 * 24)
                TimeUnit.Year -> 1 / (365.25 * 24)
                else -> 1.0
            }

            TimeUnit.Day -> when (to) {
                TimeUnit.Nanosecond -> 86400e9
                TimeUnit.Microsecond -> 86400e6
                TimeUnit.Millisecond -> 86400e3
                TimeUnit.Second -> 86400.0
                TimeUnit.Minute -> 1440.0
                TimeUnit.Hour -> 24.0
                TimeUnit.Week -> 1 / 7.0
                TimeUnit.Month -> 1 / 30.436875
                TimeUnit.Year -> 1 / 365.25
                else -> 1.0
            }

            TimeUnit.Week -> when (to) {
                TimeUnit.Nanosecond -> 604800e9
                TimeUnit.Microsecond -> 604800e6
                TimeUnit.Millisecond -> 604800e3
                TimeUnit.Second -> 604800.0
                TimeUnit.Minute -> 10080.0
                TimeUnit.Hour -> 168.0
                TimeUnit.Day -> 7.0
                TimeUnit.Month -> 7 / 30.436875
                TimeUnit.Year -> 7 / 365.25
                else -> 1.0
            }

            TimeUnit.Month -> when (to) {
                TimeUnit.Nanosecond -> 30.436875 * 86400e9
                TimeUnit.Microsecond -> 30.436875 * 86400e6
                TimeUnit.Millisecond -> 30.436875 * 86400e3
                TimeUnit.Second -> 30.436875 * 86400
                TimeUnit.Minute -> 30.436875 * 1440
                TimeUnit.Hour -> 30.436875 * 24
                TimeUnit.Day -> 30.436875
                TimeUnit.Week -> 30.436875 / 7
                TimeUnit.Year -> 1 / 12.0
                else -> 1.0
            }

            TimeUnit.Year -> when (to) {
                TimeUnit.Nanosecond -> 365.25 * 86400e9
                TimeUnit.Microsecond -> 365.25 * 86400e6
                TimeUnit.Millisecond -> 365.25 * 86400e3
                TimeUnit.Second -> 365.25 * 86400
                TimeUnit.Minute -> 365.25 * 1440
                TimeUnit.Hour -> 365.25 * 24
                TimeUnit.Day -> 365.25
                TimeUnit.Week -> 365.25 / 7
                TimeUnit.Month -> 12.0
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
