package com.example.matheasyapp.view.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.matheasyapp.R
import com.example.matheasyapp.broadcast.BroadcastReceiverInternet
import com.example.matheasyapp.broadcast.registerNetworkReceiver
import com.example.matheasyapp.broadcast.unregisterNetworkReceiver
import com.example.matheasyapp.databinding.FragmentPaintBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PaintFragment : Fragment() {

    private lateinit var binding: FragmentPaintBinding

    private lateinit var networkReceiver: BroadcastReceiverInternet

    private var savedState: ByteArray? = null

    private var dialog: Dialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaintBinding.inflate(inflater)

        binding.canvasView.setDrawMode(false)

        binding.image.visibility = View.GONE

        onClick()

        handleConnect()

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val bitmapState = binding.canvasView.saveState()
        outState.putByteArray("bitmapState", bitmapState)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        // Khôi phục trạng thái của bitmap từ bundle

        savedInstanceState?.let {
            val canvasView = binding.canvasView
            val bitmapState = it.getByteArray("bitmapState")
            canvasView?.restoreState(bitmapState)
        }

    }


    private fun handleConnect() {
        // Đăng ký BroadcastReceiver để theo dõi thay đổi kết nối mạng
        networkReceiver = registerNetworkReceiver(requireContext()) { isConnected ->
            // Xử lý sự thay đổi kết nối mạng
            if (!isConnected) {
                showInputDialog(true)
            } else {
                showInputDialog(false)
            }
        }
    }

    private fun onClick() {

        // change erase
        binding.layoutErase.setOnClickListener {
            binding.canvasView.setDrawMode(false)

            binding.layoutErase.translationY = -50F
            binding.layoutPen.translationY = 27F

        }

        // change paint
        binding.layoutPen.setOnClickListener {
            binding.layoutPen.translationY = -50F
            binding.layoutErase.translationY = 27F
            binding.canvasView.setDrawMode(true)

        }


        binding.btnTrash.setOnClickListener {

            binding.canvasView.clearCanvas()

        }

        binding.btnUndo.setOnClickListener { binding.canvasView.undo() }

        binding.btnRedo.setOnClickListener { binding.canvasView.redo() }

        binding.btnNext.setOnClickListener {

            if (isInternetConnected()) {
                var bitmap: Bitmap? = binding.canvasView.getBitmap()
                if (bitmap != null) {
                    binding.canvasView.visibility = View.GONE

                    binding.image.visibility = View.VISIBLE
                    binding.image.setImageBitmap(binding.canvasView.getBitmap()!!)

                    var fileImageWhiteBackground: File? =
                        binding.canvasView.getBitmapWithWhiteBackground(requireContext())

                    var fileNonBackground: File? =
                        bitmapToFileInCache(binding.canvasView.getBitmap()!!)


                    binding.image.setImageURI(Uri.parse(fileImageWhiteBackground!!.path))

                    var intent = Intent(requireActivity(), ResponseActivity::class.java)
                    intent.putExtra("photo_file_path", fileImageWhiteBackground!!.absolutePath)
                    intent.putExtra("photo_non_background", fileNonBackground!!.absolutePath)


                    startActivity(intent)

                }
            } else {
                showDialogNotification()
            }

        }
    }


    fun bitmapToFileInCache(bitmap: Bitmap): File? {
        return try {
            // Tạo một tệp mới trong thư mục cache
            val file = File(requireActivity().cacheDir, "fileName")

            //  FileOutputStream ghi dữ liệu từ Bitmap vào file
            val outputStream = FileOutputStream(file)

            // Nén Bitmap thành định dạng PNG hoặc JPEG và ghi vào FileOutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Đóng FileOutputStream
            outputStream.flush()
            outputStream.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun showInputDialog(show: Boolean) {
        if (show) {
            if (dialog == null || !dialog!!.isShowing) {
                // Chỉ tạo mới dialog nếu nó chưa được tạo hoặc không đang hiển thị
                dialog = Dialog(requireContext())
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
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    override fun onPause() {
        super.onPause()

        val bitmap = binding.canvasView.getBitmap()
        bitmap?.let {
            binding.canvasView.saveBitmapToFileCache(it) // Lưu bitmap vào file cache
        }
    }

    override fun onResume() {
        super.onResume()

        val savedBitmap =
            binding.canvasView.getBitmapFromFileCache() // Hoặc lưu bitmap vào file rồi đọc lại
        savedBitmap?.let {
            binding.canvasView.restoreState(it) // Khôi phục bitmap từ file cache
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        unregisterNetworkReceiver(requireContext(), networkReceiver)
    }

    private fun showDialogNotification() {

        var dialogNotifi: Dialog = Dialog(requireContext())

        dialogNotifi.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialogNotifi.setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
        dialogNotifi.setContentView(R.layout.layout_dialog_notifi)  // Đặt nội dung sau khi gọi requestWindowFeature

        dialogNotifi.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnClose = dialogNotifi.findViewById<LinearLayout>(R.id.layoutClose)

        btnClose.setOnClickListener {
            dialogNotifi.dismiss()
        }

        dialogNotifi.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialogNotifi.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialogNotifi.window?.attributes = layoutParams
        dialogNotifi.show()

    }


}