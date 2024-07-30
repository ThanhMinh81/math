package com.example.matheasyapp.view.calculate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.MyPreferences
import com.example.matheasyapp.NumberFormatter
import com.example.matheasyapp.SharedPreferencesHelper
import com.example.matheasyapp.TextSizeAdjuster
import com.example.matheasyapp.bottomsheft.BottomSheftUnitFromConver
import com.example.matheasyapp.bottomsheft.BottomSheftUnitToConver
import com.example.matheasyapp.convert.MassConverter

import com.example.matheasyapp.databinding.FragmentConversionUnitBinding
import com.example.matheasyapp.livedata.CaculatorViewModel
import com.example.matheasyapp.livedata.SelectedFromViewModel
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale


class UnitConverFragment : Fragment() {

    //    private lateinit var  converter: LengthConverter
    private lateinit var binding: FragmentConversionUnitBinding

    private var valueSelected: String = ""

    // check selected item
    private lateinit var selectedFromViewModel: SelectedFromViewModel

    private lateinit var dialogFrom: BottomSheftUnitFromConver

    private lateinit var dialogTo: BottomSheftUnitToConver

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var bottomSheftOnClick: BottomSheftUnitToConver

    private lateinit var viewModelKeyboard: CaculatorViewModel

    private val decimalSeparatorSymbol =
        DecimalFormatSymbols.getInstance().decimalSeparator.toString()

    private val groupingSeparatorSymbol =
        DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private var listLength = ArrayList<String>()
    private var listWeight = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initList()
        val textSizeAdjuster = TextSizeAdjuster(requireActivity())


        bottomSheftOnClick = BottomSheftUnitToConver()

        binding = FragmentConversionUnitBinding.inflate(layoutInflater, container, false)

