package com.example.matheasyapp.bottomsheft.money

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Filter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.view.calculate.format.SharedPreferencesHelper
import com.example.matheasyapp.adapter.AdapterMoneyConverter
import com.example.matheasyapp.databinding.BottomsheftMoneyconverBinding
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.livedata.ConvertMoneyViewModel
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.model.Currency
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class BottomSheftFromMoney : BottomSheetDialogFragment(), OnClickItemConver {

    lateinit var binding: BottomsheftMoneyconverBinding

    private lateinit var converAdapter: AdapterMoneyConverter

    public lateinit var listCurrency: ArrayList<Currency>

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var selectedViewModel: ConvertMoneyViewModel


    private var listData: ArrayList<Currency> = arrayListOf()

    // fromMoney
    // toMoney


    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it as BottomSheetDialog
            val bottomSheetInternal =
                bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheetInternal!!)

            // Lấy chiều cao của màn hình
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels

            // Đặt chiều cao của BottomSheet theo % màn hình (80%)
            val desiredHeight = (screenHeight * 0.9).toInt()

            bottomSheetInternal.layoutParams.height = desiredHeight
            bottomSheetInternal.layoutParams = bottomSheetInternal.layoutParams

            // Đảm bảo BottomSheet không bị collapse
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            binding.layoutNoData.visibility = View.GONE


            bottomSheetInternal.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bottomSheetInternal.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    behavior.peekHeight = desiredHeight
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheftMoneyconverBinding.inflate(inflater, container, false)
        binding.root.setBackgroundResource(R.drawable.rounded_background)

        sharedPreferencesHelper = SharedPreferencesHelper(requireActivity())

        selectedViewModel =
            ViewModelProvider(requireActivity()).get(ConvertMoneyViewModel::class.java)

        var valueFromSelected = sharedPreferencesHelper.getString("fromMoney").toString()

        listCurrency = arrayListOf()
        parseJsonArray()


        val indexCheck = listCurrency.indexOfFirst { it.symbol.equals(valueFromSelected) }
        if (indexCheck != -1) {
            listCurrency.get(indexCheck).check = true
            converAdapter.notifyDataSetChanged()
        }

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isNotEmpty()) {

                    converAdapter.getFilter().filter(s.toString(), object : Filter.FilterListener {
                        override fun onFilterComplete(count: Int) {
                            val itemCount = converAdapter.itemCount
                            Log.d("453252423452", itemCount.toString())
                            if (itemCount > 0) {
                                binding.layoutNoData.visibility = View.GONE
                            } else {
                                binding.layoutNoData.visibility = View.VISIBLE
                            }
                        }
                    })

                } else {
                    binding.layoutNoData.visibility = View.GONE
                    converAdapter.updateFullList()
                    converAdapter.notifyDataSetChanged()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })



        binding.rcvUnitTo.adapter = converAdapter
        binding.rcvUnitTo.layoutManager = LinearLayoutManager(requireActivity())


        return binding.root
    }


    override fun onClickCurrency(item: Currency) {
        listCurrency.clear()

        sharedPreferencesHelper.saveValueTo("fromMoney", item.symbol.toString())
        selectedViewModel.setValueMoneyFrom(item.symbol.toString())
        selectedViewModel.setOnClickCloseFrom("true")
    }

    override fun onClick(item: Convert) {
        TODO("Not yet implemented")
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

        converAdapter = AdapterMoneyConverter(listCurrency, this, requireActivity())
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
                    converAdapter.setListSearch(listCurrency)
                    converAdapter.notifyDataSetChanged()


                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }


}