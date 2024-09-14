package com.example.matheasyapp.view.picture


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.matheasyapp.databinding.ActivityCropImageBinding
import com.example.matheasyapp.view.home.ResponseActivity
import com.lyrebirdstudio.croppylib.ui.CroppedBitmapData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.camera.core.AspectRatio
import androidx.core.net.toUri
import com.example.matheasyapp.R
import com.example.matheasyapp.broadcast.BroadcastReceiverInternet
import com.example.matheasyapp.broadcast.registerNetworkReceiver
import com.example.matheasyapp.broadcast.unregisterNetworkReceiver
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyActivity
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest

class CropImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropImageBinding

    private lateinit var networkReceiver: BroadcastReceiverInternet

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imgPath = intent.getStringExtra("img_path")

        binding = ActivityCropImageBinding.inflate(layoutInflater)

        val file = File(imgPath)
        val imageUri = Uri.fromFile(file)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)


        binding.croppedImageView.post {
            if (imageUri != null) {
                binding.croppedImageView.setBitmap(bitmap)
            }
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
            finish()
        }


        binding.icCheck.setOnClickListener {
            if (isInternetConnected()) {
                cropImage()
            } else {
                showDialogNotification()
            }
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        setContentView(binding.root)

        handleConnect()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            data?.data?.let {
                Log.v("TEST", it.toString())
//                binding.croppedImageView.setImageURI(it)
            }
        }
    }

    private fun showDialogNotification() {

        var dialogNotifi: Dialog = Dialog(this)

        dialogNotifi.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialogNotifi.setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
        dialogNotifi.setContentView(R.layout.layout_dialog_notifi)  // Đặt nội dung sau khi gọi requestWindowFeature

        dialogNotifi.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnClose = dialogNotifi.findViewById<LinearLayout>(R.id.layoutClose)

        btnClose.setOnClickListener {
            dialogNotifi.dismiss()
        }

        dialogNotifi.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialogNotifi.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialogNotifi.window?.attributes = layoutParams
        dialogNotifi.show()

    }

    private fun cropImage() {

        binding.croppedImageView.post {

            val croppedImageUri = binding.croppedImageView.alpha

            if (croppedImageUri != null) {
                val cropBitmap: CroppedBitmapData = binding.croppedImageView.getCroppedData()

                val croppedBitmap: CroppedBitmapData? = binding.croppedImageView.getCroppedData()
                if (croppedBitmap != null) {
                    val bitmap: Bitmap? = croppedBitmap!!.croppedBitmap
                    if (bitmap != null) {
                        val file = File(this.cacheDir, "image.png")
                        val resultFile = bitmapToFile(bitmap, file)
                        if (resultFile != null) {
                            var intent: Intent = Intent(this, ResponseActivity::class.java)
                            intent.putExtra("photo_file_path", resultFile.absolutePath)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun handleConnect() {
        // Đăng ký BroadcastReceiver để theo dõi thay đổi kết nối mạng
        networkReceiver = registerNetworkReceiver(baseContext) { isConnected ->
            // Xử lý sự thay đổi kết nối mạng
            if (!isConnected) {
                showInputDialog(true)
            } else {
                showInputDialog(false)
//                if(dialog != null ){
//                    if (dialog.isShowing){
//                        dialog.dismiss()
//                    }
//                }
            }
        }
    }

    // convert bitmap to file

    fun bitmapToFile(bitmap: Bitmap, file: File): File? {
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    companion object {
        private const val RC_CROP_IMAGE = 102
    }


    fun showInputDialog(show: Boolean) {
        if (show) {
            if (dialog == null || !dialog!!.isShowing) {
                // Chỉ tạo mới dialog nếu nó chưa được tạo hoặc không đang hiển thị
                dialog = Dialog(this)
                dialog?.apply {
                    // Gọi requestWindowFeature trước khi thêm content
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
                    setContentView(R.layout.dialog_check_connect)  // Đặt nội dung sau khi gọi requestWindowFeature
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    val btnRefresh: Button = findViewById(R.id.btnRefresh)
                    btnRefresh.setOnClickListener {
                        val checkConnect = isInternetConnected()

                        if (checkConnect) {
                            dismiss()
                        } else {
                            blinkDialog(this)
                        }
                    }

                    window?.setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )

                    val layoutParams = window?.attributes
                    layoutParams?.gravity = Gravity.CENTER
                    window?.attributes = layoutParams

                    show()
                }
            }
        } else {
            dialog?.dismiss()
            dialog = null
        }
    }


    private fun blinkDialog(dialog: Dialog) {
        val handler = Handler()
        val blinkDuration = 200L // Thời gian nhấp nháy 200ms
        val blinkInterval = 200L // Thời gian nhấp nháy giữa các lần ẩn hiện (200ms)

        val showRunnable = object : Runnable {
            override fun run() {
                dialog.window?.decorView?.let {
                    if (it.visibility == View.VISIBLE) {
                        it.visibility = View.INVISIBLE
                    } else {
                        it.visibility = View.VISIBLE
                    }
                    handler.postDelayed(this, blinkInterval)
                }
            }
        }

        // Bắt đầu nhấp nháy
        handler.post(showRunnable)

        // Dừng hiệu ứng nhấp nháy sau 500ms
        handler.postDelayed({
            dialog.window?.decorView?.visibility = View.VISIBLE
            handler.removeCallbacks(showRunnable)
        }, blinkDuration)
    }

    fun isInternetConnected(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        unregisterNetworkReceiver(this, networkReceiver)
    }

}