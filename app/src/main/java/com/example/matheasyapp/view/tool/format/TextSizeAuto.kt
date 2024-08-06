import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import kotlin.math.max

class TextSizeSyncManager(
    private val editText1: EditText,
    private val editText2: EditText,
    private val initialTextSize: Float = 20f,
    private val minTextSize: Float = 14f,
    private val thresholdLength: Int = 10,
    private val scaleStep: Float = 2f
) {

    private var currentTextSize: Float = initialTextSize

    init {
        attachTextWatchers()
    }

    private fun attachTextWatchers() {
        editText1.addTextChangedListener {
            syncTextSize()
        }

        editText2.addTextChangedListener {
            syncTextSize()
        }
    }

    private fun syncTextSize() {
        val maxLength = maxOf(editText1.text.length, editText2.text.length)
        val newSize = calculateTextSize(maxLength)
        if (newSize != currentTextSize) {
            currentTextSize = newSize
            editText1.textSize = currentTextSize
            editText2.textSize = currentTextSize
        }
    }

    private fun calculateTextSize(length: Int): Float {
        return if (length > thresholdLength) {
            max(initialTextSize - ((length - thresholdLength) / scaleStep) * scaleStep, minTextSize)
        } else {
            initialTextSize
        }
    }
}
