package com.example.matheasyapp.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import com.agog.mathdisplay.MTMathView
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityResponseBinding
import com.example.matheasyapp.db.CameraHistory
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.db.ImageChunk
import com.example.matheasyapp.retrofit.CompletionResponse
import com.example.matheasyapp.retrofit.GPTRequest
import com.example.matheasyapp.retrofit.ImageUrl
import com.example.matheasyapp.retrofit.ImageUrlContent
import com.example.matheasyapp.retrofit.Message
import com.example.matheasyapp.retrofit.OpenAIService
import com.example.matheasyapp.retrofit.RetrofitClient
import com.example.matheasyapp.retrofit.TextContent
import com.example.matheasyapp.view.dialog.ProgressDialogManager
import com.google.android.flexbox.FlexboxLayout
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream


class ResponseActivity : AppCompatActivity() {

    private var photoFileNonBg: String? = null
    private var base64Img: String? = null
    private lateinit var progressDialogManager: ProgressDialogManager
    private lateinit var binding: ActivityResponseBinding

    private lateinit var database: HistoryDatabase

    private var rotageImage: String? = null

    private var photoFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        database = HistoryDatabase.getDatabase(this@ResponseActivity)

        binding = ActivityResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        photoFilePath = intent.getStringExtra("photo_file_path")

        photoFileNonBg = intent.getStringExtra("photo_non_background")
        rotageImage = intent.getStringExtra("rotage")

        progressDialogManager = ProgressDialogManager()
        progressDialogManager.showProgressDialog(this)

        if (photoFilePath != null) {

            lifecycleScope.launch {

                withContext(Dispatchers.IO) {
                    if (rotageImage != null) {
                        photoFilePath = processAndSaveImage(photoFilePath!!)
                    }

                    val photoFile = File(photoFilePath)

                    if (photoFile.exists()) {
                        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                        base64Img = bitmapToBase64(bitmap)
                        // Update UI Main Thread
                        withContext(Dispatchers.Main) {
                            if (photoFileNonBg != null) {
                                val fileNonBg = File(photoFileNonBg)
                                if (fileNonBg.exists()) {
                                    val bitmapNonBg =
                                        BitmapFactory.decodeFile(fileNonBg.absolutePath)
                                    binding.imageQuestion.setImageBitmap(bitmapNonBg)
                                }
                            } else {
                                val bitmap2 = base64ToBitmap(base64Img!!)
                                binding.imageQuestion.setImageBitmap(bitmap2)
                            }

                            lifecycleScope.launch {
                                withContext(Dispatchers.IO) {
                                    getGPT4Response(base64Img!!)
                                }
                            }
                        }
                    }
                }
            }

        }

        binding.expandButtonSolution.setOnClickListener {
            expand("solution")
        }

        binding.expandButtonResult.setOnClickListener {
            expand("result")

        }

        binding.expandButtonRequest.setOnClickListener {
            expand("request")
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }


