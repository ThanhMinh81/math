package com.example.matheasyapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.matheasyapp.Stroke

class DrawView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint = Paint()
    private val paths = ArrayList<Stroke>()
    private val redoStack = ArrayList<Stroke>() // Thêm redoStack
    private var currentColor = Color.GREEN
    private var strokeWidth = 20
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    private var isPaintable = true
    private var isErasing = false
    private var eraseWidth = 50 // Độ rộng của vùng xóa

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.alpha = 0xff
    }

    fun init(height: Int, width: Int) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
    }

    fun setColor(color: Int) {
        currentColor = color
    }

    fun setStrokeWidth(width: Int) {
        strokeWidth = width
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            // Lưu trạng thái vào redoStack trước khi xóa
            redoStack.add(paths.removeAt(paths.size - 1))
            invalidate()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            // Khôi phục từ redoStack về paths
            paths.add(redoStack.removeAt(redoStack.size - 1))
            invalidate()
        }
    }

    fun clear() {
        paths.clear() // Xóa tất cả các đường vẽ
        redoStack.clear() // Xóa redoStack
        invalidate() // Yêu cầu vẽ lại để làm sạch màn hình
    }

    fun setEraseMode(enabled: Boolean) {
        isErasing = enabled
        mPaint.xfermode = if (enabled) PorterDuffXfermode(PorterDuff.Mode.CLEAR) else null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            canvas.drawPath(fp.path, mPaint)
        }
        mBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, mBitmapPaint)
        }
    }

    private fun touchStart(x: Float, y: Float) {
        Log.d("DrawView", "Touch start at: $x, $y")
        mPath = Path()
        val fp = Stroke(currentColor, strokeWidth, mPath!!)
        paths.add(fp)
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        Log.d("DrawView", "Touch move to: $x, $y")
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
    }

    public fun changeStatePaint(statePaint: Boolean) {
        this.isPaintable = statePaint
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

//        if (isErasing) {
//            handleErase(x, y)
//            return true
//        }

        if (isPaintable) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchStart(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    touchMove(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    touchUp()
                    invalidate()
                }
            }
            return true
        }
        return false
    }





    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }

}
