package com.example.matheasyapp

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.util.TypedValue
import android.widget.TextView

class TextSizeAdjuster(private val context: Context) {

    enum class AdjustableTextType {
        Input,
        Output,
    }

    fun adjustTextSize(textView: TextView, adjustableTextType: AdjustableTextType) {
        val screenWidth = context.resources.displayMetrics.widthPixels

        // Text size will be reduced a bit before reaching the screen width, for a smoother experience
        val maxWidth = screenWidth - dpToPx(35f)

        // Get the min and max text sizes
        val (minTextSize, maxTextSize) = getTextSizeBounds(adjustableTextType)

        var textSize = maxTextSize
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

        val textBounds = Rect()
        val text = textView.text.toString()
        val paint = textView.paint
        paint.getTextBounds(text, 0, text.length, textBounds)

        // Reduce the text size until it fits within the allowed width and meets the minimum size
        while (textBounds.width() > maxWidth && textSize > minTextSize) {
            textSize -= 1f
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            paint.getTextBounds(text, 0, text.length, textBounds)
        }
    }

    private fun getTextSizeBounds(adjustableTextType: AdjustableTextType): Pair<Float, Float> {
        // Fixed min and max text sizes for both Input and Output
        return when (adjustableTextType) {
            AdjustableTextType.Input,
            AdjustableTextType.Output -> Pair(16f, 25f) // minTextSize = 16sp, maxTextSize = 25sp
        }
    }

    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}
