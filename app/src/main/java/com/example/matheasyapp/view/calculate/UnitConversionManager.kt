package com.example.matheasyapp.view.calculate

import com.example.matheasyapp.convert.AreaConverter
import com.example.matheasyapp.convert.DataConverter
import com.example.matheasyapp.convert.MassConverter
import com.example.matheasyapp.convert.TimeConverter
import com.example.matheasyapp.convert.VolumeConverter
import com.example.matheasyapp.view.calculate.format.SharedPreferencesHelper
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class UnitConversionManager {


    private var listLength = ArrayList<String>()
    private var listWeight = ArrayList<String>()
     private var listSquare = ArrayList<String>()
    private var  listVolume = ArrayList<String>()
    private var listTime = ArrayList<String>()
    private var  listData = ArrayList<String>()

    private   var sharedPreferencesHelper: SharedPreferencesHelper

    private val decimalSeparatorSymbol = DecimalFormatSymbols.getInstance().decimalSeparator.toString()

    private val groupingSeparatorSymbol = DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private var titleConverter: String

    private var resultConverter: String

    constructor(sharedPreferencesHelper: SharedPreferencesHelper) {
        this.sharedPreferencesHelper = sharedPreferencesHelper
        titleConverter = ""
        resultConverter = ""
        initList()
    }

      fun unitConverter(newValue: String): String {

        if (listLength.contains(sharedPreferencesHelper.getString("from")) &&
            listLength.contains(sharedPreferencesHelper.getString("to"))
        ) {
            var converter = LengthConverter(
                LengthConverter.Unit.fromString(
                    sharedPreferencesHelper.getString("from", "").toString()
                ),
                LengthConverter.Unit.fromString(
                    sharedPreferencesHelper.getString("to", "").toString()
                )
            )

            val valueConvert = parseNumber(newValue)

            var result: String = converter.convert(valueConvert).toString()

            val resultFormat = getFormattedResult(result.toString().toBigDecimal())

            titleConverter = converter.provideUnitExample()

//            if (newValue != "1") {
                resultConverter = resultFormat
//            }
            return resultConverter

        } else if (listWeight.contains(sharedPreferencesHelper.getString("from"))
            && listWeight.contains(sharedPreferencesHelper.getString("to"))
        ) {

            var converter = MassConverter(
                MassConverter.Unit.fromString(
                    sharedPreferencesHelper.getString("from", "").toString()
                ),
                MassConverter.Unit.fromString(
                    sharedPreferencesHelper.getString("to", "").toString()
                )
            )
            val valueConvert = parseNumber(newValue)
            var result: String = converter.convert(valueConvert).toString()

            titleConverter = converter.provideUnitExample()

            val resultToFormat = getFormattedResult(result.toString().toBigDecimal())

//            if (newValue != "1") {
                resultConverter = resultToFormat
//            }
            return resultConverter

        }else if(listSquare.contains(sharedPreferencesHelper.getString("from")) &&
            listSquare.contains(sharedPreferencesHelper.getString("to"))){

            var converter = AreaConverter(

                AreaConverter.AreaUnit.fromString(
                    sharedPreferencesHelper.getString("from", "").toString()
                ),
                AreaConverter.AreaUnit.fromString(
                    sharedPreferencesHelper.getString("to", "").toString()
                )
            )

            val valueConvert = parseNumber(newValue)
            var result: String = converter.convert(valueConvert).toString()

            titleConverter = converter.provideUnitExample()

            val resultToFormat = getFormattedResult(result.toString().toBigDecimal())

//            if (newValue != "1") {
                resultConverter = resultToFormat
//            }
            return resultConverter

        }else if(listVolume.contains(sharedPreferencesHelper.getString("from")) &&
            listVolume.contains(sharedPreferencesHelper.getString("to"))){

            var converter = VolumeConverter(
                VolumeConverter.Unit.fromString(
                    sharedPreferencesHelper.getString("from", "").toString()
                ),
                VolumeConverter.Unit.fromString(
                    sharedPreferencesHelper.getString("to", "").toString()
                )
            )

            val valueConvert = parseNumber(newValue)
            var result: String = converter.convert(valueConvert).toString()

            titleConverter = converter.provideUnitExample()

            val resultToFormat = getFormattedResult(result.toString().toBigDecimal())

//            if (newValue != "1") {
                resultConverter = resultToFormat
//            }
            return resultConverter

        } else if(listTime.contains(sharedPreferencesHelper.getString("from")) &&
            listTime.contains(sharedPreferencesHelper.getString("to"))){

            var converter = TimeConverter(
                TimeConverter.TimeUnit.fromString(
                    sharedPreferencesHelper.getString("from", "").toString()
                ),
                TimeConverter.TimeUnit.fromString(
                    sharedPreferencesHelper.getString("to", "").toString()
                )
            )

            val valueConvert = parseNumber(newValue)
            var result: String = converter.convert(valueConvert).toString()

            titleConverter = converter.provideUnitExample()


            val resultToFormat = getFormattedResult(result.toString().toBigDecimal())

//            if (newValue != "1") {
                resultConverter = resultToFormat
//            }
            return resultConverter


        }else if(listData.contains(sharedPreferencesHelper.getString("from")) &&
            listData.contains(sharedPreferencesHelper.getString("to"))){

            var converter = DataConverter(
                DataConverter.DataUnit.fromString(
                    sharedPreferencesHelper.getString("from", "").toString()
                ),
                DataConverter.DataUnit.fromString(
                    sharedPreferencesHelper.getString("to", "").toString()
                )
            )

            val valueConvert = parseNumber(newValue)
            var result: String = converter.convert(valueConvert).toString()

            titleConverter = converter.provideUnitExample()


            val resultToFormat = getFormattedResult(result.toString().toBigDecimal())

//            if (newValue != "1") {
                resultConverter = resultToFormat
//            }
            return resultConverter


        }

        return  ""

    }


    fun getTitleConvert(): String {
        return this.titleConverter
    }

    fun parseNumber(input: String): BigDecimal {
        // Tạo DecimalFormatSymbols dựa trên ngôn ngữ và quốc gia mặc định
        val symbols = DecimalFormatSymbols.getInstance()
        val decimalSeparator = symbols.decimalSeparator
        val groupingSeparator = symbols.groupingSeparator

        // Tạo DecimalFormat với các ký hiệu hiện tại
        val format = DecimalFormat("#,###.##", symbols)

        return try {
            // Loại bỏ dấu phân cách nhóm
            val cleanedInput = input.replace(groupingSeparator.toString(), "")

            // Phân tích chuỗi đã làm sạch
            val number = format.parse(cleanedInput)

            // Chuyển đổi đối tượng Number thành BigDecimal
            number?.let {
                BigDecimal(it.toString())
            } ?: BigDecimal.ZERO
        } catch (e: Exception) {
            // Xử lý lỗi nếu chuỗi không hợp lệ
            BigDecimal.ZERO
        }
    }

    fun getFormattedResult(calculationResult: BigDecimal): String {
        val formattedResult =
            if (calculationResult.abs() >= BigDecimal("10000") || calculationResult.abs() < BigDecimal(
                    "0.1"
                )
            ) {
                val fullNumber = calculationResult.toPlainString()

                val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
                    groupingSeparator = groupingSeparatorSymbol[0]
                    decimalSeparator = decimalSeparatorSymbol[0]
                }

                val decimalFormat = DecimalFormat("#,##0.############", decimalFormatSymbols)

                decimalFormat.format(BigDecimal(fullNumber))
            } else {
                calculationResult.toPlainString()
            }

        return formattedResult
    }

    fun initList() {
        listLength.add("mm")
        listLength.add("cm")
        listLength.add("m")
        listLength.add("km")
        listLength.add("in")
        listLength.add("yd")
        listLength.add("ft-us")
        listLength.add("ft")
        listLength.add("mi")

        listWeight.add("mcg")
        listWeight.add("mg")
        listWeight.add("g")
        listWeight.add("kg")
        listWeight.add("mt")
        listWeight.add("oz")
        listWeight.add("lb")
        listWeight.add("t")

        listSquare.add("mm2")
        listSquare.add("cm2")
        listSquare.add("m2")
        listSquare.add("ha")
        listSquare.add("km2")
        listSquare.add("in2")
        listSquare.add("yd2")
        listSquare.add("ft2")
        listSquare.add("ac")
        listSquare.add("mi2")


        listVolume.add("ml")
        listVolume.add("cl")
        listVolume.add("dl")
        listVolume.add("l")
        listVolume.add("hl")
        listVolume.add("kl")
        listVolume.add("mm3")
        listVolume.add("cm3")
        listVolume.add("dm3")
        listVolume.add("m3")
        listVolume.add("in3")
        listVolume.add("ft3")
        listVolume.add("yd3")
        listVolume.add("gal")
        listVolume.add("qt")
        listVolume.add("pt")
        listVolume.add("cup")
        listVolume.add("fl-oz")
        listVolume.add("tbsp")
        listVolume.add("tsp")
        listVolume.add("bbl")

        listTime.add("ns");
        listTime.add("mu");
        listTime.add("ms");
        listTime.add("s");
        listTime.add("min");
        listTime.add("h");
        listTime.add("d");
        listTime.add("week");
        listTime.add("month");
        listTime.add("year");


        listData.add("b");   //Bit
        listData.add("Kb");  // Kilobit
        listData.add("Mb");  // Megabit
        listData.add("Gb");  // Gigabit
        listData.add("Tb");  // Terabit
        listData.add("B");   // Byte
        listData.add("KB");  // Kilobyte
        listData.add("MB");  // Megabyte
        listData.add("GB");  // Gigabyte
        listData.add("TB");


    }

}