    private fun getGPT4Response(base64String: String) {
        val service = RetrofitClient.instance.create(OpenAIService::class.java)
        val gptRequest = GPTRequest(
            model = "gpt-4-vision-preview",
            maxTokens = 300,
            messages = listOf(
                Message(
                    role = "user",
                    content = listOf(

                        TextContent(
                            type = "text",
                            text = "\n" +
                                    "Solve the following problems and answer the results according to the following structure, clearly explaining each step in detail," +
                                    " remember that for complex Latex expressions with 2, 3 or more Latex components, " +
                                    "give it \n . note : Most importantly, please give me a complete answer"
                        ),
                        TextContent(
                            type = "text",
                            text = "Answer: The answer is Insert the result of the math problem here , remember that for complex Latex expressions with 2, 3 or more Latex components, give it \n . note : Most importantly, please return the complete content, without interruptions or paragraphs"
                        ),
                        TextContent(
                            type = "text",
                            text = "Solution: The image shows the equation Insert math problem here = Insert there result Insert the detailed steps of solving the math problem here ,  remember that for complex Latex expressions with 2, 3 or more Latex components, give it \n . note : Most importantly, please return the complete content, without interruptions or paragraphs "
                        ),
                        ImageUrlContent(
                            type = "image_url",
                            imageUrl = ImageUrl(
                                url = "data:image/png;base64,${base64String}",
                                detail = "low"
                            )
                        )
                    )
                )
            )
        )

        service.getCompletion(gptRequest).enqueue(object : Callback<CompletionResponse> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<CompletionResponse>,
                response: Response<CompletionResponse>
            ) {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        progressDialogManager.hideProgressDialog()
                        if (response.isSuccessful) {
                            val responseText =
                                response.body()?.choices?.firstOrNull()?.message!!.content
                                    ?: "No response text"
                            binding.tvSolution.visibility = View.VISIBLE
                            val result = splitByKeywords(responseText)

                            if (result.isNotEmpty()) {
                                if (result.size >= 2) {
                                    processAndAddViews(
                                        applicationContext,
                                        result[0],
                                        binding.tvResult
                                    )
                                    processAndAddViews(
                                        applicationContext,
                                        result[1],
                                        binding.tvSolution
                                    )
                                    addHistory(result[0], result[1])
                                } else {
                                    binding.tvShowResult.visibility = View.VISIBLE
                                    binding.tvShowSolution.visibility = View.VISIBLE
                                    binding.tvShowResult.text = responseText
                                    binding.tvShowSolution.text = responseText
                                }
                            }
                        } else {
                            Log.d("ResponseActivity", "Response failed: ${response.code()}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CompletionResponse>, t: Throwable) {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        progressDialogManager.hideProgressDialog()
                        Log.d("ResponseActivity", "API call failed", t)
                    }
                }
            }
        })
    }