        selectedFromViewModel =
            ViewModelProvider(requireActivity()).get(SelectedFromViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        viewModelKeyboard = ViewModelProvider(requireActivity()).get(CaculatorViewModel::class.java)

        dialogFrom = BottomSheftUnitFromConver();

        viewModelKeyboard.getLiveTvResult().observe(
            viewLifecycleOwner,
            Observer { newValue ->
                if (newValue.isNotEmpty()) {
                    if (newValue.last().isDigit()) {

                        val value = removeDotsAndCommas(newValue)
                        UnitConverter(value)
                        binding.fromValue.setText(newValue)

                    } else {
                        binding.fromValue.setText("0")
                    }
                } else {
                    binding.fromValue.setText("0")
                    binding.toValue.setText("0")
                }
            })

        binding.btnSwichConver.setOnClickListener {
            swichConverter()
        }

        // init value

        if ((sharedPreferencesHelper.getString("from", "null").equals("null"))) {

            sharedPreferencesHelper.saveValueTo("from", "mcg")
            sharedPreferencesHelper.saveValueTo("typeFrom", "weight")
            sharedPreferencesHelper.saveValueTo("to", "g")
            sharedPreferencesHelper.saveValueTo("typeTo", "weight")

        }


        binding.tvValueFrom.text = sharedPreferencesHelper.getString("from", "mcg").toString()
        binding.tvValueTo.text = sharedPreferencesHelper.getString("to", "g").toString()

        binding.toValue.addTextChangedListener(object : TextWatcher {
            private var beforeTextLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeTextLength = s?.length ?: 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                textSizeAdjuster.adjustTextSize(
                    binding.toValue,
                    TextSizeAdjuster.AdjustableTextType.Input
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.fromValue.addTextChangedListener(object : TextWatcher {
            private var beforeTextLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeTextLength = s?.length ?: 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                textSizeAdjuster.adjustTextSize(
                    binding.fromValue,
                    TextSizeAdjuster.AdjustableTextType.Input
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        UnitConverter("1")

        dialogTo = BottomSheftUnitToConver()

        binding.wrapperUnitFrom.setOnClickListener {
            try {


//                val annualInterestRate = 10.0 // Lãi suất hàng năm là 10%
//                val monthlyInterestRate = annualInterestRate / 100 / 12 * 10000

                println("Lãi suất hàng tháng:  ")


                var keyCurrent: String =
                    if (binding.tvValueFrom.text.length > 0) binding.tvValueFrom.text.toString() else "mm"

                dialogFrom = BottomSheftUnitFromConver()
                dialogFrom.show(parentFragmentManager, "MyDialogFragment")
            } catch (e: Exception) {
                println("looiioio ${e}")
            }

        }

        // handle event selectedFromViewModel
        selectedFromViewModel.getLiveDataValueFrom()
            .observe(viewLifecycleOwner, Observer { valueFrom ->
                //hide dialog from

                valueSelected = valueFrom

                binding.tvValueFrom.setText(sharedPreferencesHelper.getString("from", ""))
//            initKeyFrom.setInitKey(valueFrom)
                dialogFrom.dismiss()

                //show dialog to
                // valueFrom : keyInit

                if (!(sharedPreferencesHelper.getString("typeFrom")
                        .equals(sharedPreferencesHelper.getString("typeTo")))
                    || (sharedPreferencesHelper.getString("from")
                        .equals(sharedPreferencesHelper.getString("to")))
                ) {

                    try {
                        dialogTo = BottomSheftUnitToConver()
                        dialogTo.show(parentFragmentManager, "MyDialogFragment")
                    } catch (e: java.lang.Exception) {
                        println("456456 ${e.printStackTrace()}")
                    }
                }

                convertUnit()


            })

        //update text button
        selectedFromViewModel.getLiveDataValueTo().observe(viewLifecycleOwner, Observer { valueTo ->
            binding.tvValueTo.setText(valueTo)

            convertUnit()


        })

        // dong dialog thi call cai nay
        selectedFromViewModel.getSelectedOnClick()
            .observe(viewLifecycleOwner, Observer { valueSelected ->
                dialogTo.dismiss()

                convertUnit()

            })

        binding.wrapUnitTo.setOnClickListener {
            // Tooo

            bottomSheftOnClick = BottomSheftUnitToConver
                .newInstance(
                    sharedPreferencesHelper.getString("to", "").toString(),
                    sharedPreferencesHelper.getString("from", "").toString()
                )
            bottomSheftOnClick.show(
                requireActivity().getSupportFragmentManager(),
                bottomSheftOnClick.tag
            )

        }

        selectedFromViewModel.getOnClickBottomTo().observe(viewLifecycleOwner, Observer { value ->
            bottomSheftOnClick.dismiss()

            convertUnit()

        })

        return binding.root

    }


    private fun swichConverter() {
        val fromUnit: String = sharedPreferencesHelper.getString("from", "").toString()
        val toUnit: String = sharedPreferencesHelper.getString("to", "").toString()

        binding.tvValueFrom.setText(toUnit)
        binding.tvValueTo.setText(fromUnit)

        sharedPreferencesHelper.saveValueTo("from", toUnit)
        sharedPreferencesHelper.saveValueTo("to", fromUnit)

        convertUnit()

    }

    fun removeDotsAndCommas(input: String): String {
        return input.replace(Regex("[.,]"), "")
    }

    fun formatNumberWithThousandsSeparator(number: Long): String {
        // Tạo NumberFormat để định dạng số với dấu phân cách hàng nghìn
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        return numberFormat.format(number)
    }

    private fun convertUnit() {

        val value = removeDotsAndCommas(binding.fromValue.text.toString())
        println("remove dot ${value}")
        if (value.isNotEmpty()) {
            if (value.toBigDecimal() > "0".toBigDecimal()) {

                if (sharedPreferencesHelper.getString("typeFrom").toString().equals(
                        sharedPreferencesHelper.getString("typeTo").toString()
                    )
                ) {
                    UnitConverter(value)
                }
            }
        }
    }

    private fun UnitConverter(newValue: String) {

        if (listLength.contains(sharedPreferencesHelper.getString("from"))) {
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

            println("checkkkkk $result")


            val result2 = getFormattedResult(result.toString().toBigDecimal())
            println("dddddaaaaa $result2")

            binding.tvTitleConvert.setText(converter.provideUnitExample())

            if (newValue != "1") {
                binding.toValue.setText(result2)
            }

        } else if (listWeight.contains(sharedPreferencesHelper.getString("from"))) {

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

            binding.tvTitleConvert.setText(converter.provideUnitExample())

            if (newValue != "1") {
                binding.fromValue.setText(newValue)
                binding.toValue.setText(result.toString())
            }
        }

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

//    fun getFormattedResult(calculationResult: BigDecimal): String {
//        // Đảm bảo rằng chúng ta không sử dụng số dạng khoa học
//        val formattedResult =
//            if (calculationResult.abs() >= BigDecimal("10000") || calculationResult.abs() < BigDecimal(
//                    "0.1"
//                )
//            ) {
//                // Chuyển đổi số thành chuỗi đầy đủ mà không có ký hiệu khoa học
//                val fullNumber = calculationResult.toPlainString()
//
//                // Tạo DecimalFormat để định dạng số theo yêu cầu
//                val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
//                    groupingSeparator = groupingSeparatorSymbol[0]
//                    decimalSeparator = decimalSeparatorSymbol[0]
//                }
//                val decimalFormat = DecimalFormat("#,##0.###", decimalFormatSymbols)
//
//                // Định dạng số và loại bỏ dấu phân cách hàng nghìn không cần thiết
//                decimalFormat.format(fullNumber.toDouble())
//            } else {
//                // Nếu không cần loại bỏ ký hiệu khoa học
//                calculationResult.toPlainString()
//            }
//
//        return formattedResult
//    }


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

    }


}