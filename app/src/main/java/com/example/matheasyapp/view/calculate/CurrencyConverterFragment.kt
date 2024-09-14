package com.example.matheasyapp.view.calculate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.bottomsheft.money.BottomSheftFromMoney
import com.example.matheasyapp.bottomsheft.money.BottomSheftToMoney
import com.example.matheasyapp.databinding.FragmentMoneyConverBinding
import com.example.matheasyapp.livedata.CaculatorViewModel
import com.example.matheasyapp.livedata.ConvertMoneyViewModel
import com.example.matheasyapp.model.Currency
import com.example.matheasyapp.view.calculate.format.SharedPreferencesHelper
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class CurrencyConverterFragment : Fragment() {

    private lateinit var binding: FragmentMoneyConverBinding

    private lateinit var selectedViewModel: ConvertMoneyViewModel

    private lateinit var dialogFrom: BottomSheftFromMoney

    private lateinit var dialogTo: BottomSheftToMoney
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var viewModelKeyboard: CaculatorViewModel

    private val decimalSeparatorSymbol =
        DecimalFormatSymbols.getInstance().decimalSeparator.toString()

    private val groupingSeparatorSymbol =
        DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private var listCurrency: ArrayList<Currency> = arrayListOf();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoneyConverBinding.inflate(layoutInflater, container, false)
        dialogFrom = BottomSheftFromMoney()
        dialogTo = BottomSheftToMoney()

        parseJsonArray()

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        viewModelKeyboard = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)

        if ((sharedPreferencesHelper.getString("fromMoney", "null").equals("null"))) {
            sharedPreferencesHelper.saveValueTo("fromMoney", "USD")
            sharedPreferencesHelper.saveValueTo("toMoney", "AED")
        }

        binding.tvValueFrom.text = sharedPreferencesHelper.getString("fromMoney", "USD").toString()
        binding.tvValueTo.text = sharedPreferencesHelper.getString("toMoney", "AED").toString()

        updateTitleConverter()

        viewModelKeyboard.getLiveTvResult().observe(
            viewLifecycleOwner,
            Observer { newValue ->
                if (newValue.isNotEmpty()) {
                    if (newValue.last().isDigit()) {

                        val value = removeDotsAndCommas(newValue)

                        if (value.toBigDecimal() > BigDecimal(0)) {
                            binding.fromValue.setText(newValue)

                            val valueTo = converterCurrency(
                                binding.fromValue.text.toString(),
                                binding.tvValueFrom.text.toString().trim(),
                                binding.tvValueTo.text.toString().trim()
                            )
                            updateTitleConverter()
                            binding.toValue.setText(valueTo.toString())
                        }

                    } else {
                        binding.fromValue.setText("0")
                    }
                } else {
                    binding.fromValue.setText("0")
                    binding.toValue.setText("0")
                }
            })


        if (binding.fromValue.text.isEmpty()) {
            binding.fromValue.setText("0")
            binding.toValue.setText("0")
        }

        selectedViewModel =
            ViewModelProvider(requireActivity()).get(ConvertMoneyViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        binding.btnSwichConver.setOnClickListener {

            swichConverter()

//            if (binding.fromValue.text.length > 0) {
//                if (binding.fromValue.text.toString().toBigDecimal() > BigDecimal(0)) {
//                    converterCurrency(
//                        binding.fromValue.text.toString(),
//                        binding.tvValueFrom.text.toString(),
//                        binding.tvValueTo.text.toString()
//                    )
//                    updateTitleConverter()
//                }
//            }
        }




        binding.wrapperUnitFrom.setOnClickListener {
            try {

                dialogFrom = BottomSheftFromMoney()
                dialogFrom.show(parentFragmentManager, "MyDialogFragment")

            } catch (e: Exception) {
                println("looiioio ${e}")
            }
        }

        selectedViewModel.getLiveDataValueMoneyFrom()
            .observe(requireActivity(), Observer { valueFrom ->
                binding.tvValueFrom.text = valueFrom
            })

        selectedViewModel.getOnClickCloseFrom().observe(requireActivity(), Observer { value ->

            if (dialogFrom.isAdded && dialogFrom.isVisible) {
                dialogFrom.dismiss()
            }
            // ham chuyen doi

            if (binding.fromValue.text.toString().length > 0) {

                val valueTo = converterCurrency(
                    binding.fromValue.text.toString().trim(),
                    binding.tvValueFrom.text.toString().trim(),
                    binding.tvValueTo.text.toString().trim()
                )
                binding.toValue.setText(valueTo.toString())
                updateTitleConverter()

            }
        })

        binding.wrapUnitTo.setOnClickListener {
            // Tooo
            dialogTo = BottomSheftToMoney()
            dialogTo.show(parentFragmentManager, "MyDialogFragment")
        }

        selectedViewModel.getLiveDataValueMoneyTo().observe(
            requireActivity(), Observer { valueFrom ->
                binding.tvValueTo.text = valueFrom

            }
        )
        selectedViewModel.getOnClickCloseTo().observe(requireActivity(),
            Observer { value ->

                if (dialogTo.isAdded && dialogTo.isVisible) {
                    dialogTo.dismiss()
                }
                // ham chuyen doi

                if (binding.fromValue.text.toString().length > 0) {

                    val valueTo = converterCurrency(
                        binding.fromValue.text.toString().trim(),
                        binding.tvValueFrom.text.toString().trim(),
                        binding.tvValueTo.text.toString().trim()
                    )

                    binding.toValue.setText(valueTo.toString())
                    updateTitleConverter()

                }
            })

        return binding.root

    }


    private fun swichConverter() {
        val fromUnit: String = sharedPreferencesHelper.getString("fromMoney", "").toString()
        val toUnit: String = sharedPreferencesHelper.getString("toMoney", "").toString()

        binding.tvValueFrom.setText(toUnit)
        binding.tvValueTo.setText(fromUnit)

        sharedPreferencesHelper.saveValueTo("fromMoney", toUnit)
        sharedPreferencesHelper.saveValueTo("toMoney", fromUnit)

        val valueTo = converterCurrency(
            binding.fromValue.text.toString(),
            binding.tvValueFrom.text.toString(),
            binding.tvValueTo.text.toString()
        )
        updateTitleConverter()

        binding.toValue.setText(valueTo.toString())

    }

    private fun updateTitleConverter() {
        val value = getExchangeRate(
            binding.tvValueFrom.text.toString().trim(),
            binding.tvValueTo.text.toString().trim()
        )

        binding.tvTitleFrom.text = "1 " + binding.tvValueFrom.text.toString()
        binding.tvTitleTo.text = "$value " + binding.tvValueTo.text.toString()

    }


    fun removeDotsAndCommas(input: String): String {
        return input.replace(Regex("[.,]"), "")
    }


    fun converterCurrency(value: String, symbolsFrom: String, symbolsTo: String): String {

        val valueRemoveDot = removeDotsAndCommas(value)
        val parseNumber = parseNumber(valueRemoveDot)
        val currencyFrom: Currency? =
            listCurrency.find { currency -> currency.symbol.trim().equals(symbolsFrom.trim()) }

        val currencyTo: Currency? =
            listCurrency.find { currency -> currency.symbol.trim().equals(symbolsTo.trim()) }

        val rateFrom = currencyFrom!!.rate.toBigDecimal()
        val rateTo = currencyTo!!.rate.toBigDecimal()


        val resultNumber: BigDecimal =
            parseNumber * (rateTo.divide(rateFrom, 10, RoundingMode.HALF_UP))

        return getFormattedResult(resultNumber)

    }

    fun getExchangeRate(symbolsFrom: String, symbolsTo: String): BigDecimal {
        val currencyFrom: Currency? =
            listCurrency.find { currency -> currency.symbol.trim().equals(symbolsFrom.trim()) }

        val currencyTo: Currency? =
            listCurrency.find { currency -> currency.symbol.trim().equals(symbolsTo.trim()) }

        val rateFrom = currencyFrom!!.rate.toBigDecimal()
        val rateTo = currencyTo!!.rate.toBigDecimal()

        // Tỷ lệ giữa symbolsFrom và symbolsTo
        return rateTo.divide(rateFrom, 10, RoundingMode.HALF_UP)
    }


    fun parseNumber(input: String): BigDecimal {
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

    fun readJsonFromAssets(fileName: String): String? {
        return try {
            val inputStream = requireActivity().assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    fun parseJsonArray() {
        val jsonString = readJsonFromAssets("currency.json")
        jsonString?.let {
            try {
                val jsonArray = JSONArray(it)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("name")
                    val symbol = jsonObject.getString("symbol")
                    val value = jsonObject.getString("value")

                    val currency = Currency(symbol, name, value.toDouble(), false)

                    listCurrency.add(currency)


                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }


}