//    private fun getGPT4Response(base64String: String) {
//
//        val service = RetrofitClient.instance.create(OpenAIService::class.java)
//
//        val gptRequest = GPTRequest(
//            model = "gpt-4-vision-preview",
//            maxTokens = 300,
//            messages = listOf(
//                Message(
//                    role = "user",
//                    content = listOf(
//
////                        TextContent(
////                            type = "text",
////                            text = "Solve the following math problems and answer the results according to the following structure, clearly explaining each step in detail   "
////                        ),
//                        TextContent(
//                            type = "text",
//                            text = "\n" +
//                                    "Solve the following problems and answer the results according to the following structure, clearly explaining each step in detail," +
//                                    " remember that for complex Latex expressions with 2, 3 or more Latex components, " +
//                                    "give it \n . note : Most importantly, please give me a complete answer"
//                        ),
//                        TextContent(
//                            type = "text",
//                            text = "Answer: The answer is Insert the result of the math problem here , remember that for complex Latex expressions with 2, 3 or more Latex components, give it \n . note : Most importantly, please return the complete content, without interruptions or paragraphs"
//                        ),
//                        TextContent(
//                            type = "text",
//                            text = "Solution: The image shows the equation Insert math problem here = Insert there result Insert the detailed steps of solving the math problem here ,  remember that for complex Latex expressions with 2, 3 or more Latex components, give it \n . note : Most importantly, please return the complete content, without interruptions or paragraphs "
//                        ),
//                        ImageUrlContent(
//                            type = "image_url",
//                            imageUrl = ImageUrl(
//                                url = "data:image/png;base64,${base64String}",
//                                detail = "low"
//                            )
//                        )
//                    )
//                )
//            )
//        )
//
//
//        service.getCompletion(gptRequest).enqueue(object : Callback<CompletionResponse> {
//            @SuppressLint("SuspiciousIndentation")
//            override fun onResponse(
//                call: Call<CompletionResponse>,
//                response: Response<CompletionResponse>
//            ) {
//                if (response.isSuccessful) {
//                    progressDialogManager.hideProgressDialog();
//
//                    val responseText = response.body()?.choices?.firstOrNull()?.message!!.content
//                        ?: "No response text"
//                    binding.tvSolution.visibility = View.VISIBLE
//
//                    val result = splitByKeywords(responseText)
//
//                    if (result.isNotEmpty()) {
//
//                        if (result.size >= 2) {
//                            processAndAddViews(applicationContext, result.get(0), binding.tvResult)
//
//                            processAndAddViews(
//                                applicationContext,
//                                result.get(1),
//                                binding.tvSolution
//                            )
//                            addHistory(result.get(0).toString(), result.get(1).toString())
//                        } else {
//                            binding.tvShowResult.visibility = View.VISIBLE
//                            binding.tvShowSolution.visibility = View.VISIBLE
//                            binding.tvShowResult.setText(responseText.toString())
//                            binding.tvShowSolution.setText(responseText.toString())
//                        }
//
//                    }
//
//                } else {
//                    progressDialogManager.hideProgressDialog();
//                    Log.d("54352352", response.toString())
//
//                }
//            }
//
//            override fun onFailure(call: Call<CompletionResponse>, t: Throwable) {
//
//                progressDialogManager.hideProgressDialog();
//                Log.d("54352352", "error")
//
//
//            }
//        })
//    }

    fun splitByKeywords(input: String): List<String> {
        // Sử dụng biểu thức chính quy để tách chuỗi theo các từ khóa
        val regex = Regex("""(Answer:|Solution:)""")

        // Tách chuỗi theo các từ khóa, giữ lại các phần tử không rỗng
        val parts = regex.split(input).map { it.trim() }.filter { it.isNotEmpty() }

        // Trả về danh sách các phần tử sau khi tách
        return parts
    }

    private fun expand(value: String) {

        when (value) {

            "request" -> {
                var v: Int =
                    if (binding.imageQuestion.visibility == View.GONE) View.VISIBLE else View.GONE
                if (binding.imageQuestion.visibility == View.VISIBLE) {
                    binding.expandButtonRequest.setImageResource(R.drawable.ic_arrow_up)
                } else {
                    binding.expandButtonRequest.setImageResource(R.drawable.ic_arrow_down)
                }
                val autoTransition = AutoTransition().apply {
                    duration = 10
                }

                TransitionManager.beginDelayedTransition(binding.layoutQuestion, autoTransition)
                binding.imageQuestion.visibility = v
            }

            "result" -> {
                var v: Int =
                    if (binding.tvResult.visibility == View.GONE) View.VISIBLE else View.GONE

                if (binding.tvResult.visibility == View.VISIBLE) {
                    binding.expandButtonResult.setImageResource(R.drawable.ic_arrow_up)
                } else {
                    binding.expandButtonResult.setImageResource(R.drawable.ic_arrow_down)
                }

                val autoTransition = AutoTransition().apply {
                    duration = 10
                }

                TransitionManager.beginDelayedTransition(binding.layoutResult, autoTransition)
                binding.tvResult.visibility = v
            }

            "solution" -> {

                var v: Int =
                    if (binding.tvSolution.visibility == View.GONE) View.VISIBLE else View.GONE


                if (binding.tvSolution.visibility == View.VISIBLE) {
                    binding.expandButtonSolution.setImageResource(R.drawable.ic_arrow_up)
                } else {
                    binding.expandButtonSolution.setImageResource(R.drawable.ic_arrow_down)
                }

                val autoTransition = AutoTransition().apply {
                    duration = 10
                }

                TransitionManager.beginDelayedTransition(binding.layoutSolution, autoTransition)
                binding.tvSolution.visibility = v
            }

        }

    }


    fun processAndAddViews(context: Context, inputString: String, parentLayout: FlexboxLayout) {
        // Tách chuỗi đầu vào thành các dòng
        val lines = inputString.split("\n")

        for (line in lines) {
            // Biểu thức chính quy cập nhật để bao gồm tất cả các dạng biểu thức LaTeX
            val regex = Regex("(\\\\\\[.*?\\\\\\]|\\$\\$.*?\\$\\$|\\$.*?\\$|\\\\\\(.*?\\\\\\))")

            // Phân tách chuỗi dòng dựa trên biểu thức chính quy
            val parts = regex.split(line)
            val matches = regex.findAll(line)

            // Iterator cho các kết quả khớp
            val matchIterator = matches.iterator()

            for (part in parts) {
                if (part.isNotBlank()) {
                    // Thêm TextView cho các phần văn bản không chứa LaTeX
                    val textView = TextView(context)
                    textView.text = part.trim()
                    textView.textSize = 16f
                    val typeface = ResourcesCompat.getFont(context, R.font.inter_28pt_light)
                    textView.typeface = typeface
                    textView.setTextColor(resources.getColor(R.color.currency))
                    textView.setPadding(0, 0, 0, 0)
                    val params = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(5, 7, 5, 7)
                    }
                    textView.layoutParams = params
                    parentLayout.addView(textView)
                }
                if (matchIterator.hasNext()) {
                    val match = matchIterator.next().value

                    // Tạo MTMathView và thiết lập nội dung LaTeX
                    val mtv = MTMathView(context, null, 0)
                    mtv.fontSize = 44f

                    // Loại bỏ các ký tự bao quanh từ các dạng biểu thức LaTeX
                    val latexContent = match
                        .removeSurrounding("\\[", "\\]")
                        .removeSurrounding("$$", "$$")
                        .removeSurrounding("\\(", "\\)")
                        .replace("\\\\", "\\")

                    mtv.latex = latexContent.trim()

                    Log.d("42523452352", latexContent.toString())

                    // Thiết lập margin cho MTMathView
                    val params = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(10, 0, 10, 0)
                    }
                    mtv.setPadding(0, 0, 0, 0)
                    mtv.layoutParams = params
                    parentLayout.addView(mtv)
                }
            }

            // Thêm một dòng trống nếu không phải dòng cuối cùng
            if (line != lines.last()) {
                val spaceView = View(context)
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    10 // Chiều cao của dòng trống
                )
                spaceView.layoutParams = params
                parentLayout.addView(spaceView)
            }
        }
    }


    private suspend fun addHistory(result: String, solution: String) {


        // add to database image not background
//        if (photoFileNonBg != null) {
//            val photoFile = File(photoFileNonBg)
//            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
//            base64Img = bitmapToBase64(bitmap)
//        }

        lifecycleScope.launch {
            val compressedBase64 = compressImage(base64Img!!)
            if (compressedBase64 != null) {
                val history =
                    CameraHistory(result = result, solution = solution, base64 = compressedBase64)
                val newId = database.cameraHistoryDao().insert(history)
                Log.d("Database", "Đã lưu chuỗi Base64 nén với ID: $newId")
            } else {
                // Xử lý khi nén thất bại
                Log.e("Error535235235", "Nén chuỗi Base64 thất bại")
            }

        }


    }


    fun compressImage(base64String: String): String? {
        try {
            // 1. Chuyển đổi chuỗi Base64 thành Bitmap
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // 2. Nén ảnh
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                50,
                outputStream
            ) // Giảm chất lượng ảnh xuống 50%

            // 3. Chuyển đổi ảnh đã nén thành Base64
            val compressedBytes = outputStream.toByteArray()
            return Base64.encodeToString(compressedBytes, Base64.DEFAULT)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun compressBase64String(data: String): String? {
        return try {
            val byteStream = ByteArrayOutputStream()
            val gzipStream = GZIPOutputStream(byteStream)
            gzipStream.write(data.toByteArray())
            gzipStream.close()

            val compressedData = byteStream.toByteArray()
            byteStream.close()

            // Chuyển mảng byte nén sang Base64 để lưu trữ
            Base64.encodeToString(compressedData, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun processAndSaveImage(filePath: String): String? {
        var newFilePath: String? = null
        try {
            // Load the image from the file path
            val bitmap = BitmapFactory.decodeFile(filePath)

            // Get the rotation angle from the image's EXIF data
            val exif = ExifInterface(filePath)
            val rotation = when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            // Rotate the bitmap based on the EXIF data
            val rotatedBitmap = rotateBitmap(bitmap, rotation)

            // Save the rotated bitmap to a new file
            val newFile =
                File(ResponseActivity@ this.externalCacheDir, "${System.currentTimeMillis()}.jpg")
            FileOutputStream(newFile).use { outputStream ->
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            newFilePath = newFile.absolutePath

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return newFilePath
    }

    private fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
        if (rotation == 0) return bitmap

        val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


}
