package com.example.matheasyapp.view.tool.format
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

class MaxValueTextWatcher(private val editText: EditText, private val maxValue: Int) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)

        val input = s.toString()
        if (input.isNotEmpty()) {
            try {
                val value = input.toInt()
                if (value > maxValue) {
                    Toast.makeText(editText.context, "Giá trị tối đa $maxValue%", Toast.LENGTH_SHORT).show()
                    editText.setText(maxValue.toString())
                    editText.setSelection(maxValue.toString().length)
                }
            } catch (e: NumberFormatException) {
                // Handle if the input is not a valid number
            }
        }

        editText.addTextChangedListener(this)
    }
}
