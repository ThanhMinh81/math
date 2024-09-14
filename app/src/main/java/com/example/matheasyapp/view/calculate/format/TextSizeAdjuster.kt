package com.example.matheasyapp.view.calculate.format

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.widget.TextView

class TextSizeAdjuster(private val context: Context) {

    enum class AdjustableTextType {
        Input,
        Output,
    }

    fun adjustTextSize(textView: TextView, adjustableTextType: AdjustableTextType, lengthThreshold: Int) {
        val screenWidth = context.resources.displayMetrics.widthPixels

        // Giảm kích thước văn bản một chút trước khi đạt đến chiều rộng màn hình, để trải nghiệm mượt mà hơn
        val maxWidth = screenWidth - dpToPx(35f)

        // Lấy kích thước văn bản nhỏ nhất và lớn nhất
        val (minTextSize, maxTextSize) = getTextSizeBounds(adjustableTextType)

        // Lấy văn bản hiện tại của TextView
        val text = textView.text.toString()

        // Kiểm tra nếu độ dài văn bản vượt qua ngưỡng
        if (text.length > lengthThreshold) {
            // Bắt đầu scale nếu độ dài văn bản vượt ngưỡng
            var textSize = maxTextSize
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

            val textBounds = Rect()
            val paint = textView.paint
            paint.getTextBounds(text, 0, text.length, textBounds)

            // Giảm kích thước văn bản cho đến khi nó vừa với chiều rộng cho phép và đạt kích thước tối thiểu
            while (textBounds.width() > maxWidth && textSize > minTextSize) {
                textSize -= 1f
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                paint.getTextBounds(text, 0, text.length, textBounds)
            }
        } else {
            // Nếu độ dài văn bản dưới ngưỡng, không cần scale, giữ nguyên kích thước lớn nhất
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, maxTextSize)
        }
    }

    private fun getTextSizeBounds(adjustableTextType: AdjustableTextType): Pair<Float, Float> {
        // Kích thước văn bản tối thiểu và tối đa cho cả Input và Output
        return when (adjustableTextType) {
            AdjustableTextType.Input,
            AdjustableTextType.Output -> Pair(16f, 25f) // minTextSize = 16sp, maxTextSize = 25sp
        }
    }

    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}
