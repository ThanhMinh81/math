package com.example.matheasyapp.view.home.paint

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var paint: Paint = Paint()
    private var path: Path = Path()
    private var brushSize: Float = 10f
    private var eraserSize: Float = brushSize * 5
    private var drawMode: Boolean = false // true = Draw, false = Erase
    private var canvas: Canvas? = null
    private var bitmap: Bitmap? = null
    private var bitmapPaint: Paint = Paint(Paint.DITHER_FLAG)

    private val undoStack = mutableListOf<Bitmap>()
    private val redoStack = mutableListOf<Bitmap>()

    init {
        setupPaint()
    }

    private fun setupPaint() {
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = brushSize
    }

    fun setBrushSize(newSize: Float) {
        brushSize = newSize
        if (drawMode) {
            paint.strokeWidth = brushSize
        } else {
            eraserSize = brushSize * 5
            paint.strokeWidth = eraserSize
        }
    }

    fun setDrawMode(isDrawMode: Boolean) {
        drawMode = isDrawMode
        if (drawMode) {
            paint.xfermode = null
            paint.color = Color.BLACK // Reset color if needed
            paint.strokeWidth = brushSize // Set to normal brush size
        } else {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            paint.strokeWidth = eraserSize // Set to larger eraser size
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.add(bitmap!!.copy(bitmap!!.config, true))
            bitmap = undoStack.removeAt(undoStack.size - 1)
            canvas = Canvas(bitmap!!)
            invalidate()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            saveStateForUndo()
            bitmap = redoStack.removeAt(redoStack.size - 1)
            canvas = Canvas(bitmap!!)
            invalidate()
        }
    }

    fun clearCanvas() {
        bitmap?.eraseColor(Color.TRANSPARENT)
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        invalidate()
    }

    private fun saveStateForUndo() {
        undoStack.add(bitmap!!.copy(bitmap!!.config, true))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, 0f, 0f, bitmapPaint)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                saveStateForUndo()
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                if (!drawMode) {
                    canvas?.drawPath(path, paint)
                    path.reset()
                    path.moveTo(x, y)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (drawMode) {
                    path.lineTo(x, y)
                    canvas?.drawPath(path, paint)
                } else {
                    canvas?.drawPath(path, paint)
                }
                path.reset()
            }
        }
        invalidate()
        return true
    }

    fun getBitmap(): Bitmap? {
        // Kiểm tra bitmap có null không trước khi copy
        return bitmap?.let {
            // Tạo một bản sao của bitmap với cùng cấu hình và nội dung
            it.copy(it.config, true)
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        return try {
            // Tạo thư mục lưu trữ nếu chưa tồn tại
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(storageDir, fileName)
            FileOutputStream(file).use { outputStream ->
                // Nén bitmap thành PNG và lưu vào file
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun getBitmapWithWhiteBackground(context: Context): File? {
        // Tạo bitmap với nền trắng
        val whiteBitmap = Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val whiteCanvas = Canvas(whiteBitmap)
        whiteCanvas.drawColor(Color.WHITE) // Vẽ nền trắng

        // Vẽ bitmap hiện tại lên bitmap nền trắng
        val combinedBitmap = Bitmap.createBitmap(whiteBitmap.width, whiteBitmap.height, Bitmap.Config.ARGB_8888)
        val combinedCanvas = Canvas(combinedBitmap)
        combinedCanvas.drawBitmap(whiteBitmap, 0f, 0f, null) // Vẽ nền trắng lên canvas
        combinedCanvas.drawBitmap(bitmap!!, 0f, 0f, null) // Vẽ bitmap hiện tại lên nền trắng

        // Lưu bitmap vào bộ nhớ cache và trả về file
        return saveBitmapToCache(context, combinedBitmap)
    }

    private fun saveBitmapToCache(context: Context, bitmap: Bitmap): File? {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, "drawing_${System.currentTimeMillis()}.png")

        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Thêm phương thức lưu trạng thái của bitmap
    fun saveState(): ByteArray? {
        return bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
    }

    // Thêm phương thức để khôi phục trạng thái của bitmap

    fun restoreState(state: ByteArray?) {
        state?.let {
            val inputStream = ByteArrayInputStream(it)

            val restoredBitmap = BitmapFactory.decodeStream(inputStream)

            // Tạo một bitmap có thể chỉnh sửa từ restoredBitmap
            bitmap = restoredBitmap.copy(Bitmap.Config.ARGB_8888, true)

            // Tạo lại canvas với bitmap đã chỉnh sửa
            canvas = Canvas(bitmap!!)
            invalidate()
        }
    }

    //    fun restoreState(state: ByteArray?) {
//        state?.let {
//            val inputStream = ByteArrayInputStream(it)
//            bitmap = BitmapFactory.decodeStream(inputStream)
//            canvas = Canvas(bitmap!!)
//            invalidate()
//        }
//    }
      fun saveBitmapToFileCache(bitmap: Bitmap): Boolean {
        val file = File(context.cacheDir, "canvas_bitmap.png")
        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

      fun getBitmapFromFileCache(): ByteArray? {
        val file = File(context.cacheDir, "canvas_bitmap.png")
        return if (file.exists()) {
            file.readBytes()
        } else {
            null
        }
    }



}
