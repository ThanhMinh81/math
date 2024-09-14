package com.example.matheasyapp.view.tool.format

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.Locale


class ThousandSeparatorTextWatcher(private val editText: EditText) : TextWatcher {

    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (s.toString() != current) {
            editText.removeTextChangedListener(this)

            val cleanString = s.toString().replace("[,.]".toRegex(), "")
            if (cleanString.isNotEmpty()) {
                val parsed = cleanString.toDouble()
                val formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed)
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
            }

            editText.addTextChangedListener(this)
        }
    }
}